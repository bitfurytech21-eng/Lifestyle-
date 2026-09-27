package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GalleryRepository
import com.example.data.GeminiApiClient
import com.example.data.TextToSpeechHelper
import com.example.data.TimelineCacheRepository
import com.example.data.TimelineRepository
import com.example.data.sync.TimelineSyncPreferences
import com.example.data.sync.TimelineSyncScheduler
import androidx.work.WorkInfo
import com.example.data.VoiceProfileRepository
import com.example.data.VoiceProfileStorage
import com.example.data.VoiceRecorderHelper
import com.example.model.AccentStyle
import com.example.model.CALIBRATION_SENTENCES
import com.example.model.ChatMessage
import com.example.model.CountryEdition
import com.example.model.DayCategory
import com.example.model.GalleryMediaItem
import com.example.model.PracticeScenario
import com.example.model.TimelineEntry
import com.example.model.VoiceProfile
import com.example.data.TutorResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class AppTab(val americanTitle: String, val canadianTitle: String) {
    TIMELINE("Daily Life", "Daily Life"),
    GALLERY("Photos & Videos", "Photos & Videos"),
    TUTOR("AI English Tutor", "AI English Tutor")
}

enum class CloningStep {
    INTRO,
    RECORDING,
    ANALYZING,
    CALIBRATION_COMPLETE
}

data class DailyAmericanUiState(
    val selectedEdition: CountryEdition = CountryEdition.AMERICAN,
    val selectedTab: AppTab = AppTab.TIMELINE,
    val selectedCategory: DayCategory = DayCategory.WEEKDAY,
    val timelineEntries: List<TimelineEntry> = emptyList(),
    val selectedEntry: TimelineEntry? = null,
    val bookmarkedIds: Set<String> = emptySet(),
    val chatMessages: List<ChatMessage> = emptyList(),
    val isTutorThinking: Boolean = false,
    val showSavedNotes: Boolean = false,
    val errorMessage: String? = null,
    val selectedScenario: PracticeScenario = PracticeScenario.CASUAL_CHAT,
    val isSpeakingSlowly: Boolean = false,

    // Room Database Timeline Caching State (Offline Browsing)
    val isOfflineCached: Boolean = true,
    val cachedTotalCount: Int = 0,
    val isRefreshingCache: Boolean = false,
    val cacheStatusMessage: String = "Offline Ready • Cached in Room DB",

    // Background WorkManager Synchronization State
    val isSyncingWithRemote: Boolean = false,
    val lastSyncTimeFormatted: String = "Scheduled (when online)",
    val syncWorkerSummary: String = "WorkManager auto-sync active (every 6 hrs when online)",

    // Photos & Driveway Videos Gallery
    val galleryItems: List<GalleryMediaItem> = GalleryRepository.defaultItems,
    val galleryFavoriteIds: Set<String> = emptySet(),

    // Voice Cloning & Profiles
    val availableVoices: List<VoiceProfile> = emptyList(),
    val activeVoiceProfile: VoiceProfile = TextToSpeechHelper.defaultVoiceProfile(),
    val showVoiceStudio: Boolean = false,
    val cloningStep: CloningStep = CloningStep.INTRO,
    val currentSentenceIndex: Int = 0,
    val isRecordingVoice: Boolean = false,
    val liveAmplitude: Float = 0f,
    val waveformSamples: List<Float> = emptyList(),
    val recordedAudioPath: String? = null,
    val analyzedPitchHz: Float = 160f,
    val draftCloneName: String = "My Cloned Voice",
    val draftPitch: Float = 1.0f,
    val draftSpeed: Float = 0.95f,
    val draftStyle: AccentStyle = AccentStyle.CUSTOM_CLONE,
    val previewText: String = "Hey there! This is my cloned voice profile for daily practice."
)

class DailyAmericanViewModel(application: Application) : AndroidViewModel(application) {

    private val ttsHelper = TextToSpeechHelper(application)
    private val voiceRecorder = VoiceRecorderHelper(application)
    private val voiceStorage = VoiceProfileStorage(application)
    private val voiceRepository = VoiceProfileRepository(application)
    private val timelineCacheRepo = TimelineCacheRepository(application)
    private val syncPreferences = TimelineSyncPreferences(application)
    private var timelineObservationJob: Job? = null

    private val initialEdition = voiceStorage.getSelectedEdition()
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)

    private val _uiState = MutableStateFlow(
        DailyAmericanUiState(
            selectedEdition = initialEdition,
            timelineEntries = TimelineRepository.getEntriesFor(initialEdition, DayCategory.WEEKDAY),
            chatMessages = listOf(
                createInitialChatMessage(initialEdition)
            )
        )
    )
    val uiState: StateFlow<DailyAmericanUiState> = _uiState.asStateFlow()

    init {
        // Seed Room Database with Daily Timeline Data & observe reactive updates
        viewModelScope.launch {
            val totalCached = timelineCacheRepo.ensureCacheSeeded()
            _uiState.update {
                it.copy(
                    isOfflineCached = true,
                    cachedTotalCount = totalCached,
                    cacheStatusMessage = "Room DB: $totalCached daily routines cached offline"
                )
            }
        }

        // Start reactive Room observation for initial edition & category
        observeTimelineFromCache(initialEdition, DayCategory.WEEKDAY)

        // Observe persistent cloned voice profiles from Room Database
        viewModelScope.launch {
            voiceRepository.allClonedProfiles.collect { customProfiles ->
                val allProfiles = TextToSpeechHelper.defaultPresets + customProfiles
                val savedId = voiceStorage.getSelectedProfileId()
                val currentActive = _uiState.value.activeVoiceProfile
                val active = allProfiles.find { it.id == savedId }
                    ?: allProfiles.find { it.id == currentActive.id }
                    ?: TextToSpeechHelper.defaultVoiceProfile(_uiState.value.selectedEdition)

                ttsHelper.setVoiceProfile(active)

                _uiState.update {
                    it.copy(
                        availableVoices = allProfiles,
                        activeVoiceProfile = active
                    )
                }
            }
        }

        viewModelScope.launch {
            voiceRecorder.amplitudeFlow.collect { amp ->
                _uiState.update { it.copy(liveAmplitude = amp) }
            }
        }
        viewModelScope.launch {
            voiceRecorder.waveformSamples.collect { samples ->
                _uiState.update { it.copy(waveformSamples = samples) }
            }
        }
        viewModelScope.launch {
            voiceRecorder.isRecording.collect { rec ->
                _uiState.update { it.copy(isRecordingVoice = rec) }
            }
        }

        // Observe WorkManager background sync metadata and execution
        viewModelScope.launch {
            syncPreferences.syncMetadataFlow.collect { meta ->
                _uiState.update {
                    it.copy(
                        lastSyncTimeFormatted = meta.formattedLastSyncTime,
                        syncWorkerSummary = meta.lastSyncSummary
                    )
                }
            }
        }

        viewModelScope.launch {
            try {
                TimelineSyncScheduler.observeSyncWorkInfo(application).collect { workInfoList ->
                    val isRunning = workInfoList.any { it.state == WorkInfo.State.RUNNING }
                    _uiState.update { it.copy(isSyncingWithRemote = isRunning) }
                }
            } catch (e: Throwable) {
                android.util.Log.w("DailyAmericanVM", "WorkInfo observation skipped: ${e.message}")
            }
        }
    }

    /**
     * Triggers an immediate background synchronization via WorkManager
     * for both 'A Day in America' and 'Canadian' editions.
     */
    fun triggerRemoteSync() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncingWithRemote = true) }
            TimelineSyncScheduler.triggerImmediateSync(getApplication())
        }
    }

    private fun observeTimelineFromCache(edition: CountryEdition, category: DayCategory) {
        timelineObservationJob?.cancel()
        timelineObservationJob = viewModelScope.launch {
            timelineCacheRepo.observeEntries(edition, category).collect { cachedList ->
                val resolvedList = if (cachedList.isNotEmpty()) {
                    cachedList
                } else {
                    TimelineRepository.getEntriesFor(edition, category)
                }
                _uiState.update {
                    it.copy(
                        timelineEntries = resolvedList,
                        isOfflineCached = true
                    )
                }
            }
        }
    }

    fun refreshTimelineCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshingCache = true) }
            val count = timelineCacheRepo.refreshCache()
            _uiState.update {
                it.copy(
                    isRefreshingCache = false,
                    isOfflineCached = true,
                    cachedTotalCount = count,
                    cacheStatusMessage = "Room DB: $count routines refreshed & verified in local SQLite cache"
                )
            }
        }
    }

    private fun loadVoiceProfiles() {
        // Maintained for backward compatibility or immediate sync
        viewModelScope.launch {
            val customProfiles = voiceStorage.loadCustomProfiles()
            if (customProfiles.isNotEmpty()) {
                // If any legacy custom profiles exist in SharedPreferences, migrate them to Room
                customProfiles.forEach { legacy ->
                    voiceRepository.saveClonedVoice(
                        name = legacy.name,
                        style = legacy.style,
                        pitch = legacy.pitch,
                        speechRate = legacy.speechRate,
                        pitchVariance = legacy.pitchVariance,
                        tempAudioWavPath = legacy.sampleAudioPath,
                        calibratedPitchHz = legacy.calibratedPitchHz
                    )
                }
                voiceStorage.saveCustomProfiles(emptyList()) // clear legacy after migration
            }
        }
    }

    fun switchCountryEdition(edition: CountryEdition) {
        if (_uiState.value.selectedEdition == edition) return

        voiceStorage.saveSelectedEdition(edition)
        val defaultVoice = TextToSpeechHelper.defaultVoiceProfile(edition)

        ttsHelper.setVoiceProfile(defaultVoice)

        _uiState.update { state ->
            state.copy(
                selectedEdition = edition,
                activeVoiceProfile = defaultVoice,
                chatMessages = listOf(createInitialChatMessage(edition, state.selectedScenario))
            )
        }
        observeTimelineFromCache(edition, _uiState.value.selectedCategory)
    }

    private fun createInitialChatMessage(
        edition: CountryEdition,
        scenario: PracticeScenario = PracticeScenario.CASUAL_CHAT
    ): ChatMessage {
        val tutorName = if (edition == CountryEdition.CANADIAN) "Robin" else "Sam"
        val nationName = if (edition == CountryEdition.CANADIAN) "Canadian" else "American"

        val welcomeText = when (scenario) {
            PracticeScenario.CASUAL_CHAT ->
                "Hey there! I'm $tutorName, your $nationName English tutor. Whether you want to practice casual conversation, master everyday idioms, or ask about daily customs, I'm right here with you! What would you like to practice today?"
            PracticeScenario.DRIVE_THRU_DINER ->
                "Welcome to the counter! I'm taking your order today. What kind of coffee or meal can I get started for you? (Try ordering an iced drink or breakfast!)"
            PracticeScenario.WORKPLACE ->
                "Good morning! Let's do a quick sync before our meetings start. How are your projects coming along, and is there anything we should touch base on?"
            PracticeScenario.ERRANDS_SHOPPING ->
                "Hi there! Welcome in. Are you looking for anything in particular today, or can I help you find an aisle?"
            PracticeScenario.SLANG_AND_IDIOMS ->
                "Welcome to our idioms and slang lab! Ask me about any phrase you've heard, or try using a colloquial idiom in a sentence and we'll refine it together."
        }

        return ChatMessage(
            text = welcomeText,
            isUser = false,
            timestamp = timeFormat.format(Date()),
            suggestedFollowUps = scenario.getPromptsFor(edition),
            languageTip = if (edition == CountryEdition.CANADIAN) "In Canada, saying 'please' and 'sorry' generously makes you sound right at home!" else "In the US, short friendly greetings like 'How's it going?' or 'Have a good one!' are customary.",
            idiomSpotlight = if (edition == CountryEdition.CANADIAN) "Double-Double: Coffee with two creams and two sugars." else "Touch base: Briefly talk or update each other."
        )
    }

    fun selectPracticeScenario(scenario: PracticeScenario) {
        if (_uiState.value.selectedScenario == scenario) return
        val edition = _uiState.value.selectedEdition
        _uiState.update { state ->
            state.copy(
                selectedScenario = scenario,
                chatMessages = listOf(createInitialChatMessage(edition, scenario))
            )
        }
    }

    fun selectVoiceProfile(profile: VoiceProfile) {
        ttsHelper.setVoiceProfile(profile)
        voiceStorage.saveSelectedProfileId(profile.id)
        _uiState.update { it.copy(activeVoiceProfile = profile) }
    }

    fun toggleVoiceStudio(show: Boolean) {
        if (show) {
            ttsHelper.stop()
            stopRecordingVoice()
        }
        _uiState.update {
            it.copy(
                showVoiceStudio = show,
                cloningStep = CloningStep.INTRO,
                currentSentenceIndex = 0
            )
        }
    }

    // --- Voice Cloning Wizard Flow ---

    fun startCloningFlow() {
        _uiState.update {
            it.copy(
                cloningStep = CloningStep.RECORDING,
                currentSentenceIndex = 0
            )
        }
    }

    fun startRecordingCurrentSentence(): Boolean {
        val index = _uiState.value.currentSentenceIndex
        return voiceRecorder.startRecording(index, viewModelScope)
    }

    fun stopAndProcessSentenceRecording() {
        val wavFile = voiceRecorder.stopRecording()
        val currentIndex = _uiState.value.currentSentenceIndex

        if (currentIndex < CALIBRATION_SENTENCES.size - 1) {
            _uiState.update {
                it.copy(
                    currentSentenceIndex = currentIndex + 1,
                    recordedAudioPath = wavFile?.absolutePath
                )
            }
        } else {
            _uiState.update { it.copy(cloningStep = CloningStep.ANALYZING) }

            viewModelScope.launch {
                val analysis = if (wavFile != null && wavFile.exists()) {
                    voiceRecorder.analyzeRecordedAudio(wavFile)
                } else {
                    com.example.data.AudioAnalysisResult(
                        averagePitchHz = 165f,
                        averageRmsDb = -18f,
                        recommendedPitchMultiplier = 1.0f,
                        recommendedRateMultiplier = 0.95f,
                        audioWavPath = ""
                    )
                }

                val editionName = _uiState.value.selectedEdition.shortName
                _uiState.update { state ->
                    state.copy(
                        cloningStep = CloningStep.CALIBRATION_COMPLETE,
                        analyzedPitchHz = analysis.averagePitchHz,
                        draftPitch = analysis.recommendedPitchMultiplier,
                        draftSpeed = analysis.recommendedRateMultiplier,
                        draftCloneName = "My $editionName Voice Clone",
                        recordedAudioPath = analysis.audioWavPath
                    )
                }
            }
        }
    }

    fun stopRecordingVoice() {
        voiceRecorder.stopRecording()
    }

    fun updateDraftPitch(pitch: Float) {
        _uiState.update { it.copy(draftPitch = pitch) }
    }

    fun updateDraftSpeed(speed: Float) {
        _uiState.update { it.copy(draftSpeed = speed) }
    }

    fun updateDraftCloneName(name: String) {
        _uiState.update { it.copy(draftCloneName = name) }
    }

    fun updateDraftStyle(style: AccentStyle) {
        _uiState.update { it.copy(draftStyle = style) }
    }

    fun previewDraftClone(sampleText: String? = null) {
        val state = _uiState.value
        val textToSpeak = sampleText ?: state.previewText
        val tempProfile = VoiceProfile(
            name = state.draftCloneName,
            subtitle = "Custom voice preview",
            style = state.draftStyle,
            pitch = state.draftPitch,
            speechRate = state.draftSpeed,
            isCustomClone = true
        )
        ttsHelper.speak(textToSpeak, tempProfile)
    }

    fun saveCustomCloneProfile() {
        val state = _uiState.value
        val cloneName = state.draftCloneName.ifBlank { "My Voice Clone" }
        viewModelScope.launch {
            val savedProfile = voiceRepository.saveClonedVoice(
                name = cloneName,
                style = state.draftStyle,
                pitch = state.draftPitch,
                speechRate = state.draftSpeed,
                pitchVariance = 1.0f,
                tempAudioWavPath = state.recordedAudioPath,
                calibratedPitchHz = state.analyzedPitchHz
            )

            selectVoiceProfile(savedProfile)

            _uiState.update {
                it.copy(
                    showVoiceStudio = false,
                    cloningStep = CloningStep.INTRO
                )
            }
        }
    }

    fun renameCustomVoice(profileId: String, newName: String) {
        viewModelScope.launch {
            voiceRepository.renameVoiceProfile(profileId, newName)
            // If the renamed profile is currently active, update active voice model
            if (_uiState.value.activeVoiceProfile.id == profileId) {
                _uiState.update { state ->
                    state.copy(
                        activeVoiceProfile = state.activeVoiceProfile.copy(name = newName.trim())
                    )
                }
            }
        }
    }

    fun deleteCustomVoice(profileId: String) {
        viewModelScope.launch {
            voiceRepository.deleteVoiceProfile(profileId)

            if (_uiState.value.activeVoiceProfile.id == profileId) {
                selectVoiceProfile(TextToSpeechHelper.defaultVoiceProfile(_uiState.value.selectedEdition))
            }
        }
    }

    // --- App Navigation & Timeline ---

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun selectCategory(category: DayCategory) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
        observeTimelineFromCache(_uiState.value.selectedEdition, category)
    }

    fun openEntryDetail(entry: TimelineEntry) {
        _uiState.update { it.copy(selectedEntry = entry) }
    }

    fun dismissEntryDetail() {
        _uiState.update { it.copy(selectedEntry = null) }
    }

    fun toggleSavedNotes(show: Boolean) {
        _uiState.update { it.copy(showSavedNotes = show) }
    }

    fun toggleBookmark(id: String) {
        _uiState.update { state ->
            val updated = if (state.bookmarkedIds.contains(id)) {
                state.bookmarkedIds - id
            } else {
                state.bookmarkedIds + id
            }
            state.copy(bookmarkedIds = updated)
        }
    }

    fun toggleGalleryFavorite(id: String) {
        _uiState.update { state ->
            val updated = if (state.galleryFavoriteIds.contains(id)) {
                state.galleryFavoriteIds - id
            } else {
                state.galleryFavoriteIds + id
            }
            state.copy(galleryFavoriteIds = updated)
        }
    }

    fun practiceInChat(entry: TimelineEntry) {
        val prompt = if (entry.practicePrompt.isNotBlank()) {
            entry.practicePrompt
        } else {
            "Can we talk about the phrase '${entry.calloutTitle}' and how to use it naturally?"
        }
        dismissEntryDetail()
        selectTab(AppTab.TUTOR)
        sendMessage(prompt)
    }

    // --- Chat with Sam / Robin ---

    fun sendMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank() || _uiState.value.isTutorThinking) return

        val userMessage = ChatMessage(
            text = trimmed,
            isUser = true,
            timestamp = timeFormat.format(Date())
        )

        val edition = _uiState.value.selectedEdition
        val scenario = _uiState.value.selectedScenario

        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + userMessage,
                isTutorThinking = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val currentMessages = _uiState.value.chatMessages
            val historyPairs = currentMessages.takeLast(10).map { it.text to it.isUser }

            val result = GeminiApiClient.sendChatMessage(historyPairs, edition, scenario)

            result.onSuccess { tutorResponse ->
                val tutorMessage = ChatMessage(
                    text = tutorResponse.replyText,
                    isUser = false,
                    timestamp = timeFormat.format(Date()),
                    suggestedFollowUps = if (tutorResponse.suggestedFollowUps.isNotEmpty()) {
                        tutorResponse.suggestedFollowUps
                    } else {
                        scenario.getPromptsFor(edition)
                    },
                    languageTip = tutorResponse.languageTip,
                    idiomSpotlight = tutorResponse.idiomSpotlight
                )
                _uiState.update { state ->
                    state.copy(
                        chatMessages = state.chatMessages + tutorMessage,
                        isTutorThinking = false
                    )
                }
            }.onFailure { _ ->
                val fallbackReply = generateOfflineTutorReply(trimmed, edition, scenario)
                val tutorMessage = ChatMessage(
                    text = fallbackReply.replyText,
                    isUser = false,
                    timestamp = timeFormat.format(Date()),
                    isError = false,
                    suggestedFollowUps = fallbackReply.suggestedFollowUps,
                    languageTip = fallbackReply.languageTip,
                    idiomSpotlight = fallbackReply.idiomSpotlight
                )
                _uiState.update { state ->
                    state.copy(
                        chatMessages = state.chatMessages + tutorMessage,
                        isTutorThinking = false
                    )
                }
            }
        }
    }

    private fun generateOfflineTutorReply(prompt: String, edition: CountryEdition, scenario: PracticeScenario = PracticeScenario.CASUAL_CHAT): TutorResponse {
        val lower = prompt.lowercase(Locale.US)
        return if (edition == CountryEdition.CANADIAN) {
            when {
                lower.contains("tims") || lower.contains("tim hortons") || lower.contains("double-double") || lower.contains("timbits") ->
                    TutorResponse(
                        replyText = "At Timmies, ordering is fast and easy! Just say: 'Can I get a large double-double and a box of ten honey dip Timbits, please?' You'll often hear them say 'Have a good one!' at the window.",
                        languageTip = "In Canada, 'double-double' is an official dictionary word meaning two cream and two sugar in coffee.",
                        idiomSpotlight = "Timbits: Bite-sized bite doughnut holes unique to Tim Hortons.",
                        suggestedFollowUps = listOf("How do I order breakfast at Timmies?", "What are the most popular Timbit flavors?")
                    )
                lower.contains("eh") ->
                    TutorResponse(
                        replyText = "The Canadian 'eh' is all about seeking gentle agreement! For example: 'Chilly out there today, eh?' or 'That was a great movie, eh?' It keeps the conversation warm and inclusive.",
                        languageTip = "Never use 'eh' alone as a statement; place it at the end of a friendly observation to invite the other person in.",
                        idiomSpotlight = "Eh?: Informal tag question meaning 'Right?' or 'Don't you agree?'",
                        suggestedFollowUps = listOf("Give me 3 examples of using 'eh' naturally.", "How do people react if a tourist says 'eh'?")
                    )
                lower.contains("cottage") || lower.contains("cabin") || lower.contains("lake") ->
                    TutorResponse(
                        replyText = "Heading 'up north to the cottage' is the ultimate Canadian weekend ritual! You can say: 'We're packing up the car for the long weekend at the cottage—hoping for sunny weather and a campfire tonight!'",
                        languageTip = "In Ontario it's called 'the cottage', in Quebec 'le chalet', and in Western Canada often 'the cabin'.",
                        idiomSpotlight = "May Two-Four: The Victoria Day long weekend in late May marking cottage opening season.",
                        suggestedFollowUps = listOf("What do people pack for a cottage weekend?", "How do Canadians talk about campfires and s'mores?")
                    )
                lower.contains("hockey") || lower.contains("shinny") || lower.contains("rink") ->
                    TutorResponse(
                        replyText = "Hockey is a national passion here! You can ask someone: 'Did you catch the game last night?' or suggest 'Let's head down to the pond for a quick game of shinny after work!'",
                        languageTip = "'Shinny' is an informal game of pick-up hockey played outdoors with no referees or formal gear.",
                        idiomSpotlight = "Shinny: Casual outdoor pond or street hockey.",
                        suggestedFollowUps = listOf("How to talk about hockey scores politely?", "What does 'dropping the puck' mean?")
                    )
                else ->
                    TutorResponse(
                        replyText = "That's a classic Canadian topic! In everyday Canadian conversation, keeping things polite, warm, and modest is the key. Try using it in a sentence and we can practice together!",
                        languageTip = "Canadian English blends American pronunciation with British spelling (e.g., 'colour', 'neighbour', 'centre').",
                        idiomSpotlight = "Loonie & Toonie: Canadian 1-dollar and 2-dollar coins.",
                        suggestedFollowUps = scenario.getPromptsFor(edition)
                    )
            }
        } else {
            when {
                lower.contains("coffee") || lower.contains("drive-thru") ->
                    TutorResponse(
                        replyText = "At an American drive-thru, keep it quick: 'Hey, could I get a medium iced cold brew with oat milk and a toasted bagel to go?' You'll usually hear them ask: 'Anything else for you today?' before rolling up to the window!",
                        languageTip = "Americans often shorten requests to 'Could I get...' or 'Can I do a...' rather than 'I want...'",
                        idiomSpotlight = "To-go / Grab-and-go: Packaged for quick departure without dining in.",
                        suggestedFollowUps = listOf("How to customize sweetness and syrups at a coffee shop?", "What if they mess up my order at the drive-thru?")
                    )
                lower.contains("diner") || lower.contains("breakfast") ->
                    TutorResponse(
                        replyText = "When ordering eggs, waitstaff will ask: 'How do you want your eggs done?' You can say 'Over-easy with sourdough toast and crispy bacon, please!' And don't worry—bottomless drip coffee refills are customary.",
                        languageTip = "Egg styles in the US: 'scrambled', 'sunny-side up', 'over-easy' (runny yolk), 'over-medium', and 'over-hard'.",
                        idiomSpotlight = "Bottomless cup: Free continuous coffee refills included with your meal.",
                        suggestedFollowUps = listOf("How much should I tip at a casual diner?", "How do I ask for the check or a to-go box?")
                    )
                lower.contains("office") || lower.contains("touch base") || lower.contains("circle back") ->
                    TutorResponse(
                        replyText = "'Touch base' is super common in American offices! For example, you can say: 'Let's touch base for five minutes before tomorrow's team sync so we're on the same page.'",
                        languageTip = "Corporate American English frequently uses sports metaphors like 'touch base' (baseball) or 'kickoff' (football).",
                        idiomSpotlight = "Circle back: To return to an unresolved topic later.",
                        suggestedFollowUps = listOf("How to politely ask for an extension on a deadline?", "What is watercooler talk?")
                    )
                else ->
                    TutorResponse(
                        replyText = "That's a fantastic phrase to practice! In everyday American English, we love keeping things casual, warm, and direct. Try saying it in a sentence, and I'll help you refine the rhythm and tone!",
                        languageTip = "When greeting colleagues or cashiers, 'How's it going?' is a greeting, not an invitation for a lengthy medical report; reply 'Good, thanks! How about you?'",
                        idiomSpotlight = "Ballpark figure: A rough or approximate financial estimate.",
                        suggestedFollowUps = scenario.getPromptsFor(edition)
                    )
            }
        }
    }

    fun speakText(text: String, profile: VoiceProfile? = null, slowly: Boolean? = null) {
        val slow = slowly ?: _uiState.value.isSpeakingSlowly
        ttsHelper.speak(text, profile ?: _uiState.value.activeVoiceProfile, slow = slow)
    }

    fun speakText(text: String, slowly: Boolean) {
        ttsHelper.speak(text, _uiState.value.activeVoiceProfile, slow = slowly)
    }

    fun toggleSlowSpeech() {
        _uiState.update { it.copy(isSpeakingSlowly = !it.isSpeakingSlowly) }
    }

    fun stopSpeaking() {
        ttsHelper.stop()
    }

    fun resetConversation() {
        val edition = _uiState.value.selectedEdition
        _uiState.update { state ->
            state.copy(
                chatMessages = listOf(createInitialChatMessage(edition, state.selectedScenario))
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper.shutdown()
        voiceRecorder.stopRecording()
    }
}

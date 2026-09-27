package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.EditorialHeader
import com.example.ui.components.NavigationTabBar
import com.example.ui.components.PhotoGalleryView
import com.example.ui.components.SavedNotesDialog
import com.example.ui.components.TimelineDetailDialog
import com.example.ui.components.TimelineView
import com.example.ui.components.TutorChatView
import com.example.ui.components.VoiceCloneStudioDialog
import com.example.ui.theme.DailyAmericanTheme
import com.example.ui.theme.WarmPaperCream
import com.example.viewmodel.AppTab
import com.example.viewmodel.DailyAmericanViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DailyAmericanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyAmericanTheme {
                DailyAmericanApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DailyAmericanApp(
    viewModel: DailyAmericanViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { errorMsg ->
            snackbarHostState.showSnackbar(errorMsg)
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = WarmPaperCream
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmPaperCream)
        ) {
            // Editorial Field Guide Masthead with Voice Studio, Notes, & Country Edition Switcher
            EditorialHeader(
                edition = uiState.selectedEdition,
                savedCount = uiState.bookmarkedIds.size,
                activeVoiceName = uiState.activeVoiceProfile.name,
                isCustomVoice = uiState.activeVoiceProfile.isCustomClone,
                onSwitchEdition = { viewModel.switchCountryEdition(it) },
                onOpenSavedNotes = { viewModel.toggleSavedNotes(true) },
                onOpenVoiceStudio = { viewModel.toggleVoiceStudio(true) }
            )

            // Two-tab navigation bar
            NavigationTabBar(
                selectedTab = uiState.selectedTab,
                edition = uiState.selectedEdition,
                onTabSelected = { viewModel.selectTab(it) }
            )

            // Content Area based on active tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                when (uiState.selectedTab) {
                    AppTab.TIMELINE -> {
                        TimelineView(
                            entries = uiState.timelineEntries,
                            edition = uiState.selectedEdition,
                            selectedCategory = uiState.selectedCategory,
                            bookmarkedIds = uiState.bookmarkedIds,
                            isOfflineCached = uiState.isOfflineCached,
                            cachedCount = if (uiState.cachedTotalCount > 0) uiState.cachedTotalCount else uiState.timelineEntries.size,
                            isRefreshingCache = uiState.isRefreshingCache,
                            isSyncingWithRemote = uiState.isSyncingWithRemote,
                            lastSyncFormatted = uiState.lastSyncTimeFormatted,
                            onRefreshCache = { viewModel.refreshTimelineCache() },
                            onTriggerRemoteSync = { viewModel.triggerRemoteSync() },
                            onSelectCategory = { viewModel.selectCategory(it) },
                            onEntryClick = { viewModel.openEntryDetail(it) },
                            onPracticeInChat = { viewModel.practiceInChat(it) },
                            onSpeak = { viewModel.speakText(it) },
                            onToggleBookmark = { viewModel.toggleBookmark(it) }
                        )
                    }

                    AppTab.GALLERY -> {
                        PhotoGalleryView(
                            mediaItems = uiState.galleryItems,
                            favoriteIds = uiState.galleryFavoriteIds,
                            onToggleFavorite = { viewModel.toggleGalleryFavorite(it) },
                            onSpeakCaption = { viewModel.speakText(it) }
                        )
                    }

                    AppTab.TUTOR -> {
                        TutorChatView(
                            messages = uiState.chatMessages,
                            edition = uiState.selectedEdition,
                            selectedScenario = uiState.selectedScenario,
                            isTutorThinking = uiState.isTutorThinking,
                            activeVoiceProfile = uiState.activeVoiceProfile,
                            isSpeakingSlowly = uiState.isSpeakingSlowly,
                            onSendMessage = { viewModel.sendMessage(it) },
                            onSpeakText = { text, slowly -> viewModel.speakText(text, slowly) },
                            onToggleSlowSpeech = { viewModel.toggleSlowSpeech() },
                            onSelectScenario = { viewModel.selectPracticeScenario(it) },
                            onOpenVoiceStudio = { viewModel.toggleVoiceStudio(true) },
                            onResetConversation = { viewModel.resetConversation() }
                        )
                    }
                }
            }
        }

        // Modals & Sheets
        uiState.selectedEntry?.let { entry ->
            TimelineDetailDialog(
                entry = entry,
                onDismiss = { viewModel.dismissEntryDetail() },
                onPracticeInChat = { viewModel.practiceInChat(entry) },
                onSpeak = { viewModel.speakText(it) }
            )
        }

        if (uiState.showSavedNotes) {
            SavedNotesDialog(
                bookmarkedIds = uiState.bookmarkedIds,
                onDismiss = { viewModel.toggleSavedNotes(false) },
                onSelectEntry = { entry ->
                    viewModel.openEntryDetail(entry)
                },
                onRemoveBookmark = { id ->
                    viewModel.toggleBookmark(id)
                }
            )
        }

        VoiceCloneStudioDialog(
            show = uiState.showVoiceStudio,
            cloningStep = uiState.cloningStep,
            availableVoices = uiState.availableVoices,
            activeVoiceProfile = uiState.activeVoiceProfile,
            currentSentenceIndex = uiState.currentSentenceIndex,
            isRecordingVoice = uiState.isRecordingVoice,
            liveAmplitude = uiState.liveAmplitude,
            waveformSamples = uiState.waveformSamples,
            analyzedPitchHz = uiState.analyzedPitchHz,
            draftCloneName = uiState.draftCloneName,
            draftPitch = uiState.draftPitch,
            draftSpeed = uiState.draftSpeed,
            draftStyle = uiState.draftStyle,
            onDismiss = { viewModel.toggleVoiceStudio(false) },
            onSelectVoice = { viewModel.selectVoiceProfile(it) },
            onStartCloning = { viewModel.startCloningFlow() },
            onStartRecording = { viewModel.startRecordingCurrentSentence() },
            onStopAndNextRecording = { viewModel.stopAndProcessSentenceRecording() },
            onUpdateDraftName = { viewModel.updateDraftCloneName(it) },
            onUpdateDraftPitch = { viewModel.updateDraftPitch(it) },
            onUpdateDraftSpeed = { viewModel.updateDraftSpeed(it) },
            onUpdateDraftStyle = { viewModel.updateDraftStyle(it) },
            onPreviewDraft = { viewModel.previewDraftClone() },
            onSaveCustomClone = { viewModel.saveCustomCloneProfile() },
            onRenameCustomVoice = { id, newName -> viewModel.renameCustomVoice(id, newName) },
            onDeleteCustomVoice = { viewModel.deleteCustomVoice(it) },
            onTestVoicePhrase = { profile, phrase ->
                viewModel.speakText(phrase, profile)
            }
        )
    }
}

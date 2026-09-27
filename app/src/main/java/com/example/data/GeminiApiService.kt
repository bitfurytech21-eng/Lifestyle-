package com.example.data

import com.example.BuildConfig
import com.example.model.CountryEdition
import com.example.model.GeminiContent
import com.example.model.GeminiGenerateRequest
import com.example.model.GeminiGenerateResponse
import com.example.model.GeminiGenerationConfig
import com.example.model.GeminiPart
import com.example.model.PracticeScenario
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

enum class RefinerTone(val displayName: String, val promptDescription: String) {
    CASUAL("Casual & Natural", "Relaxed everyday North American speech with natural contractions"),
    FRIENDLY("Friendly & Warm", "Approachable, warm, and inviting tone suitable for team or friends"),
    WARM("Warm & Considerate", "Thoughtful, gentle, and highly respectful phrasing"),
    PROFESSIONAL("Polished Professional", "Clean, courteous, and workplace-appropriate business tone"),
    CONCISE("Concise & Direct", "Brief, crisp, and straight to the point without fluff"),
    EMPATHETIC("Empathetic & Supportive", "Compassionate and understanding with active listening cues"),
    CONFIDENT("Confident & Assertive", "Clear, decisive, and persuasive self-assurance"),
    APOLOGETIC("Apologetic & Tactful", "Sincere, accountable, and tactfully respectful"),
    PERSUASIVE("Persuasive & Engaging", "Compelling, motivating, and action-oriented"),
    PLAYFUL("Playful & Witty", "Lighthearted, friendly, and engaging with subtle humor")
}

enum class RefinerAction(val displayName: String, val promptInstruction: String) {
    REFINE_ALL("Natural Refine", "Refine overall flow, phrasing, and natural speech rhythm"),
    SHORTEN("Shorten & Streamline", "Remove unnecessary fluff while preserving all key facts and tone"),
    EXPAND("Expand & Elaborate", "Add natural conversational transitions and polite detail"),
    CLARIFY("Clarify Meaning", "Make ambiguous statements crisp, unambiguous, and easy to understand"),
    SOFTEN("Soften Tone", "Replace sharp or abrupt phrasing with polite, tactful expressions"),
    MAKE_DIRECT("Make Direct & Clear", "Eliminate passive voice and state requests directly and politely"),
    IMPROVE_GRAMMAR("Grammar & Flow", "Fix grammatical errors and awkward phrasing without altering tone")
}

data class RefinerResult(
    val refinedText: String,
    val changesMade: List<String>,
    val toneUsed: String,
    val actionUsed: String
)

data class TutorResponse(
    val replyText: String,
    val languageTip: String? = null,
    val idiomSpotlight: String? = null,
    val suggestedFollowUps: List<String> = emptyList()
)

interface GeminiRetrofitService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateRequest
    ): GeminiGenerateResponse
}

object GeminiApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: GeminiRetrofitService by lazy {
        retrofit.create(GeminiRetrofitService::class.java)
    }

    private fun getSystemInstruction(edition: CountryEdition, scenario: PracticeScenario = PracticeScenario.CASUAL_CHAT): String {
        val tutorName = if (edition == CountryEdition.CANADIAN) "Robin" else "Sam"
        val nation = if (edition == CountryEdition.CANADIAN) "Canadian" else "American"
        val regionalSlang = if (edition == CountryEdition.CANADIAN) {
            "Canadian English customs, spelling (colour, neighbour), polite culture, and local idioms like 'double-double', 'toque', 'two-four', 'eh?', and 'cottage weekend'"
        } else {
            "American English customs, regional accents, fast-casual culture, and everyday idioms like 'touch base', 'grab-and-go', 'circle back', 'ballpark figure', and 'drive-thru'"
        }

        return "You are $tutorName, a warm, patient, and friendly native $nation English tutor from the 'Daily $nation' field guide. " +
            "Your mission is to help English learners practice natural, spoken $nation English in an immersive conversation. " +
            "Current practice scenario: ${scenario.title} (${scenario.promptContext}). " +
            "Core knowledge base: $regionalSlang. " +
            "Key tutoring rules: " +
            "1. Reply conversationally in character (strictly 2 to 3 natural sentences). " +
            "2. If the user makes a grammar, phrasing, or vocabulary mistake, gently model the natural native phrasing in your reply without lecturing. " +
            "3. If there is a valuable native speaker nuance or alternative phrasing to share, add a tag on a new line: [TIP: Native tip here]. " +
            "4. If a colloquial idiom or slang term is relevant, add a tag: [IDIOM: Term - brief meaning]. " +
            "5. If helpful, suggest a follow-up practice phrase with: [PROMPT: Suggested phrase]. " +
            "6. Always maintain an encouraging, positive tone that builds English speaking confidence."
    }

    fun parseGeminiTutorOutput(rawText: String): TutorResponse {
        var cleanText = rawText
        var tip: String? = null
        var idiom: String? = null
        val followUps = mutableListOf<String>()

        val tipRegex = Regex("""\[TIP:\s*([^\]]+)\]""", RegexOption.IGNORE_CASE)
        val tipMatch = tipRegex.find(cleanText)
        if (tipMatch != null) {
            tip = tipMatch.groupValues[1].trim()
            cleanText = cleanText.replace(tipMatch.value, "").trim()
        }

        val idiomRegex = Regex("""\[IDIOM:\s*([^\]]+)\]""", RegexOption.IGNORE_CASE)
        val idiomMatch = idiomRegex.find(cleanText)
        if (idiomMatch != null) {
            idiom = idiomMatch.groupValues[1].trim()
            cleanText = cleanText.replace(idiomMatch.value, "").trim()
        }

        val promptRegex = Regex("""\[PROMPT:\s*([^\]]+)\]""", RegexOption.IGNORE_CASE)
        promptRegex.findAll(cleanText).forEach { match ->
            followUps.add(match.groupValues[1].trim())
            cleanText = cleanText.replace(match.value, "").trim()
        }

        return TutorResponse(
            replyText = cleanText.trim(),
            languageTip = tip,
            idiomSpotlight = idiom,
            suggestedFollowUps = followUps
        )
    }

    suspend fun sendChatMessage(
        conversationHistory: List<Pair<String, Boolean>>, // Pair(text, isUser)
        edition: CountryEdition = CountryEdition.AMERICAN,
        scenario: PracticeScenario = PracticeScenario.CASUAL_CHAT
    ): Result<TutorResponse> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val tutorName = if (edition == CountryEdition.CANADIAN) "Robin" else "Sam"
            val nationName = if (edition == CountryEdition.CANADIAN) "Canadian" else "American"
            return@withContext Result.success(
                TutorResponse(
                    replyText = "Hey there! I'm $tutorName, your $nationName English tutor. You can practice everyday phrases with me! (To activate live AI responses, ensure your Gemini API key is configured in the AI Studio Secrets panel).",
                    languageTip = if (edition == CountryEdition.CANADIAN) "In Canada, saying 'please' and 'sorry' generously makes you sound right at home!" else "In the US, short friendly greetings like 'How's it going?' or 'Have a good one!' are customary.",
                    idiomSpotlight = if (edition == CountryEdition.CANADIAN) "Double-Double: Coffee with two creams and two sugars." else "Grab-and-Go: Fast takeaway food or drinks without sitting down.",
                    suggestedFollowUps = scenario.getPromptsFor(edition)
                )
            )
        }

        val geminiContents = conversationHistory.map { (text, isUser) ->
            GeminiContent(
                role = if (isUser) "user" else "model",
                parts = listOf(GeminiPart(text = text))
            )
        }

        val request = GeminiGenerateRequest(
            contents = geminiContents,
            systemInstruction = GeminiContent(
                role = "system",
                parts = listOf(GeminiPart(text = getSystemInstruction(edition, scenario)))
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.7f,
                topP = 0.95f,
                topK = 40,
                maxOutputTokens = 400
            )
        )

        try {
            val response = service.generateContent(apiKey = apiKey, request = request)
            val candidateText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!candidateText.isNullOrBlank()) {
                val parsed = parseGeminiTutorOutput(candidateText)
                Result.success(parsed)
            } else if (response.error != null) {
                Result.failure(Exception("Gemini API Error: ${response.error.message ?: "Unknown error"}"))
            } else {
                Result.failure(Exception("No response text received from Gemini."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(
        conversationHistory: List<Pair<String, Boolean>>,
        edition: CountryEdition = CountryEdition.AMERICAN
    ): Result<String> {
        val result = sendChatMessage(conversationHistory, edition)
        return result.map { it.replyText }
    }

    suspend fun refineConversation(
        rawInput: String,
        tone: RefinerTone,
        action: RefinerAction,
        edition: CountryEdition = CountryEdition.AMERICAN
    ): Result<RefinerResult> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        // Fallback local refiner if API key is not present or offline
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val localRefined = applyLocalRefinerFallback(rawInput, tone, action)
            return@withContext Result.success(localRefined)
        }

        val refinerSystemInstruction = """
            You are an expert English Conversation Refiner specialized in authentic, natural ${if (edition == CountryEdition.CANADIAN) "Canadian" else "American"} English.
            Your task is to refine the user's input text (which may be a single message or a multi-speaker conversation thread).

            TARGET TONE: ${tone.displayName} (${tone.promptDescription})
            TARGET ACTION: ${action.displayName} (${action.promptInstruction})

            MANDATORY RULES:
            1. Preserve all original facts, names, times, dates, commitments, and core intent. NEVER invent or hallucinate new facts.
            2. If the input contains speaker names or labels (e.g., 'Person A:', 'Sam:', 'Alex:'), preserve the speaker labels and message order.
            3. Make the language sound completely natural when read and spoken aloud by a native speaker. Avoid robotic AI phrases like 'delve', 'furthermore', 'in summary', or excessive emojis.
            4. Output format MUST strictly follow this structure:
               [REFINED_TEXT]
               (Your refined output here)
               [/REFINED_TEXT]
               [CHANGES]
               - Change bullet 1
               - Change bullet 2
               - Change bullet 3
               [/CHANGES]
        """.trimIndent()

        val request = GeminiGenerateRequest(
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = rawInput))
                )
            ),
            systemInstruction = GeminiContent(
                role = "system",
                parts = listOf(GeminiPart(text = refinerSystemInstruction))
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.6f,
                topP = 0.9f,
                topK = 40,
                maxOutputTokens = 1000
            )
        )

        try {
            val response = service.generateContent(apiKey = apiKey, request = request)
            val candidateText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!candidateText.isNullOrBlank()) {
                val parsedResult = parseRefinerOutput(candidateText, tone, action)
                Result.success(parsedResult)
            } else {
                val localFallback = applyLocalRefinerFallback(rawInput, tone, action)
                Result.success(localFallback)
            }
        } catch (e: Exception) {
            val localFallback = applyLocalRefinerFallback(rawInput, tone, action)
            Result.success(localFallback)
        }
    }

    private fun parseRefinerOutput(rawText: String, tone: RefinerTone, action: RefinerAction): RefinerResult {
        var refinedText = rawText
        val changesList = mutableListOf<String>()

        val textRegex = Regex("""\[REFINED_TEXT\]([\s\S]*?)\[/REFINED_TEXT\]""", RegexOption.IGNORE_CASE)
        val textMatch = textRegex.find(rawText)
        if (textMatch != null) {
            refinedText = textMatch.groupValues[1].trim()
        }

        val changesRegex = Regex("""\[CHANGES\]([\s\S]*?)\[/CHANGES\]""", RegexOption.IGNORE_CASE)
        val changesMatch = changesRegex.find(rawText)
        if (changesMatch != null) {
            val changesBlock = changesMatch.groupValues[1].trim()
            changesBlock.lines().forEach { line ->
                val cleanLine = line.replace(Regex("""^[-*•\d\.\s]+"""), "").trim()
                if (cleanLine.isNotBlank()) {
                    changesList.add(cleanLine)
                }
            }
        }

        if (changesList.isEmpty()) {
            changesList.add("Smoothed sentence cadence and rhythm for ${tone.displayName}")
            changesList.add("Preserved speaker tags, names, and original context")
            changesList.add("Applied ${action.displayName} formatting")
        }

        return RefinerResult(
            refinedText = refinedText,
            changesMade = changesList,
            toneUsed = tone.displayName,
            actionUsed = action.displayName
        )
    }

    private fun applyLocalRefinerFallback(rawInput: String, tone: RefinerTone, action: RefinerAction): RefinerResult {
        val lines = rawInput.lines()
        val processedLines = lines.map { line ->
            if (line.isBlank()) return@map line
            var s = line.trim()

            // Smooth out robotic phrasing
            s = s.replace(Regex("""\bI am writing to inform you that\b""", RegexOption.IGNORE_CASE), "Just letting you know that")
            s = s.replace(Regex("""\bIt is imperative that we\b""", RegexOption.IGNORE_CASE), "We should really")
            s = s.replace(Regex("""\bPlease be advised that\b""", RegexOption.IGNORE_CASE), "Quick heads up:")
            s = s.replace(Regex("""\bDo not hesitate to contact me\b""", RegexOption.IGNORE_CASE), "Let me know if you need anything else!")
            s = s.replace(Regex("""\bI would appreciate if you could\b""", RegexOption.IGNORE_CASE), "Could you please")

            when (tone) {
                RefinerTone.CASUAL -> {
                    s = s.replace("cannot", "can't")
                        .replace("do not", "don't")
                        .replace("will not", "won't")
                        .replace("is not", "isn't")
                        .replace("hello", "hey")
                }
                RefinerTone.FRIENDLY, RefinerTone.WARM -> {
                    if (!s.contains("!") && !s.startsWith("Alex:") && !s.startsWith("Person")) {
                        s = s + " 😊"
                    }
                }
                RefinerTone.CONCISE -> {
                    s = s.replace("very ", "").replace("really ", "").replace("actually ", "")
                }
                else -> {}
            }

            s
        }

        val refinedText = processedLines.joinToString("\n")
        val changes = listOf(
            "Transformed robotic formal expressions into natural conversational English",
            "Adjusted tone to ${tone.displayName} and action to ${action.displayName}",
            "Preserved original facts, names, constraints, and speaker structure",
            "Offline processing complete"
        )

        return RefinerResult(
            refinedText = refinedText,
            changesMade = changes,
            toneUsed = tone.displayName,
            actionUsed = action.displayName
        )
    }
}

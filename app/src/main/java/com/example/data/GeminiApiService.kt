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
}

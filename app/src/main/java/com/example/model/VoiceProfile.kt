package com.example.model

import java.util.UUID

enum class AccentStyle(val displayName: String, val description: String) {
    STANDARD_AMERICAN("General American", "Clean, neutral, standard broadcast rhythm"),
    CANADIAN_STANDARD("General Canadian", "Canadian raising, gentle rhythm, authentic 'eh' phrasing"),
    CANADIAN_MARITIMES("Atlantic / Maritime", "Friendly East Coast cadence, melodic inflection"),
    MIDWESTERN("Midwestern Warmth", "Friendly, rounded vowels, approachable pace"),
    SOUTHERN("Southern Hospitality", "Warm, melodic drawl with relaxed cadence"),
    NEW_YORK("New York Fast-Pitch", "Brisk, direct, high-energy conversation style"),
    PACIFIC_COAST("West Coast Casual", "Laid-back, smooth, modern phrasing"),
    BROADCASTER("Golden Broadcaster", "Resonant baritone with crisp articulation"),
    CUSTOM_CLONE("My Custom Clone", "Trained on user voice sample calibration")
}

data class VoiceProfile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val subtitle: String,
    val style: AccentStyle,
    val pitch: Float = 1.0f,            // 0.5f to 2.0f
    val speechRate: Float = 1.0f,       // 0.5f to 2.0f
    val pitchVariance: Float = 1.0f,    // intonation richness
    val isCustomClone: Boolean = false,
    val sampleAudioPath: String? = null,
    val calibratedPitchHz: Float? = null,
    val dateCreated: String = ""
)

data class CalibrationSentence(
    val id: Int,
    val text: String,
    val phoneticFocus: String
)

val CALIBRATION_SENTENCES = listOf(
    CalibrationSentence(
        id = 1,
        text = "The quick brown fox jumps over the lazy dog in downtown Chicago and Toronto.",
        phoneticFocus = "Full phonetic spectrum & vowel transitions"
    ),
    CalibrationSentence(
        id = 2,
        text = "Can I get a double-double with a box of Timbits at the drive-thru window?",
        phoneticFocus = "Casual North American conversational cadence & rhythm"
    ),
    CalibrationSentence(
        id = 3,
        text = "Let's touch base after lunch to circle back on our weekend cottage trip, eh?",
        phoneticFocus = "Idiomatic intonation & natural conversational pitch"
    )
)

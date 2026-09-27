package com.example.data

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import com.example.model.AccentStyle
import com.example.model.CountryEdition
import com.example.model.VoiceProfile
import java.util.Locale

class TextToSpeechHelper(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private var currentProfile: VoiceProfile = defaultVoiceProfile()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w("TTS", "US English Language is not supported on this device.")
                } else {
                    isInitialized = true
                    applyProfile(currentProfile)
                }
            } else {
                Log.e("TTS", "TTS Initialization failed.")
            }
        }
    }

    fun setVoiceProfile(profile: VoiceProfile) {
        currentProfile = profile
        if (isInitialized) {
            applyProfile(profile)
        }
    }

    private fun applyProfile(profile: VoiceProfile) {
        tts?.setPitch(profile.pitch)
        tts?.setSpeechRate(profile.speechRate)

        try {
            if (profile.style == AccentStyle.CANADIAN_STANDARD || profile.style == AccentStyle.CANADIAN_MARITIMES) {
                tts?.language = Locale.CANADA
            } else {
                tts?.language = Locale.US
            }

            val availableVoices = tts?.voices?.filter { it.locale == Locale.US || it.locale == Locale.CANADA }
            if (!availableVoices.isNullOrEmpty()) {
                val matchedVoice = when (profile.style) {
                    AccentStyle.CANADIAN_STANDARD -> availableVoices.find { it.locale == Locale.CANADA || it.name.contains("ca", ignoreCase = true) }
                    AccentStyle.BROADCASTER -> availableVoices.find { it.name.contains("male", ignoreCase = true) || it.name.contains("en-us-x-sfg") }
                    AccentStyle.NEW_YORK -> availableVoices.find { it.name.contains("en-us-x-tpd") || it.name.contains("en-us-x-iol") }
                    AccentStyle.SOUTHERN, AccentStyle.MIDWESTERN -> availableVoices.find { it.name.contains("female", ignoreCase = true) || it.name.contains("en-us-x-sfg") }
                    else -> availableVoices.firstOrNull()
                }
                if (matchedVoice != null) {
                    tts?.voice = matchedVoice
                }
            }
        } catch (e: Exception) {
            Log.w("TTS", "Could not filter system voices", e)
        }
    }

    fun speak(text: String, profile: VoiceProfile? = null, slow: Boolean = false) {
        if (isInitialized && tts != null) {
            tts?.stop()
            val activeProfile = profile ?: currentProfile
            applyProfile(activeProfile)
            if (slow) {
                tts?.setSpeechRate((activeProfile.speechRate * 0.72f).coerceAtLeast(0.5f))
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "daily_guide_tts_${System.currentTimeMillis()}")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    companion object {
        fun defaultVoiceProfile(edition: CountryEdition = CountryEdition.AMERICAN): VoiceProfile {
            return if (edition == CountryEdition.CANADIAN) {
                VoiceProfile(
                    id = "preset_robin_canadian",
                    name = "Robin (Canadian Standard)",
                    subtitle = "Native Canadian field guide tutor • Friendly & polite",
                    style = AccentStyle.CANADIAN_STANDARD,
                    pitch = 1.02f,
                    speechRate = 0.94f,
                    isCustomClone = false
                )
            } else {
                VoiceProfile(
                    id = "preset_sam_default",
                    name = "Sam (Standard American)",
                    subtitle = "Native field guide tutor • Warm & natural",
                    style = AccentStyle.STANDARD_AMERICAN,
                    pitch = 1.0f,
                    speechRate = 0.95f,
                    isCustomClone = false
                )
            }
        }

        val defaultPresets: List<VoiceProfile> = listOf(
            defaultVoiceProfile(CountryEdition.AMERICAN),
            defaultVoiceProfile(CountryEdition.CANADIAN),
            VoiceProfile(
                id = "preset_canadian_maritimes",
                name = "Maritime East Coast",
                subtitle = "Friendly Atlantic cadence, upbeat melodic rhythm",
                style = AccentStyle.CANADIAN_MARITIMES,
                pitch = 1.06f,
                speechRate = 0.96f,
                isCustomClone = false
            ),
            VoiceProfile(
                id = "preset_midwestern",
                name = "Midwestern Neighbor",
                subtitle = "Approachable, rounded cadence, relaxed vowels",
                style = AccentStyle.MIDWESTERN,
                pitch = 1.05f,
                speechRate = 0.92f,
                isCustomClone = false
            ),
            VoiceProfile(
                id = "preset_southern",
                name = "Southern Drawl",
                subtitle = "Warm hospitality, relaxed pitch with melodic lilt",
                style = AccentStyle.SOUTHERN,
                pitch = 0.95f,
                speechRate = 0.88f,
                isCustomClone = false
            ),
            VoiceProfile(
                id = "preset_ny",
                name = "New York Fast-Pitch",
                subtitle = "Energetic, crisp consonants, swift urban tempo",
                style = AccentStyle.NEW_YORK,
                pitch = 1.12f,
                speechRate = 1.15f,
                isCustomClone = false
            ),
            VoiceProfile(
                id = "preset_broadcaster",
                name = "Golden Broadcaster",
                subtitle = "Deep resonant tone, authoritative enunciation",
                style = AccentStyle.BROADCASTER,
                pitch = 0.82f,
                speechRate = 0.94f,
                isCustomClone = false
            ),
            VoiceProfile(
                id = "preset_westcoast",
                name = "West Coast Casual",
                subtitle = "Modern, breezy, relaxed intonation",
                style = AccentStyle.PACIFIC_COAST,
                pitch = 1.02f,
                speechRate = 0.96f,
                isCustomClone = false
            )
        )
    }
}

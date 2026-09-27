package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AccentStyle
import com.example.model.VoiceProfile
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class VoiceProfileStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("daily_american_voices", Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, VoiceProfileDto::class.java)
    private val adapter = moshi.adapter<List<VoiceProfileDto>>(listType)

    data class VoiceProfileDto(
        val id: String,
        val name: String,
        val subtitle: String,
        val styleName: String,
        val pitch: Float,
        val speechRate: Float,
        val pitchVariance: Float,
        val isCustomClone: Boolean,
        val sampleAudioPath: String?,
        val calibratedPitchHz: Float?,
        val dateCreated: String
    )

    fun saveCustomProfiles(profiles: List<VoiceProfile>) {
        val dtos = profiles.filter { it.isCustomClone }.map {
            VoiceProfileDto(
                id = it.id,
                name = it.name,
                subtitle = it.subtitle,
                styleName = it.style.name,
                pitch = it.pitch,
                speechRate = it.speechRate,
                pitchVariance = it.pitchVariance,
                isCustomClone = it.isCustomClone,
                sampleAudioPath = it.sampleAudioPath,
                calibratedPitchHz = it.calibratedPitchHz,
                dateCreated = it.dateCreated
            )
        }
        val json = adapter.toJson(dtos)
        prefs.edit().putString("saved_custom_profiles", json).apply()
    }

    fun loadCustomProfiles(): List<VoiceProfile> {
        val json = prefs.getString("saved_custom_profiles", null) ?: return emptyList()
        return try {
            val dtos = adapter.fromJson(json) ?: emptyList()
            dtos.map { dto ->
                VoiceProfile(
                    id = dto.id,
                    name = dto.name,
                    subtitle = dto.subtitle,
                    style = try { AccentStyle.valueOf(dto.styleName) } catch (e: Exception) { AccentStyle.CUSTOM_CLONE },
                    pitch = dto.pitch,
                    speechRate = dto.speechRate,
                    pitchVariance = dto.pitchVariance,
                    isCustomClone = true,
                    sampleAudioPath = dto.sampleAudioPath,
                    calibratedPitchHz = dto.calibratedPitchHz,
                    dateCreated = dto.dateCreated
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveSelectedProfileId(id: String) {
        prefs.edit().putString("selected_profile_id", id).apply()
    }

    fun getSelectedProfileId(): String {
        return prefs.getString("selected_profile_id", "preset_sam_default") ?: "preset_sam_default"
    }

    fun saveSelectedEdition(edition: com.example.model.CountryEdition) {
        prefs.edit().putString("selected_country_edition", edition.name).apply()
    }

    fun getSelectedEdition(): com.example.model.CountryEdition {
        val saved = prefs.getString("selected_country_edition", com.example.model.CountryEdition.AMERICAN.name)
        return try {
            com.example.model.CountryEdition.valueOf(saved ?: com.example.model.CountryEdition.AMERICAN.name)
        } catch (e: Exception) {
            com.example.model.CountryEdition.AMERICAN
        }
    }
}

package com.example.data

import android.content.Context
import com.example.data.db.AppDatabase
import com.example.data.db.ClonedVoiceDao
import com.example.data.db.ClonedVoiceProfileEntity
import com.example.model.AccentStyle
import com.example.model.VoiceProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class VoiceProfileRepository(
    private val context: Context,
    private val dao: ClonedVoiceDao = AppDatabase.getInstance(context).clonedVoiceDao()
) {
    private val dateFormat = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.US)

    val allClonedProfiles: Flow<List<VoiceProfile>> = dao.getAllClonedVoices().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun saveClonedVoice(
        name: String,
        style: AccentStyle,
        pitch: Float,
        speechRate: Float,
        pitchVariance: Float = 1.0f,
        tempAudioWavPath: String? = null,
        calibratedPitchHz: Float? = null
    ): VoiceProfile = withContext(Dispatchers.IO) {
        val id = "custom_voice_${UUID.randomUUID().toString().take(8)}"
        val dateString = dateFormat.format(Date())

        // Persist audio WAV sample permanently into internal app storage
        val persistentWavPath = if (!tempAudioWavPath.isNullOrEmpty()) {
            val sourceFile = File(tempAudioWavPath)
            if (sourceFile.exists()) {
                val voiceDir = File(context.filesDir, "voice_samples").apply { if (!exists()) mkdirs() }
                val destFile = File(voiceDir, "$id.wav")
                try {
                    sourceFile.copyTo(destFile, overwrite = true)
                    destFile.absolutePath
                } catch (e: Exception) {
                    tempAudioWavPath
                }
            } else {
                tempAudioWavPath
            }
        } else null

        val subtitle = buildString {
            append("Custom • ${style.displayName}")
            if (calibratedPitchHz != null && calibratedPitchHz > 0) {
                append(" (${calibratedPitchHz.toInt()} Hz)")
            }
        }

        val entity = ClonedVoiceProfileEntity(
            id = id,
            name = name.ifBlank { "My Voice Clone" },
            subtitle = subtitle,
            styleName = style.name,
            pitch = pitch,
            speechRate = speechRate,
            pitchVariance = pitchVariance,
            audioSampleWavPath = persistentWavPath,
            calibratedPitchHz = calibratedPitchHz,
            dateCreated = dateString,
            createdAtTimestamp = System.currentTimeMillis()
        )

        dao.insertClonedVoice(entity)
        entity.toDomainModel()
    }

    suspend fun renameVoiceProfile(id: String, newName: String) = withContext(Dispatchers.IO) {
        if (newName.isNotBlank()) {
            dao.updateVoiceName(id, newName.trim())
        }
    }

    suspend fun deleteVoiceProfile(id: String) = withContext(Dispatchers.IO) {
        val entity = dao.getClonedVoiceById(id)
        if (entity?.audioSampleWavPath != null) {
            val file = File(entity.audioSampleWavPath)
            if (file.exists()) {
                file.delete()
            }
        }
        dao.deleteClonedVoiceById(id)
    }

    suspend fun getVoiceProfileById(id: String): VoiceProfile? = withContext(Dispatchers.IO) {
        dao.getClonedVoiceById(id)?.toDomainModel()
    }

    private fun ClonedVoiceProfileEntity.toDomainModel(): VoiceProfile {
        val accent = try {
            AccentStyle.valueOf(styleName)
        } catch (e: Exception) {
            AccentStyle.CUSTOM_CLONE
        }
        return VoiceProfile(
            id = id,
            name = name,
            subtitle = subtitle,
            style = accent,
            pitch = pitch,
            speechRate = speechRate,
            pitchVariance = pitchVariance,
            isCustomClone = true,
            sampleAudioPath = audioSampleWavPath,
            calibratedPitchHz = calibratedPitchHz,
            dateCreated = dateCreated
        )
    }
}

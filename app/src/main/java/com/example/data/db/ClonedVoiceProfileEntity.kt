package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cloned_voice_profiles")
data class ClonedVoiceProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val subtitle: String,
    val styleName: String,
    val pitch: Float,
    val speechRate: Float,
    val pitchVariance: Float = 1.0f,
    val audioSampleWavPath: String? = null,
    val calibratedPitchHz: Float? = null,
    val dateCreated: String,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)

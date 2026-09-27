package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClonedVoiceDao {

    @Query("SELECT * FROM cloned_voice_profiles ORDER BY createdAtTimestamp DESC")
    fun getAllClonedVoices(): Flow<List<ClonedVoiceProfileEntity>>

    @Query("SELECT * FROM cloned_voice_profiles WHERE id = :id LIMIT 1")
    suspend fun getClonedVoiceById(id: String): ClonedVoiceProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClonedVoice(voice: ClonedVoiceProfileEntity)

    @Update
    suspend fun updateClonedVoice(voice: ClonedVoiceProfileEntity)

    @Query("UPDATE cloned_voice_profiles SET name = :newName WHERE id = :id")
    suspend fun updateVoiceName(id: String, newName: String)

    @Query("DELETE FROM cloned_voice_profiles WHERE id = :id")
    suspend fun deleteClonedVoiceById(id: String)

    @Query("DELETE FROM cloned_voice_profiles")
    suspend fun deleteAll()
}

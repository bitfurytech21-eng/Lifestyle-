package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for interacting with cached timeline entries stored in Room SQLite.
 */
@Dao
interface TimelineDao {

    @Query("SELECT * FROM timeline_entries WHERE edition = :edition AND category = :category ORDER BY sortOrder ASC")
    fun getEntriesByEditionAndCategory(edition: String, category: String): Flow<List<TimelineEntryEntity>>

    @Query("SELECT * FROM timeline_entries WHERE edition = :edition ORDER BY sortOrder ASC")
    fun getEntriesByEdition(edition: String): Flow<List<TimelineEntryEntity>>

    @Query("SELECT * FROM timeline_entries ORDER BY sortOrder ASC")
    fun getAllEntries(): Flow<List<TimelineEntryEntity>>

    @Query("SELECT * FROM timeline_entries WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: String): TimelineEntryEntity?

    @Query("SELECT COUNT(*) FROM timeline_entries")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM timeline_entries WHERE edition = :edition")
    suspend fun getCountByEdition(edition: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<TimelineEntryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: TimelineEntryEntity)

    @Query("DELETE FROM timeline_entries WHERE id = :id")
    suspend fun deleteEntryById(id: String)

    @Query("DELETE FROM timeline_entries WHERE edition = :edition")
    suspend fun clearByEdition(edition: String)

    @Query("DELETE FROM timeline_entries")
    suspend fun clearAll()
}

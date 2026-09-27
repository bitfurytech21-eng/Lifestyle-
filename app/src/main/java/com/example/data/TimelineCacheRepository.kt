package com.example.data

import android.content.Context
import com.example.data.db.AppDatabase
import com.example.data.db.TimelineDao
import com.example.data.db.TimelineEntryEntity
import com.example.model.CountryEdition
import com.example.model.DayCategory
import com.example.model.TimelineEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Repository providing a persistent Room database caching layer for daily timeline data.
 * Guarantees that users can browse "A Day in America" routines, idioms, and cultural customs
 * offline without requiring an active network connection.
 */
class TimelineCacheRepository(
    private val context: Context,
    private val dao: TimelineDao = AppDatabase.getInstance(context).timelineDao()
) {

    /**
     * Observe cached timeline entries for a specific country edition and category.
     * Emits reactively whenever the Room database is updated or refreshed.
     */
    fun observeEntries(edition: CountryEdition, category: DayCategory): Flow<List<TimelineEntry>> {
        return dao.getEntriesByEditionAndCategory(edition.name, category.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Observe all cached timeline entries for a given country edition.
     */
    fun observeEntriesByEdition(edition: CountryEdition): Flow<List<TimelineEntry>> {
        return dao.getEntriesByEdition(edition.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Observe all entries currently stored in the Room database cache.
     */
    fun observeAllEntries(): Flow<List<TimelineEntry>> {
        return dao.getAllEntries().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Ensures that the local Room cache is populated with the complete daily timeline data.
     * If the database table is empty or [forceRefresh] is true, seeds all routines into SQLite.
     */
    suspend fun ensureCacheSeeded(forceRefresh: Boolean = false): Int = withContext(Dispatchers.IO) {
        val currentCount = dao.getCount()
        if (currentCount == 0 || forceRefresh) {
            val entities = TimelineRepository.allEntries.mapIndexed { index, entry ->
                TimelineEntryEntity.fromDomainModel(entry, sortOrder = index)
            }
            dao.insertEntries(entities)
            return@withContext entities.size
        }
        return@withContext currentCount
    }

    /**
     * Force refresh the Room cache from the authoritative dataset.
     */
    suspend fun refreshCache(): Int = withContext(Dispatchers.IO) {
        ensureCacheSeeded(forceRefresh = true)
    }

    /**
     * Retrieve total number of cached entries across all categories.
     */
    suspend fun getCachedCount(): Int = withContext(Dispatchers.IO) {
        dao.getCount()
    }

    /**
     * Retrieve number of cached entries for a specific edition.
     */
    suspend fun getCachedCountByEdition(edition: CountryEdition): Int = withContext(Dispatchers.IO) {
        dao.getCountByEdition(edition.name)
    }

    /**
     * Synchronizes and persists entries fetched from the remote source into the Room cache.
     * Updates or inserts records for both American and Canadian editions.
     * Returns the total count of synchronized entries stored in SQLite.
     */
    suspend fun syncWithRemoteEntries(remoteEntries: List<TimelineEntry>): Int = withContext(Dispatchers.IO) {
        val entities = remoteEntries.mapIndexed { index, entry ->
            TimelineEntryEntity.fromDomainModel(entry, sortOrder = index)
        }
        dao.insertEntries(entities)
        return@withContext dao.getCount()
    }

    /**
     * Retrieve a specific cached timeline entry by its ID.
     */
    suspend fun getEntryById(id: String): TimelineEntry? = withContext(Dispatchers.IO) {
        dao.getEntryById(id)?.toDomainModel()
    }
}

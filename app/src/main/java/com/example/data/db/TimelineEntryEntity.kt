package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.CalloutType
import com.example.model.CountryEdition
import com.example.model.DayCategory
import com.example.model.TimelineEntry

/**
 * Room Database entity representing a daily timeline entry cached locally in SQLite.
 * This enables complete offline browsing of "A Day in America" routines, idioms, and cultural notes.
 */
@Entity(tableName = "timeline_entries")
data class TimelineEntryEntity(
    @PrimaryKey
    val id: String,
    val edition: String, // "AMERICAN" or "CANADIAN"
    val time: String,
    val category: String, // "WEEKDAY", "WEEKEND", "HOLIDAYS"
    val title: String,
    val description: String,
    val calloutType: String, // "SAY_IT_LIKE_A_LOCAL" or "CULTURE_NOTE"
    val calloutTitle: String,
    val calloutBody: String,
    val idiomSample: String?,
    val culturalContextExtra: String,
    val practicePrompt: String,
    val sortOrder: Int = 0,
    val cachedAt: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): TimelineEntry {
        val parsedEdition = runCatching { CountryEdition.valueOf(edition) }.getOrDefault(CountryEdition.AMERICAN)
        val parsedCategory = runCatching { DayCategory.valueOf(category) }.getOrDefault(DayCategory.WEEKDAY)
        val parsedCalloutType = runCatching { CalloutType.valueOf(calloutType) }.getOrDefault(CalloutType.SAY_IT_LIKE_A_LOCAL)

        return TimelineEntry(
            id = id,
            time = time,
            category = parsedCategory,
            title = title,
            description = description,
            calloutType = parsedCalloutType,
            calloutTitle = calloutTitle,
            calloutBody = calloutBody,
            idiomSample = idiomSample,
            culturalContextExtra = culturalContextExtra,
            practicePrompt = practicePrompt,
            edition = parsedEdition
        )
    }

    companion object {
        fun fromDomainModel(entry: TimelineEntry, sortOrder: Int = 0): TimelineEntryEntity {
            return TimelineEntryEntity(
                id = entry.id,
                edition = entry.edition.name,
                time = entry.time,
                category = entry.category.name,
                title = entry.title,
                description = entry.description,
                calloutType = entry.calloutType.name,
                calloutTitle = entry.calloutTitle,
                calloutBody = entry.calloutBody,
                idiomSample = entry.idiomSample,
                culturalContextExtra = entry.culturalContextExtra,
                practicePrompt = entry.practicePrompt,
                sortOrder = sortOrder,
                cachedAt = System.currentTimeMillis()
            )
        }
    }
}

package com.example.data.sync

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SyncMetadata(
    val lastSyncTimestamp: Long = 0L,
    val lastStatus: String = "IDLE", // IDLE, SYNCING, SUCCESS, FAILED
    val lastSyncedCount: Int = 0,
    val lastSyncSummary: String = "Periodic WorkManager sync configured"
) {
    val formattedLastSyncTime: String
        get() {
            if (lastSyncTimestamp == 0L) return "Never synced"
            val format = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            return format.format(Date(lastSyncTimestamp))
        }
}

/**
 * Storage helper for tracking background WorkManager timeline synchronization metadata.
 */
class TimelineSyncPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _syncMetadataFlow = MutableStateFlow(loadMetadata())
    val syncMetadataFlow: StateFlow<SyncMetadata> = _syncMetadataFlow.asStateFlow()

    fun loadMetadata(): SyncMetadata {
        val timestamp = prefs.getLong(KEY_LAST_SYNC_TIMESTAMP, 0L)
        val status = prefs.getString(KEY_LAST_STATUS, "IDLE") ?: "IDLE"
        val count = prefs.getInt(KEY_LAST_SYNCED_COUNT, 0)
        val summary = prefs.getString(KEY_LAST_SUMMARY, "Periodic WorkManager sync scheduled")
            ?: "Periodic WorkManager sync scheduled"

        return SyncMetadata(
            lastSyncTimestamp = timestamp,
            lastStatus = status,
            lastSyncedCount = count,
            lastSyncSummary = summary
        )
    }

    fun recordSyncStart() {
        val current = loadMetadata()
        val updated = current.copy(lastStatus = "SYNCING")
        prefs.edit().putString(KEY_LAST_STATUS, "SYNCING").apply()
        _syncMetadataFlow.value = updated
    }

    fun recordSyncSuccess(count: Int, summary: String = "Synced $count timeline routines successfully") {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putLong(KEY_LAST_SYNC_TIMESTAMP, now)
            .putString(KEY_LAST_STATUS, "SUCCESS")
            .putInt(KEY_LAST_SYNCED_COUNT, count)
            .putString(KEY_LAST_SUMMARY, summary)
            .apply()

        _syncMetadataFlow.value = SyncMetadata(
            lastSyncTimestamp = now,
            lastStatus = "SUCCESS",
            lastSyncedCount = count,
            lastSyncSummary = summary
        )
    }

    fun recordSyncFailure(errorMessage: String) {
        val current = loadMetadata()
        val updated = current.copy(
            lastStatus = "FAILED",
            lastSyncSummary = errorMessage
        )
        prefs.edit()
            .putString(KEY_LAST_STATUS, "FAILED")
            .putString(KEY_LAST_SUMMARY, errorMessage)
            .apply()

        _syncMetadataFlow.value = updated
    }

    companion object {
        private const val PREFS_NAME = "timeline_sync_preferences"
        private const val KEY_LAST_SYNC_TIMESTAMP = "key_last_sync_timestamp"
        private const val KEY_LAST_STATUS = "key_last_status"
        private const val KEY_LAST_SYNCED_COUNT = "key_last_synced_count"
        private const val KEY_LAST_SUMMARY = "key_last_summary"
    }
}

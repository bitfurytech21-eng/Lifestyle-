package com.example.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.data.TimelineCacheRepository
import com.example.data.remote.RemoteTimelineDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * WorkManager CoroutineWorker for periodically synchronizing both 'A Day in America'
 * and 'Canadian' timeline routines with the remote source when the device has an active
 * internet connection.
 *
 * Persists all retrieved entries into the local Room database to maintain an up-to-date
 * offline cache.
 */
class TimelineSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val remoteDataSource = RemoteTimelineDataSource(appContext)
    private val cacheRepository = TimelineCacheRepository(appContext)
    private val syncPreferences = TimelineSyncPreferences(appContext)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d(TAG, "Starting background timeline synchronization job (attempt: $runAttemptCount)")

        syncPreferences.recordSyncStart()

        try {
            val remoteResult = remoteDataSource.fetchRemoteTimelineData()

            if (remoteResult.isSuccess) {
                val payload = remoteResult.getOrThrow()
                val totalSynced = cacheRepository.syncWithRemoteEntries(payload.allEntries)

                val summary = "Synced ${payload.americanEntries.size} US & ${payload.canadianEntries.size} Canadian routines from remote"
                syncPreferences.recordSyncSuccess(totalSynced, summary)

                Log.d(TAG, "Timeline background sync completed successfully: $summary (Total in DB: $totalSynced)")

                val outputData = workDataOf(
                    KEY_OUTPUT_SYNCED_COUNT to totalSynced,
                    KEY_OUTPUT_TIMESTAMP to payload.syncTimestamp,
                    KEY_OUTPUT_SUMMARY to summary
                )

                Result.success(outputData)
            } else {
                val error = remoteResult.exceptionOrNull()?.localizedMessage ?: "Unknown sync error"
                Log.w(TAG, "Timeline remote sync failed: $error")
                syncPreferences.recordSyncFailure(error)

                if (runAttemptCount < MAX_RETRY_ATTEMPTS) {
                    Result.retry()
                } else {
                    Result.failure(workDataOf(KEY_OUTPUT_ERROR to error))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during background timeline sync", e)
            syncPreferences.recordSyncFailure(e.localizedMessage ?: "Exception during sync")

            if (runAttemptCount < MAX_RETRY_ATTEMPTS) {
                Result.retry()
            } else {
                Result.failure(workDataOf(KEY_OUTPUT_ERROR to (e.localizedMessage ?: "Exception")))
            }
        }
    }

    companion object {
        const val TAG = "TimelineSyncWorker"
        const val WORK_TAG = "timeline_sync_work"
        const val KEY_OUTPUT_SYNCED_COUNT = "synced_count"
        const val KEY_OUTPUT_TIMESTAMP = "timestamp"
        const val KEY_OUTPUT_SUMMARY = "summary"
        const val KEY_OUTPUT_ERROR = "error"
        private const val MAX_RETRY_ATTEMPTS = 3
    }
}

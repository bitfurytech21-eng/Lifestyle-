package com.example.data.sync

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

/**
 * Orchestrates WorkManager configuration and scheduling for periodic and on-demand
 * background syncing of 'A Day in America' and 'Canadian' timeline routines with the remote source.
 */
object TimelineSyncScheduler {

    private const val TAG = "TimelineSyncScheduler"

    const val PERIODIC_WORK_NAME = "periodic_timeline_sync_work"
    const val IMMEDIATE_WORK_NAME = "immediate_timeline_sync_work"
    const val SYNC_TAG = "timeline_sync_work"

    // Periodic synchronization interval (e.g. 6 hours; flex interval 30 minutes)
    const val SYNC_INTERVAL_HOURS = 6L
    const val SYNC_FLEX_MINUTES = 30L

    /**
     * Build constraints requiring an active network connection (device is online).
     */
    fun createOnlineConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    /**
     * Enqueues unique periodic background work to periodically sync timeline data
     * whenever the device is connected to the internet.
     */
    fun schedulePeriodicSync(context: Context) {
        try {
            val constraints = createOnlineConstraints()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<TimelineSyncWorker>(
                SYNC_INTERVAL_HOURS, TimeUnit.HOURS,
                SYNC_FLEX_MINUTES, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(SYNC_TAG)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    15,
                    TimeUnit.MINUTES
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )

            Log.d(TAG, "Scheduled periodic WorkManager timeline sync (every $SYNC_INTERVAL_HOURS hrs when online)")
        } catch (e: Throwable) {
            Log.w(TAG, "Failed to schedule periodic WorkManager sync: ${e.message}")
        }
    }

    /**
     * Triggers an immediate one-time background sync when online.
     * Useful when user manually initiates a sync or wants immediate remote refresh.
     */
    fun triggerImmediateSync(context: Context) {
        try {
            val constraints = createOnlineConstraints()

            val immediateWorkRequest = OneTimeWorkRequestBuilder<TimelineSyncWorker>()
                .setConstraints(constraints)
                .addTag(SYNC_TAG)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10,
                    TimeUnit.SECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                IMMEDIATE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                immediateWorkRequest
            )

            Log.d(TAG, "Triggered immediate online timeline sync via WorkManager")
        } catch (e: Throwable) {
            Log.w(TAG, "Failed to trigger immediate WorkManager sync: ${e.message}")
        }
    }

    /**
     * Observes the WorkInfo status of sync workers via Flow.
     */
    fun observeSyncWorkInfo(context: Context): Flow<List<WorkInfo>> {
        return try {
            WorkManager.getInstance(context).getWorkInfosByTagFlow(SYNC_TAG)
        } catch (e: Throwable) {
            Log.w(TAG, "Failed to observe WorkInfo, returning empty flow: ${e.message}")
            kotlinx.coroutines.flow.emptyFlow()
        }
    }

    /**
     * Cancel scheduled sync if ever needed.
     */
    fun cancelSync(context: Context) {
        try {
            WorkManager.getInstance(context).cancelUniqueWork(PERIODIC_WORK_NAME)
            WorkManager.getInstance(context).cancelUniqueWork(IMMEDIATE_WORK_NAME)
        } catch (e: Throwable) {
            Log.w(TAG, "Failed to cancel sync: ${e.message}")
        }
    }
}

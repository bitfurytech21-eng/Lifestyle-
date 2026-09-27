package com.example

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.data.sync.TimelineSyncScheduler

class DailyAmericanApplication : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        Log.d("DailyAmericanApp", "Application initialized - setting up background WorkManager sync")
        try {
            // Schedule periodic background timeline synchronization with remote source when device is online
            TimelineSyncScheduler.schedulePeriodicSync(this)
        } catch (e: Throwable) {
            Log.w("DailyAmericanApp", "WorkManager initialization or scheduling skipped in non-standard environment", e)
        }
    }
}

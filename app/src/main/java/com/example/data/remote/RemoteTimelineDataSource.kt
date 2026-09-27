package com.example.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.data.TimelineRepository
import com.example.model.CountryEdition
import com.example.model.TimelineEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Remote data payload returned by the remote source during periodic synchronization.
 */
data class RemoteTimelinePayload(
    val americanEntries: List<TimelineEntry>,
    val canadianEntries: List<TimelineEntry>,
    val syncTimestamp: Long = System.currentTimeMillis(),
    val sourceProvider: String = "Daily American Editorial Cloud Service",
    val serverVersion: String = "2.4.0"
) {
    val allEntries: List<TimelineEntry>
        get() = americanEntries + canadianEntries
}

/**
 * Remote Data Source responsible for querying the remote endpoint/server to fetch updated
 * 'A Day in America' and 'Canadian' daily timeline routines, cultural callouts, and idioms.
 */
class RemoteTimelineDataSource(private val context: Context) {

    /**
     * Checks if the device has an active internet connection.
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    /**
     * Simulates fetching the latest timeline data payload from the remote source.
     * In a production environment with a live REST/GraphQL backend, this would execute
     * a Retrofit/Ktor request to a remote server.
     */
    suspend fun fetchRemoteTimelineData(): Result<RemoteTimelinePayload> = withContext(Dispatchers.IO) {
        try {
            // Simulate brief network latency for realistic remote sync behavior
            delay(150)

            val americanEntries = TimelineRepository.allEntries.filter { it.edition == CountryEdition.AMERICAN }
            val canadianEntries = TimelineRepository.allEntries.filter { it.edition == CountryEdition.CANADIAN }

            val payload = RemoteTimelinePayload(
                americanEntries = americanEntries,
                canadianEntries = canadianEntries,
                syncTimestamp = System.currentTimeMillis(),
                sourceProvider = "Daily American & Canadian Cloud Archive",
                serverVersion = "2.4.0"
            )

            Result.success(payload)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

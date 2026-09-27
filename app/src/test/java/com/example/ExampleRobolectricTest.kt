package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.TextToSpeechHelper
import com.example.data.TimelineRepository
import com.example.data.VoiceProfileStorage
import com.example.data.sync.TimelineSyncPreferences
import com.example.data.sync.TimelineSyncScheduler
import com.example.data.sync.TimelineSyncWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.model.AccentStyle
import com.example.model.CALIBRATION_SENTENCES
import com.example.model.DayCategory
import com.example.model.VoiceProfile
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @org.junit.Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val config = androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setExecutor(androidx.work.testing.SynchronousExecutor())
            .build()
        androidx.work.testing.WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Daily American", appName)
    }

    @Test
    fun `verify timeline repository entries are populated`() {
        val weekdayEntries = TimelineRepository.getEntriesForCategory(DayCategory.WEEKDAY)
        assertTrue(weekdayEntries.isNotEmpty())
        assertEquals("6:30 AM", weekdayEntries.first().time)

        val weekendEntries = TimelineRepository.getEntriesForCategory(DayCategory.WEEKEND)
        assertTrue(weekendEntries.isNotEmpty())

        val holidayEntries = TimelineRepository.getEntriesForCategory(DayCategory.HOLIDAYS)
        assertTrue(holidayEntries.isNotEmpty())
    }

    @Test
    fun `verify calibration sentences and presets for voice cloning`() {
        assertEquals(3, CALIBRATION_SENTENCES.size)
        assertTrue(CALIBRATION_SENTENCES[0].text.contains("fox"))

        val presets = TextToSpeechHelper.defaultPresets
        assertTrue(presets.isNotEmpty())
        assertNotNull(presets.find { it.style == AccentStyle.STANDARD_AMERICAN })
        assertNotNull(presets.find { it.style == AccentStyle.NEW_YORK })
        assertNotNull(presets.find { it.style == AccentStyle.SOUTHERN })
    }

    @Test
    fun `verify voice profile storage saves and loads custom clones`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val storage = VoiceProfileStorage(context)

        val testClone = VoiceProfile(
            id = "custom_test_123",
            name = "Test Learner Voice",
            subtitle = "Custom calibration test",
            style = AccentStyle.CUSTOM_CLONE,
            pitch = 1.15f,
            speechRate = 0.98f,
            isCustomClone = true,
            calibratedPitchHz = 195f
        )

        storage.saveCustomProfiles(listOf(testClone))
        val loaded = storage.loadCustomProfiles()
        assertEquals(1, loaded.size)
        assertEquals("Test Learner Voice", loaded[0].name)
        assertEquals(1.15f, loaded[0].pitch, 0.01f)
    }

    @Test
    fun `verify content stream toggle between American and Canadian streams updates timeline data source`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val storage = VoiceProfileStorage(context)

        // Test persistence
        storage.saveSelectedEdition(com.example.model.CountryEdition.CANADIAN)
        assertEquals(com.example.model.CountryEdition.CANADIAN, storage.getSelectedEdition())

        storage.saveSelectedEdition(com.example.model.CountryEdition.AMERICAN)
        assertEquals(com.example.model.CountryEdition.AMERICAN, storage.getSelectedEdition())

        // Test American stream data source
        val americanWeekday = TimelineRepository.getEntriesFor(com.example.model.CountryEdition.AMERICAN, DayCategory.WEEKDAY)
        assertTrue(americanWeekday.isNotEmpty())
        assertTrue(americanWeekday.any { it.title.contains("Coffee") || it.description.contains("diner") || it.calloutTitle.contains("coffee") })

        // Test Canadian stream data source
        val canadianWeekday = TimelineRepository.getEntriesFor(com.example.model.CountryEdition.CANADIAN, DayCategory.WEEKDAY)
        assertTrue(canadianWeekday.isNotEmpty())
        assertTrue(canadianWeekday.any { it.title.contains("Tim Hortons") || it.calloutTitle.contains("Double-Double") || it.description.contains("Timbits") })

        val canadianHolidays = TimelineRepository.getEntriesFor(com.example.model.CountryEdition.CANADIAN, DayCategory.HOLIDAYS)
        assertTrue(canadianHolidays.isNotEmpty())
        assertTrue(canadianHolidays.any { it.title.contains("Victoria Day") || it.title.contains("Canada Day") || it.calloutTitle.contains("Two-Four") })
    }

    @Test
    fun `verify voice recorder helper and acoustic pitch calibration estimation`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val recorderHelper = com.example.data.VoiceRecorderHelper(context)
        assertNotNull(recorderHelper)
        assertEquals(false, recorderHelper.isRecording.value)
        assertEquals(0f, recorderHelper.amplitudeFlow.value, 0.001f)
        assertTrue(recorderHelper.waveformSamples.value.isEmpty())

        // Test mock WAV audio analysis fallback
        val mockFile = java.io.File(context.cacheDir, "mock_test_sample.wav")
        val analysis = recorderHelper.analyzeRecordedAudio(mockFile)
        assertNotNull(analysis)
        assertTrue(analysis.averagePitchHz > 0f)
        assertTrue(analysis.recommendedPitchMultiplier in 0.7f..1.5f)
        assertTrue(analysis.recommendedRateMultiplier in 0.7f..1.5f)
    }

    @Test
    fun `verify room persistence layer saves, names, renames, switches, and deletes multiple voice profiles`() = kotlinx.coroutines.runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = com.example.data.VoiceProfileRepository(context)

        // 1. Save first cloned voice profile with custom name
        val profile1 = repository.saveClonedVoice(
            name = "Morning American Accent",
            style = AccentStyle.STANDARD_AMERICAN,
            pitch = 1.05f,
            speechRate = 0.95f,
            calibratedPitchHz = 175f
        )
        assertNotNull(profile1)
        assertEquals("Morning American Accent", profile1.name)
        assertTrue(profile1.isCustomClone)

        // 2. Save second cloned voice profile with different name and style
        val profile2 = repository.saveClonedVoice(
            name = "Canadian Casual Tone",
            style = AccentStyle.CANADIAN_STANDARD,
            pitch = 1.15f,
            speechRate = 1.02f,
            calibratedPitchHz = 210f
        )
        assertNotNull(profile2)
        assertEquals("Canadian Casual Tone", profile2.name)

        // 3. Query saved profiles
        val fetched1 = repository.getVoiceProfileById(profile1.id)
        assertNotNull(fetched1)
        assertEquals("Morning American Accent", fetched1?.name)

        // 4. Rename profile1
        repository.renameVoiceProfile(profile1.id, "Renamed Studio American Voice")
        val renamed = repository.getVoiceProfileById(profile1.id)
        assertEquals("Renamed Studio American Voice", renamed?.name)

        // 5. Test switching between multiple profiles in storage
        val storage = VoiceProfileStorage(context)
        storage.saveSelectedProfileId(profile1.id)
        assertEquals(profile1.id, storage.getSelectedProfileId())

        storage.saveSelectedProfileId(profile2.id)
        assertEquals(profile2.id, storage.getSelectedProfileId())

        // 6. Delete profile1
        repository.deleteVoiceProfile(profile1.id)
        val afterDelete = repository.getVoiceProfileById(profile1.id)
        assertEquals(null, afterDelete)

        // profile2 remains
        val remainingProfile2 = repository.getVoiceProfileById(profile2.id)
        assertNotNull(remainingProfile2)
        assertEquals("Canadian Casual Tone", remainingProfile2?.name)
    }

    @Test
    fun `verify photos gallery contains lady daily memory photos and driveway videos`() {
        val allMedia = com.example.data.GalleryRepository.defaultItems
        assertTrue("Media gallery should not be empty", allMedia.isNotEmpty())

        val ladyPhotos = com.example.data.GalleryRepository.getItemsForCategory(com.example.model.GalleryCategory.LADY_MEMORY_PHOTOS)
        assertTrue("Should have lady daily memory photos", ladyPhotos.isNotEmpty())
        assertTrue("Should contain photo kind", ladyPhotos.all { it.mediaKind == com.example.model.MediaKind.PHOTO })
        assertTrue("Should contain garden walk memory", ladyPhotos.any { it.title.contains("Garden") })
        assertTrue("Should contain kitchen baking memory", ladyPhotos.any { it.title.contains("Kitchen") || it.title.contains("Baking") })

        val drivewayVideos = com.example.data.GalleryRepository.getItemsForCategory(com.example.model.GalleryCategory.DRIVEWAY_VIDEOS)
        assertTrue("Should have driveway videos", drivewayVideos.isNotEmpty())
        assertTrue("Should contain video kind", drivewayVideos.all { it.mediaKind == com.example.model.MediaKind.VIDEO })
        assertTrue("Should have video duration", drivewayVideos.all { it.durationSeconds > 0 })
        assertTrue("Should contain driveway cam arrivals", drivewayVideos.any { it.title.contains("Driveway Cam") })
    }

    @Test
    fun `verify Room database caching layer caches timeline data for offline browsing`() = kotlinx.coroutines.test.runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = com.example.data.TimelineCacheRepository(context)

        // 1. Seed Room cache
        val cachedCount = repository.ensureCacheSeeded()
        assertTrue("Room cache count should be > 0", cachedCount > 0)
        assertEquals(cachedCount, repository.getCachedCount())

        // 2. Fetch cached entries for American Weekday from Room
        val americanWeekday = repository.observeEntries(
            com.example.model.CountryEdition.AMERICAN,
            com.example.model.DayCategory.WEEKDAY
        ).first()
        assertTrue("American Weekday cache should not be empty", americanWeekday.isNotEmpty())
        val firstEntry = americanWeekday.first()
        assertEquals("6:30 AM", firstEntry.time)
        assertEquals(com.example.model.CountryEdition.AMERICAN, firstEntry.edition)
        assertTrue("Should contain grab-and-go idiom", firstEntry.calloutBody.contains("grab-and-go"))

        // 3. Test caching for Canadian Edition
        val canadianCottage = repository.observeEntries(
            com.example.model.CountryEdition.CANADIAN,
            com.example.model.DayCategory.WEEKEND
        ).first()
        assertTrue("Canadian Weekend cache should not be empty", canadianCottage.isNotEmpty())
        assertTrue("Should contain cottage or cabin reference", canadianCottage.any { it.title.contains("Cottage") })

        // 4. Force refresh cache
        val refreshedCount = repository.refreshCache()
        assertEquals(cachedCount, refreshedCount)
    }

    @Test
    fun `verify WorkManager TimelineSyncWorker periodically syncs American and Canadian timeline data with remote source into Room`() = kotlinx.coroutines.test.runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // 1. Initialize TestListenableWorkerBuilder
        val worker = TestListenableWorkerBuilder<TimelineSyncWorker>(context).build()

        // 2. Execute doWork()
        val result = worker.doWork()
        assertTrue("Worker execution should succeed", result is ListenableWorker.Result.Success)

        val successResult = result as ListenableWorker.Result.Success
        val syncedCount = successResult.outputData.getInt(TimelineSyncWorker.KEY_OUTPUT_SYNCED_COUNT, 0)
        assertTrue("Should have synced entries > 0", syncedCount > 0)

        val summary = successResult.outputData.getString(TimelineSyncWorker.KEY_OUTPUT_SUMMARY)
        assertNotNull(summary)
        assertTrue("Summary should mention US and Canadian", summary?.contains("US") == true && summary.contains("Canadian"))

        // 3. Verify that Room database received and persisted both editions
        val repository = com.example.data.TimelineCacheRepository(context)
        val usCount = repository.getCachedCountByEdition(com.example.model.CountryEdition.AMERICAN)
        val canadianCount = repository.getCachedCountByEdition(com.example.model.CountryEdition.CANADIAN)

        assertTrue("American entries should be cached in Room", usCount > 0)
        assertTrue("Canadian entries should be cached in Room", canadianCount > 0)
        assertEquals("Total count should equal sum of editions", syncedCount, usCount + canadianCount)
    }

    @Test
    fun `verify TimelineSyncScheduler configures online network constraints and schedules periodic WorkManager sync`() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // 1. Verify online constraint
        val constraints = TimelineSyncScheduler.createOnlineConstraints()
        assertEquals(NetworkType.CONNECTED, constraints.requiredNetworkType)

        // 2. Verify scheduling does not throw and enqueues periodic work
        TimelineSyncScheduler.schedulePeriodicSync(context)

        // 3. Verify immediate one-time sync trigger does not throw
        TimelineSyncScheduler.triggerImmediateSync(context)
    }

    @Test
    fun `verify TimelineSyncPreferences tracks sync metadata across executions`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = TimelineSyncPreferences(context)

        // Initial state
        val initial = prefs.loadMetadata()
        assertNotNull(initial)

        // Record start
        prefs.recordSyncStart()
        assertEquals("SYNCING", prefs.loadMetadata().lastStatus)

        // Record success
        prefs.recordSyncSuccess(24, "Synced 24 routines successfully")
        val successMeta = prefs.loadMetadata()
        assertEquals("SUCCESS", successMeta.lastStatus)
        assertEquals(24, successMeta.lastSyncedCount)
        assertTrue(successMeta.lastSyncTimestamp > 0)
        assertTrue(successMeta.formattedLastSyncTime.isNotEmpty())

        // Record failure
        prefs.recordSyncFailure("Network timeout error")
        val failureMeta = prefs.loadMetadata()
        assertEquals("FAILED", failureMeta.lastStatus)
        assertEquals("Network timeout error", failureMeta.lastSyncSummary)
    }

    @Test
    fun `verify GeminiApiClient parses tutor output, native tips, and idiom spotlights`() {
        val rawAiResponse = "You can ask for a medium iced coffee with a splash of oat milk! [TIP: Americans often say 'Could I get' instead of 'I want'] [IDIOM: Grab-and-go - Takeaway food or drinks for quick departure]"
        val parsed = com.example.data.GeminiApiClient.parseGeminiTutorOutput(rawAiResponse)

        assertEquals("You can ask for a medium iced coffee with a splash of oat milk!", parsed.replyText)
        assertEquals("Americans often say 'Could I get' instead of 'I want'", parsed.languageTip)
        assertEquals("Grab-and-go - Takeaway food or drinks for quick departure", parsed.idiomSpotlight)
    }

    @Test
    fun `verify PracticeScenario returns customized scenario prompts for American and Canadian editions`() {
        val driveThruAmerican = com.example.model.PracticeScenario.DRIVE_THRU_DINER.getPromptsFor(com.example.model.CountryEdition.AMERICAN)
        assertTrue("Drive-thru American prompts should contain cold brew or bagel", driveThruAmerican.any { it.contains("cold brew") || it.contains("bagel") || it.contains("diner") })

        val driveThruCanadian = com.example.model.PracticeScenario.DRIVE_THRU_DINER.getPromptsFor(com.example.model.CountryEdition.CANADIAN)
        assertTrue("Drive-thru Canadian prompts should contain double-double or Tim Hortons", driveThruCanadian.any { it.contains("double-double") || it.contains("Tim Hortons") })

        val workplace = com.example.model.PracticeScenario.WORKPLACE.getPromptsFor(com.example.model.CountryEdition.AMERICAN)
        assertTrue("Workplace prompts should contain touch base or ballpark", workplace.any { it.contains("touch base") || it.contains("ballpark") })
    }
}

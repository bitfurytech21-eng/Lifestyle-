package com.example.data

import com.example.R
import com.example.model.GalleryCategory
import com.example.model.GalleryMediaItem
import com.example.model.MediaKind

object GalleryRepository {

    val defaultItems: List<GalleryMediaItem> = listOf(
        GalleryMediaItem(
            id = "photo_lady_garden",
            title = "Lady's Morning Flower Garden Walk",
            category = GalleryCategory.LADY_MEMORY_PHOTOS,
            mediaKind = MediaKind.PHOTO,
            drawableResId = R.drawable.img_lady_memory_garden,
            dateLabel = "Today",
            timeLabel = "8:15 AM",
            location = "Front Yard Rose & Perennial Garden",
            caption = "A quiet morning memory tending to the blooming roses and fresh cut blossoms before the neighborhood woke up. Gentle morning sunlight and cheerful birdsong fill the yard.",
            memoryQuote = "\"Every blossom tells a story of another year of care and sunshine.\" — Daily Memory Journal",
            technicalBadge = "DAILY MEMORY PHOTO • 35MM KODACHROME TONES",
            isFavorite = true
        ),
        GalleryMediaItem(
            id = "video_driveway_morning",
            title = "Driveway Cam 01: Morning Suburban Arrival",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            drawableResId = R.drawable.img_driveway_video_day,
            dateLabel = "Today",
            timeLabel = "09:42:15 AM",
            durationSeconds = 28,
            location = "Main Front Driveway & Walkway",
            caption = "Crisp morning driveway security footage. Neighborhood mail truck delivery, quiet suburban street, and clear sunny driveway pavement under blue skies.",
            technicalBadge = "DRIVEWAY CAM 01 • 1080P HD 60FPS • WIDE ANGLE",
            videoMotionAlert = "MOTION DETECTED: Mail Delivery Carrier & Vehicle",
            isFavorite = false
        ),
        GalleryMediaItem(
            id = "photo_lady_kitchen",
            title = "Kitchen Baking: Fresh Apple Cookies & Cinnamon",
            category = GalleryCategory.LADY_MEMORY_PHOTOS,
            mediaKind = MediaKind.PHOTO,
            drawableResId = R.drawable.img_lady_memory_kitchen,
            dateLabel = "Yesterday",
            timeLabel = "11:30 AM",
            location = "Home Country Kitchen",
            caption = "The cherished daily routine of rolling dough and baking warm cinnamon apple cookies. Golden sunlight streaming across the flour-dusted kitchen counter.",
            memoryQuote = "\"The secret ingredient was always taking your time and listening to the radio.\" — Kitchen Memories",
            technicalBadge = "DAILY MEMORY PHOTO • 50MM F/1.8 NATURAL LIGHT",
            isFavorite = true
        ),
        GalleryMediaItem(
            id = "video_driveway_dusk",
            title = "Driveway Cam 02: Golden Hour Homecoming Arrival",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            drawableResId = R.drawable.img_driveway_video_dusk,
            dateLabel = "Yesterday",
            timeLabel = "06:18:40 PM",
            durationSeconds = 35,
            location = "Driveway Gate & North Entrance",
            caption = "Family car pulling into the driveway under a stunning crimson and amber sunset. Porch lights illuminate the driveway entryway as headlights click off.",
            technicalBadge = "DRIVEWAY CAM 02 • NIGHT VISION AUTO-SWITCH • 2K RESOLUTION",
            videoMotionAlert = "VEHICLE RECOGNITION: Family Sedan Parked",
            isFavorite = true
        ),
        GalleryMediaItem(
            id = "photo_lady_porch",
            title = "Afternoon Rocking Chair & Tea on the Front Porch",
            category = GalleryCategory.LADY_MEMORY_PHOTOS,
            mediaKind = MediaKind.PHOTO,
            drawableResId = R.drawable.img_lady_memory_porch,
            dateLabel = "Sep 25, 2026",
            timeLabel = "3:45 PM",
            location = "Front Porch • South Garden View",
            caption = "Resting on the wooden rocking chair with a hot mug of Earl Grey, watching the afternoon breeze sway the maple trees along the driveway.",
            memoryQuote = "\"Peaceful moments looking down the driveway waiting for loved ones to return.\" — Memory Notes",
            technicalBadge = "DAILY MEMORY PHOTO • WARM GOLDEN HOUR FILM",
            isFavorite = false
        ),
        GalleryMediaItem(
            id = "video_driveway_lady_stroll",
            title = "Driveway Memory: Afternoon Walk to the Mailbox",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            drawableResId = R.drawable.img_lady_memory_porch,
            dateLabel = "Sep 24, 2026",
            timeLabel = "02:15:30 PM",
            durationSeconds = 42,
            location = "Driveway Walkway & Sidewalk",
            caption = "Cherished daily driveway memory of a gentle afternoon stroll down the asphalt driveway to greet the mail carrier and check the daily paper.",
            technicalBadge = "HOME MEMORY VIDEO • MOTION TRACKING ENABLED",
            videoMotionAlert = "PERSON DETECTED: Slow Walking Stroll",
            isFavorite = true
        )
    )

    fun getItemsForCategory(category: GalleryCategory): List<GalleryMediaItem> {
        return when (category) {
            GalleryCategory.ALL -> defaultItems
            GalleryCategory.LADY_MEMORY_PHOTOS -> defaultItems.filter { it.category == GalleryCategory.LADY_MEMORY_PHOTOS }
            GalleryCategory.DRIVEWAY_VIDEOS -> defaultItems.filter { it.category == GalleryCategory.DRIVEWAY_VIDEOS }
        }
    }
}

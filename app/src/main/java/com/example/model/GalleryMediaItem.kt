package com.example.model

enum class MediaKind {
    PHOTO,
    VIDEO
}

enum class GalleryCategory(val displayName: String, val tag: String) {
    ALL("All Media", "all"),
    PHOTOS_ONLY("Photos Gallery (40+)", "photos_40"),
    VIDEOS_ONLY("Videos Gallery (40+)", "videos_40"),
    LADY_MEMORY_PHOTOS("Lady Daily Memories", "lady_memory"),
    DRIVEWAY_VIDEOS("Driveway Security Cams", "driveway_video"),
    SUBURBAN_LIFE("Suburban Architecture", "suburban_life"),
    CULTURE_CUISINE("Culture & Cuisine", "culture_cuisine"),
    NATURAL_PARKS("Parks & Nature", "natural_parks")
}

data class GalleryMediaItem(
    val id: String,
    val title: String,
    val category: GalleryCategory,
    val mediaKind: MediaKind,
    val drawableResId: Int = 0,
    val remoteUrl: String? = null,
    val posterUrl: String? = null,
    val dateLabel: String = "2026",
    val timeLabel: String = "12:00 PM",
    val durationSeconds: Int = 0,
    val location: String = "North America",
    val caption: String = "",
    val altText: String = "",
    val memoryQuote: String? = null,
    val technicalBadge: String = "AUTHENTIC REAL-WORLD MEDIA • 1080P HD",
    val resolution: String = "1080p Full HD",
    val aspectRatio: String = "16:9",
    val tags: List<String> = emptyList(),
    val attribution: String = "Unsplash / Official Public Domain Library",
    val licenseInfo: String = "Free Commercial & Personal Use License",
    val transcript: String? = null,
    val downloadUrl: String? = null,
    val isFavorite: Boolean = false,
    val videoMotionAlert: String? = null,
    val userNote: String? = null
)

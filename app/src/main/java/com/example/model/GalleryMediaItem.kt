package com.example.model

enum class MediaKind {
    PHOTO,
    VIDEO
}

enum class GalleryCategory(val displayName: String, val tag: String) {
    ALL("All Media", "all"),
    LADY_MEMORY_PHOTOS("Lady Daily Memories", "lady_memory"),
    DRIVEWAY_VIDEOS("Driveway Videos", "driveway_video")
}

data class GalleryMediaItem(
    val id: String,
    val title: String,
    val category: GalleryCategory,
    val mediaKind: MediaKind,
    val drawableResId: Int,
    val dateLabel: String,
    val timeLabel: String,
    val durationSeconds: Int = 0,
    val location: String,
    val caption: String,
    val memoryQuote: String? = null,
    val technicalBadge: String,
    val isFavorite: Boolean = false,
    val videoMotionAlert: String? = null,
    val userNote: String? = null
)

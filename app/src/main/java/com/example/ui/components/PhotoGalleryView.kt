package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GalleryCategory
import com.example.model.GalleryMediaItem
import com.example.model.MediaKind
import com.example.ui.theme.BrickRed
import com.example.ui.theme.DenimBlue
import com.example.ui.theme.HairlineRule
import com.example.ui.theme.HairlineSubtle
import com.example.ui.theme.MutedInk
import com.example.ui.theme.MustardDark
import com.example.ui.theme.MustardGold
import com.example.ui.theme.NearBlackInk
import com.example.ui.theme.PaperCardBg
import com.example.ui.theme.PaperDarker
import com.example.ui.theme.SansFamily
import com.example.ui.theme.SerifFamily
import com.example.ui.theme.WarmPaperCream
import com.example.ui.theme.White
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryView(
    mediaItems: List<GalleryMediaItem>,
    favoriteIds: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onSpeakCaption: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(GalleryCategory.ALL) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    // Selected item for full-screen dialog
    var activeDetailItem by remember { mutableStateOf<GalleryMediaItem?>(null) }

    val filteredItems = remember(mediaItems, selectedFilter, showFavoritesOnly, favoriteIds) {
        mediaItems.filter { item ->
            val matchesCategory = when (selectedFilter) {
                GalleryCategory.ALL -> true
                GalleryCategory.LADY_MEMORY_PHOTOS -> item.category == GalleryCategory.LADY_MEMORY_PHOTOS
                GalleryCategory.DRIVEWAY_VIDEOS -> item.category == GalleryCategory.DRIVEWAY_VIDEOS
            }
            val matchesFavorite = if (showFavoritesOnly) favoriteIds.contains(item.id) || item.isFavorite else true
            matchesCategory && matchesFavorite
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmPaperCream)
            .testTag("photo_gallery_view")
    ) {
        // Gallery Header Strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, HairlineRule, RectangleShape)
                .background(PaperCardBg)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FAMILY ARCHIVES & PROPERTY SURVEILLANCE",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp,
                        color = BrickRed
                    )
                    Text(
                        text = "Daily Photos & Driveway Videos",
                        fontFamily = SerifFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = NearBlackInk
                    )
                }

                Box(
                    modifier = Modifier
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .background(WarmPaperCream)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${filteredItems.size} ITEMS",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.5.sp,
                        color = NearBlackInk
                    )
                }
            }
        }

        // Category Filter Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(WarmPaperCream)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            GalleryFilterChip(
                label = "All Media",
                isSelected = selectedFilter == GalleryCategory.ALL && !showFavoritesOnly,
                onClick = {
                    selectedFilter = GalleryCategory.ALL
                    showFavoritesOnly = false
                },
                testTag = "filter_all_media"
            )

            GalleryFilterChip(
                label = "Lady Memories",
                isSelected = selectedFilter == GalleryCategory.LADY_MEMORY_PHOTOS && !showFavoritesOnly,
                onClick = {
                    selectedFilter = GalleryCategory.LADY_MEMORY_PHOTOS
                    showFavoritesOnly = false
                },
                testTag = "filter_lady_memories"
            )

            GalleryFilterChip(
                label = "Driveway Videos",
                isSelected = selectedFilter == GalleryCategory.DRIVEWAY_VIDEOS && !showFavoritesOnly,
                onClick = {
                    selectedFilter = GalleryCategory.DRIVEWAY_VIDEOS
                    showFavoritesOnly = false
                },
                testTag = "filter_driveway_videos"
            )

            GalleryFilterChip(
                label = "Favorites",
                isSelected = showFavoritesOnly,
                onClick = {
                    showFavoritesOnly = true
                },
                testTag = "filter_favorites"
            )
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineRule)

        // Media Stream List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No media found for the selected filter.",
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = MutedInk
                        )
                    }
                }
            } else {
                items(filteredItems, key = { it.id }) { item ->
                    val isFav = favoriteIds.contains(item.id) || item.isFavorite
                    if (item.mediaKind == MediaKind.PHOTO) {
                        LadyMemoryPhotoCard(
                            item = item,
                            isFavorite = isFav,
                            onToggleFavorite = { onToggleFavorite(item.id) },
                            onSpeakCaption = { onSpeakCaption("${item.title}. ${item.caption}") },
                            onClick = { activeDetailItem = item }
                        )
                    } else {
                        DrivewayVideoCard(
                            item = item,
                            isFavorite = isFav,
                            onToggleFavorite = { onToggleFavorite(item.id) },
                            onExpandDetail = { activeDetailItem = item }
                        )
                    }
                }
            }
        }
    }

    // Detail / Full-Screen Dialog
    activeDetailItem?.let { item ->
        if (item.mediaKind == MediaKind.PHOTO) {
            PhotoDetailDialog(
                item = item,
                isFavorite = favoriteIds.contains(item.id) || item.isFavorite,
                onToggleFavorite = { onToggleFavorite(item.id) },
                onSpeak = { onSpeakCaption("${item.title}. ${item.caption}") },
                onDismiss = { activeDetailItem = null }
            )
        } else {
            DrivewayVideoDetailDialog(
                item = item,
                isFavorite = favoriteIds.contains(item.id) || item.isFavorite,
                onToggleFavorite = { onToggleFavorite(item.id) },
                onDismiss = { activeDetailItem = null }
            )
        }
    }
}

@Composable
fun GalleryFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) BrickRed else HairlineRule,
                shape = RectangleShape
            )
            .background(if (isSelected) BrickRed else PaperCardBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 5.dp)
            .testTag(testTag)
    ) {
        Text(
            text = label,
            fontFamily = SansFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.5.sp,
            color = if (isSelected) White else NearBlackInk
        )
    }
}

@Composable
fun LadyMemoryPhotoCard(
    item: GalleryMediaItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSpeakCaption: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(PaperCardBg)
            .clickable(onClick = onClick)
            .padding(10.dp)
            .testTag("lady_photo_card_${item.id}")
    ) {
        // Header: Badge & Date
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = null,
                    tint = BrickRed,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = item.technicalBadge,
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                    color = BrickRed
                )
            }

            Text(
                text = "${item.dateLabel} • ${item.timeLabel}",
                fontFamily = SansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 10.5.sp,
                color = MutedInk
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Photo Frame Container (Polaroid/Letterpress Style)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .border(1.dp, HairlineRule, RectangleShape)
                .background(Color.Black)
        ) {
            Image(
                painter = painterResource(id = item.drawableResId),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Tap to view hint overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fullscreen,
                        contentDescription = "Expand",
                        tint = White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "VIEW FULL PHOTO",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp,
                        color = White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title and Location
        Text(
            text = item.title,
            fontFamily = SerifFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = NearBlackInk
        )
        Text(
            text = "📍 ${item.location}",
            fontFamily = SansFamily,
            fontSize = 11.5.sp,
            color = DenimBlue
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Caption
        Text(
            text = item.caption,
            fontFamily = SansFamily,
            fontSize = 12.5.sp,
            lineHeight = 17.5.sp,
            color = NearBlackInk
        )

        // Memory quote if present
        item.memoryQuote?.let { quote ->
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.8.dp, MustardDark, RectangleShape)
                    .background(MustardGold.copy(alpha = 0.15f))
                    .padding(8.dp)
            ) {
                Text(
                    text = quote,
                    fontFamily = SerifFamily,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 11.5.sp,
                    color = NearBlackInk
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 0.8.dp, color = HairlineSubtle)
        Spacer(modifier = Modifier.height(6.dp))

        // Actions: Listen / Favorite
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clickable(onClick = onSpeakCaption)
                    .border(1.dp, HairlineRule, RectangleShape)
                    .background(WarmPaperCream)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("listen_photo_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                    contentDescription = "Read memory caption",
                    tint = NearBlackInk,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Listen to Memory",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = NearBlackInk
                )
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(28.dp).testTag("fav_photo_${item.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) BrickRed else MutedInk,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

@Composable
fun DrivewayVideoCard(
    item: GalleryMediaItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onExpandDetail: () -> Unit
) {
    // Interactive video playback state
    var isPlaying by remember { mutableStateOf(false) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var playbackSpeed by remember { mutableFloatStateOf(1f) }

    val totalDuration = item.durationSeconds.coerceAtLeast(1)

    // Animated playback progression
    LaunchedEffect(isPlaying, playbackSpeed) {
        if (isPlaying) {
            while (isPlaying) {
                delay(100)
                val step = (0.1f * playbackSpeed) / totalDuration
                currentProgress += step
                if (currentProgress >= 1f) {
                    currentProgress = 0f // Loop video
                }
            }
        }
    }

    val currentSeconds = (currentProgress * totalDuration).toInt()
    val formattedCurrentTime = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60)
    val formattedTotalTime = String.format("%02d:%02d", totalDuration / 60, totalDuration % 60)

    val infiniteTransition = rememberInfiniteTransition()
    val recBlinkAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(Color(0xFF141714)) // CCTV Monitor dark slate
            .padding(10.dp)
            .testTag("driveway_video_card_${item.id}")
    ) {
        // Video Header: OSD & Time
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .alpha(if (isPlaying) recBlinkAlpha else 1f)
                        .background(if (isPlaying) BrickRed else Color(0xFF6B7280), CircleShape)
                )
                Text(
                    text = if (isPlaying) "● LIVE PLAYBACK" else "○ DRIVEWAY CAM FEED",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                    color = if (isPlaying) BrickRed else Color(0xFF9CA3AF)
                )
            }

            Text(
                text = "${item.dateLabel} • ${item.timeLabel}",
                fontFamily = SansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                color = Color(0xFF9CA3AF)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Video Viewport with CRT / Camera OSD Overlays
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .border(1.dp, Color(0xFF2C322C), RectangleShape)
                .background(Color.Black)
                .clickable { isPlaying = !isPlaying }
        ) {
            Image(
                painter = painterResource(id = item.drawableResId),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Top OSD timestamp & camera title
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = item.technicalBadge,
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.5.sp,
                    color = Color(0xFF4ADE80) // Green phosphor
                )
            }

            // Motion Alert Overlay Pill if active
            item.videoMotionAlert?.let { alert ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(BrickRed.copy(alpha = 0.85f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = alert,
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp,
                        color = White
                    )
                }
            }

            // Play/Pause Center Indicator when paused
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                        .border(1.5.dp, White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play video",
                        tint = White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Bottom OSD: Timecode & Duration
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$formattedCurrentTime / $formattedTotalTime • ${playbackSpeed}x",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = White
                )
            }

            // Expand Full Screen icon button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable(onClick = onExpandDetail)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Fullscreen,
                    contentDescription = "Expand Full Screen Video",
                    tint = White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Interactive Video Scrub Bar
        Slider(
            value = currentProgress,
            onValueChange = { currentProgress = it },
            colors = SliderDefaults.colors(
                thumbColor = BrickRed,
                activeTrackColor = BrickRed,
                inactiveTrackColor = Color(0xFF374151)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .testTag("video_scrub_slider_${item.id}")
        )

        // Video Controls Row: Play/Pause, Speed, Replay, Favorite
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Play / Pause Toggle Button
                Box(
                    modifier = Modifier
                        .background(if (isPlaying) BrickRed else Color(0xFF262626))
                        .border(1.dp, Color(0xFF4B5563), RectangleShape)
                        .clickable { isPlaying = !isPlaying }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("play_pause_${item.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (isPlaying) "Pause" else "Play Video",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp,
                            color = White
                        )
                    }
                }

                // Speed Selector Button (1x -> 1.5x -> 2x)
                Box(
                    modifier = Modifier
                        .background(Color(0xFF262626))
                        .border(1.dp, Color(0xFF4B5563), RectangleShape)
                        .clickable {
                            playbackSpeed = when (playbackSpeed) {
                                1f -> 1.5f
                                1.5f -> 2f
                                else -> 1f
                            }
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("speed_${item.id}")
                ) {
                    Text(
                        text = "${playbackSpeed}x",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color(0xFFE5E7EB)
                    )
                }

                // Restart from 00:00
                Box(
                    modifier = Modifier
                        .background(Color(0xFF262626))
                        .border(1.dp, Color(0xFF4B5563), RectangleShape)
                        .clickable { currentProgress = 0f }
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Restart video",
                        tint = Color(0xFFE5E7EB),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(28.dp).testTag("fav_video_${item.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) BrickRed else Color(0xFF9CA3AF),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Title and description
        Text(
            text = item.title,
            fontFamily = SerifFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = White
        )
        Text(
            text = "📍 ${item.location}",
            fontFamily = SansFamily,
            fontSize = 11.5.sp,
            color = Color(0xFF93C5FD)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.caption,
            fontFamily = SansFamily,
            fontSize = 12.sp,
            lineHeight = 16.5.sp,
            color = Color(0xFFD1D5DB)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailDialog(
    item: GalleryMediaItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSpeak: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(WarmPaperCream)
            .testTag("photo_detail_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LADY DAILY MEMORY ARCHIVE",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.5.sp,
                    letterSpacing = 0.5.sp,
                    color = BrickRed
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NearBlackInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Full Photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .background(Color.Black)
            ) {
                Image(
                    painter = painterResource(id = item.drawableResId),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = NearBlackInk
            )
            Text(
                text = "${item.dateLabel} • ${item.timeLabel} • 📍 ${item.location}",
                fontFamily = SansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp,
                color = DenimBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.caption,
                fontFamily = SansFamily,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = NearBlackInk
            )

            item.memoryQuote?.let { quote ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MustardDark, RectangleShape)
                        .background(MustardGold.copy(alpha = 0.15f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = quote,
                        fontFamily = SerifFamily,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontSize = 12.sp,
                        color = NearBlackInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .background(WarmPaperCream)
                        .clickable(onClick = onSpeak)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                            contentDescription = null,
                            tint = NearBlackInk,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Listen Aloud",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = NearBlackInk
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(BrickRed)
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Close Photo",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrivewayVideoDetailDialog(
    item: GalleryMediaItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var playbackSpeed by remember { mutableFloatStateOf(1f) }

    val totalDuration = item.durationSeconds.coerceAtLeast(1)

    LaunchedEffect(isPlaying, playbackSpeed) {
        if (isPlaying) {
            while (isPlaying) {
                delay(100)
                val step = (0.1f * playbackSpeed) / totalDuration
                currentProgress += step
                if (currentProgress >= 1f) {
                    currentProgress = 0f
                }
            }
        }
    }

    val currentSeconds = (currentProgress * totalDuration).toInt()
    val formattedCurrentTime = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60)
    val formattedTotalTime = String.format("%02d:%02d", totalDuration / 60, totalDuration % 60)

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.5.dp, Color(0xFF374151), RectangleShape)
            .background(Color(0xFF111827))
            .testTag("driveway_video_detail_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FULL-SCREEN DRIVEWAY SURVEILLANCE FEED",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF4ADE80)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Video Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .border(1.dp, Color(0xFF374151), RectangleShape)
                    .background(Color.Black)
                    .clickable { isPlaying = !isPlaying }
            ) {
                Image(
                    painter = painterResource(id = item.drawableResId),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top OSD
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "● REC ${item.technicalBadge}",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp,
                        color = Color(0xFF4ADE80)
                    )
                }

                // Motion Alert
                item.videoMotionAlert?.let { alert ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(BrickRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = alert,
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.sp,
                            color = White
                        )
                    }
                }

                // Bottom timecode
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$formattedCurrentTime / $formattedTotalTime • SPEED: ${playbackSpeed}X",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = White
                    )
                }
            }

            Slider(
                value = currentProgress,
                onValueChange = { currentProgress = it },
                colors = SliderDefaults.colors(
                    thumbColor = BrickRed,
                    activeTrackColor = BrickRed,
                    inactiveTrackColor = Color(0xFF374151)
                ),
                modifier = Modifier.fillMaxWidth().height(26.dp)
            )

            // Transport row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .background(if (isPlaying) BrickRed else Color(0xFF1F2937))
                            .border(1.dp, Color(0xFF4B5563), RectangleShape)
                            .clickable { isPlaying = !isPlaying }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isPlaying) "Pause" else "Play",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1F2937))
                            .border(1.dp, Color(0xFF4B5563), RectangleShape)
                            .clickable {
                                playbackSpeed = when (playbackSpeed) {
                                    1f -> 1.5f
                                    1.5f -> 2f
                                    else -> 1f
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${playbackSpeed}x Speed",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFFE5E7EB)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1F2937))
                            .border(1.dp, Color(0xFF4B5563), RectangleShape)
                            .clickable { currentProgress = 0f }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Replay",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFFE5E7EB)
                        )
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) BrickRed else Color(0xFF9CA3AF),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = White
            )
            Text(
                text = "📍 ${item.location} • ${item.dateLabel} • ${item.timeLabel}",
                fontFamily = SansFamily,
                fontSize = 11.5.sp,
                color = Color(0xFF93C5FD)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.caption,
                fontFamily = SansFamily,
                fontSize = 12.5.sp,
                lineHeight = 17.sp,
                color = Color(0xFFD1D5DB)
            )
        }
    }
}

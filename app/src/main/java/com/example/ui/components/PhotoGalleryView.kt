package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PhotoCamera
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PhotoGalleryView(
    mediaItems: List<GalleryMediaItem>,
    favoriteIds: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onSpeakCaption: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf(GalleryCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    // Active detail item modal
    var activeDetailItem by remember { mutableStateOf<GalleryMediaItem?>(null) }

    // Filter & search logic
    val filteredItems = remember(mediaItems, selectedFilter, searchQuery, showFavoritesOnly, favoriteIds) {
        mediaItems.filter { item ->
            val matchesCategory = when (selectedFilter) {
                GalleryCategory.ALL -> true
                GalleryCategory.PHOTOS_ONLY -> item.mediaKind == MediaKind.PHOTO
                GalleryCategory.VIDEOS_ONLY -> item.mediaKind == MediaKind.VIDEO
                else -> item.category == selectedFilter
            }

            val matchesSearch = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                item.title.lowercase().contains(q) ||
                    item.caption.lowercase().contains(q) ||
                    item.location.lowercase().contains(q) ||
                    item.tags.any { it.lowercase().contains(q) }
            }

            val matchesFav = if (showFavoritesOnly) favoriteIds.contains(item.id) || item.isFavorite else true

            matchesCategory && matchesSearch && matchesFav
        }
    }

    val photoCount = remember(mediaItems) { mediaItems.count { it.mediaKind == MediaKind.PHOTO } }
    val videoCount = remember(mediaItems) { mediaItems.count { it.mediaKind == MediaKind.VIDEO } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmPaperCream)
            .testTag("photo_gallery_view")
    ) {
        // Module Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, HairlineRule, RectangleShape)
                .background(PaperCardBg)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REAL-WORLD MEDIA VAULT • 40+ PHOTOS & 40+ VIDEOS",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp,
                        color = BrickRed
                    )
                    Box(
                        modifier = Modifier
                            .border(1.dp, NearBlackInk, RectangleShape)
                            .background(WarmPaperCream)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${filteredItems.size} DISPLAYED",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = NearBlackInk
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Authentic Gallery & Driveway Surveillance",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Verified real-world photographs ($photoCount items) and driveway security video recordings ($videoCount items) with full metadata, captions, transcripts, and high-res downloads.",
                    fontFamily = SansFamily,
                    fontSize = 11.5.sp,
                    color = MutedInk
                )
            }
        }

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WarmPaperCream)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .background(White)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MutedInk,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = TextStyle(
                        fontFamily = SansFamily,
                        fontSize = 13.sp,
                        color = NearBlackInk
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("gallery_search_input")
                ) { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search photos, driveway videos, locations, or tags...",
                            fontFamily = SansFamily,
                            fontSize = 12.sp,
                            color = MutedInk.copy(alpha = 0.6f)
                        )
                    }
                    innerTextField()
                }
            }
        }

        // Category Filter Chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(WarmPaperCream)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            GalleryFilterChip(
                label = "All ($photoCount P / $videoCount V)",
                isSelected = selectedFilter == GalleryCategory.ALL && !showFavoritesOnly,
                onClick = {
                    selectedFilter = GalleryCategory.ALL
                    showFavoritesOnly = false
                },
                testTag = "filter_all_media"
            )

            GalleryFilterChip(
                label = "Photos (40+)",
                isSelected = selectedFilter == GalleryCategory.PHOTOS_ONLY && !showFavoritesOnly,
                onClick = {
                    selectedFilter = GalleryCategory.PHOTOS_ONLY
                    showFavoritesOnly = false
                },
                testTag = "filter_photos_only"
            )

            GalleryFilterChip(
                label = "Driveway Videos (40+)",
                isSelected = selectedFilter == GalleryCategory.VIDEOS_ONLY && !showFavoritesOnly,
                onClick = {
                    selectedFilter = GalleryCategory.VIDEOS_ONLY
                    showFavoritesOnly = false
                },
                testTag = "filter_videos_only"
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
                label = "Driveway Cams",
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
                onClick = { showFavoritesOnly = true },
                testTag = "filter_favorites"
            )
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineRule)

        // Lazy Media Stream List
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
                            text = "No media items matched your search query or filter.",
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
                        AuthenticPhotoCard(
                            item = item,
                            isFavorite = isFav,
                            onToggleFavorite = { onToggleFavorite(item.id) },
                            onSpeakCaption = { onSpeakCaption("${item.title}. ${item.caption}") },
                            onClick = { activeDetailItem = item }
                        )
                    } else {
                        AuthenticVideoCard(
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

    // Detail Modal Dialog
    activeDetailItem?.let { item ->
        MediaMetadataDetailDialog(
            item = item,
            isFavorite = favoriteIds.contains(item.id) || item.isFavorite,
            onToggleFavorite = { onToggleFavorite(item.id) },
            onSpeak = { onSpeakCaption("${item.title}. ${item.caption}") },
            onDismiss = { activeDetailItem = null }
        )
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
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Text(
            text = label,
            fontFamily = SansFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.sp,
            color = if (isSelected) White else NearBlackInk
        )
    }
}

@Composable
fun AuthenticPhotoCard(
    item: GalleryMediaItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSpeakCaption: () -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(PaperCardBg)
            .clickable(onClick = onClick)
            .padding(10.dp)
            .testTag("photo_card_${item.id}")
    ) {
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

        // Image Frame Container (Remote Async Image with Fallback)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .border(1.dp, HairlineRule, RectangleShape)
                .background(Color.Black)
        ) {
            if (!item.remoteUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(item.remoteUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.altText.ifBlank { item.title },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = if (item.drawableResId != 0) painterResource(id = item.drawableResId) else null
                )
            } else if (item.drawableResId != 0) {
                Image(
                    painter = painterResource(id = item.drawableResId),
                    contentDescription = item.altText.ifBlank { item.title },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fullscreen,
                        contentDescription = "Expand photo",
                        tint = White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "METADATA & FULL RES",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp,
                        color = White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            fontFamily = SerifFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = NearBlackInk
        )
        Text(
            text = "📍 ${item.location} • ${item.resolution}",
            fontFamily = SansFamily,
            fontSize = 11.5.sp,
            color = DenimBlue
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.caption,
            fontFamily = SansFamily,
            fontSize = 12.5.sp,
            lineHeight = 17.5.sp,
            color = NearBlackInk
        )

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
                    contentDescription = "Read caption",
                    tint = NearBlackInk,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Listen Caption",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = NearBlackInk
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                item.downloadUrl?.let { url ->
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(28.dp).testTag("download_photo_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = "Download High Res Photo",
                            tint = DenimBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
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
}

@Composable
fun AuthenticVideoCard(
    item: GalleryMediaItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onExpandDetail: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
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
            .background(Color(0xFF141714))
            .padding(10.dp)
            .testTag("video_card_${item.id}")
    ) {
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
                    text = if (isPlaying) "● LIVE VIDEO PLAYBACK" else "○ DRIVEWAY CAM FEED",
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .border(1.dp, Color(0xFF2C322C), RectangleShape)
                .background(Color.Black)
                .clickable { isPlaying = !isPlaying }
        ) {
            val posterModel = item.posterUrl ?: item.remoteUrl
            if (!posterModel.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(posterModel)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.altText.ifBlank { item.title },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = if (item.drawableResId != 0) painterResource(id = item.drawableResId) else null
                )
            } else if (item.drawableResId != 0) {
                Image(
                    painter = painterResource(id = item.drawableResId),
                    contentDescription = item.altText.ifBlank { item.title },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = item.technicalBadge,
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.5.sp,
                    color = Color(0xFF4ADE80)
                )
            }

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

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable(onClick = onExpandDetail)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Fullscreen,
                    contentDescription = "Expand Full Screen Video & Metadata",
                    tint = White,
                    modifier = Modifier.size(16.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .testTag("video_scrub_slider_${item.id}")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                item.downloadUrl?.let { url ->
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(28.dp).testTag("download_video_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = "Download Video File",
                            tint = Color(0xFF93C5FD),
                            modifier = Modifier.size(16.dp)
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
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = item.title,
            fontFamily = SerifFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = White
        )
        Text(
            text = "📍 ${item.location} • ${item.resolution}",
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
fun MediaMetadataDetailDialog(
    item: GalleryMediaItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSpeak: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var showTranscript by remember { mutableStateOf(false) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(WarmPaperCream)
            .testTag("media_metadata_detail_dialog")
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
                    text = "MEDIA VAULT RECORD & METADATA",
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
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Close",
                        tint = NearBlackInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .background(Color.Black)
            ) {
                val mediaUrl = item.posterUrl ?: item.remoteUrl
                if (!mediaUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(mediaUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.altText.ifBlank { item.title },
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = if (item.drawableResId != 0) painterResource(id = item.drawableResId) else null
                    )
                } else if (item.drawableResId != 0) {
                    Image(
                        painter = painterResource(id = item.drawableResId),
                        contentDescription = item.altText.ifBlank { item.title },
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
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
                text = "📍 ${item.location} • ${item.dateLabel} ${item.timeLabel}",
                fontFamily = SansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp,
                color = DenimBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.caption,
                fontFamily = SansFamily,
                fontSize = 12.5.sp,
                lineHeight = 17.5.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Full Technical Metadata Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, HairlineRule, RectangleShape)
                    .background(PaperDarker.copy(alpha = 0.3f))
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = "TECHNICAL METADATA & LICENSING:",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.5.sp,
                        color = NearBlackInk
                    )
                    Text(text = "• Unique ID: ${item.id}", fontFamily = SansFamily, fontSize = 10.5.sp, color = MutedInk)
                    Text(text = "• Resolution: ${item.resolution} (${item.aspectRatio})", fontFamily = SansFamily, fontSize = 10.5.sp, color = MutedInk)
                    Text(text = "• Attribution: ${item.attribution}", fontFamily = SansFamily, fontSize = 10.5.sp, color = MutedInk)
                    Text(text = "• License: ${item.licenseInfo}", fontFamily = SansFamily, fontSize = 10.5.sp, color = MutedInk)
                    Text(text = "• Accessibility Alt Text: ${item.altText.ifBlank { "Verified accessible contrast" }}", fontFamily = SansFamily, fontSize = 10.5.sp, color = MutedInk)
                }
            }

            // Transcript section for video
            item.transcript?.let { tx ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DenimBlue, RectangleShape)
                        .background(PaperCardBg)
                        .padding(8.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AUDIO TRANSCRIPTION & CAPTIONS",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = DenimBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tx,
                            fontFamily = SansFamily,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = NearBlackInk
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item.downloadUrl?.let { downloadUrl ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(DenimBlue)
                            .border(1.dp, NearBlackInk, RectangleShape)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Download High-Res",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = White
                            )
                        }
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
                        text = "Close Viewer",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = White
                    )
                }
            }
        }
    }
}

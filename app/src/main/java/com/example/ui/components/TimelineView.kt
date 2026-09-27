package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CalloutType
import com.example.model.CountryEdition
import com.example.model.DayCategory
import com.example.model.TimelineEntry
import com.example.ui.theme.BrickRed
import com.example.ui.theme.DenimBlue
import com.example.ui.theme.HairlineRule
import com.example.ui.theme.HairlineSubtle
import com.example.ui.theme.MutedInk
import com.example.ui.theme.MustardDark
import com.example.ui.theme.MustardGold
import com.example.ui.theme.NearBlackInk
import com.example.ui.theme.PaperCardBg
import com.example.ui.theme.SansFamily
import com.example.ui.theme.SerifFamily
import com.example.ui.theme.WarmPaperCream

@Composable
fun TimelineView(
    entries: List<TimelineEntry>,
    edition: CountryEdition,
    selectedCategory: DayCategory,
    bookmarkedIds: Set<String>,
    isOfflineCached: Boolean = true,
    cachedCount: Int = 0,
    isRefreshingCache: Boolean = false,
    isSyncingWithRemote: Boolean = false,
    lastSyncFormatted: String = "",
    onRefreshCache: () -> Unit = {},
    onTriggerRemoteSync: () -> Unit = {},
    onSelectCategory: (DayCategory) -> Unit,
    onEntryClick: (TimelineEntry) -> Unit,
    onPracticeInChat: (TimelineEntry) -> Unit,
    onSpeak: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCanadian = edition == CountryEdition.CANADIAN

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmPaperCream)
            .testTag("timeline_list")
    ) {
        // Section 1: Hero Banner Illustration (Vintage Americana or Canadian wilderness field guide)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .border(1.dp, NearBlackInk, RectangleShape)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isCanadian) R.drawable.canadian_guide_banner_1790524362878 else R.drawable.field_guide_banner_1790523131420
                        ),
                        contentDescription = "Field Guide Landscape",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Section Intro
                Text(
                    text = if (isCanadian) "A Chronological Field Guide to Everyday Life in Canada" else "A Chronological Field Guide to Everyday Life in the USA",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NearBlackInk
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isCanadian) {
                        "From morning Tim Hortons runs to lakeside cottage weekends and pond hockey, discover authentic routines, customs, and idioms ordinary Canadians live by every day."
                    } else {
                        "From morning coffee runs to suburban cookouts and Thanksgiving turkey trots, learn authentic routines, customs, and idioms ordinary Americans live by every day."
                    },
                    fontFamily = SansFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MutedInk
                )
            }
        }

        // Section 2: Room Database Caching & WorkManager Offline Sync Status Strip
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .border(1.dp, HairlineRule, RectangleShape)
                    .background(PaperCardBg)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
                    .testTag("offline_cache_status_bar")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(DenimBlue.copy(alpha = 0.12f))
                                .border(0.5.dp, DenimBlue, RectangleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Storage,
                                contentDescription = "Local SQLite Cache",
                                tint = DenimBlue,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "ROOM DB CACHE",
                                    fontFamily = SansFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp,
                                    color = DenimBlue
                                )
                                Box(
                                    modifier = Modifier
                                        .background(BrickRed.copy(alpha = 0.1f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "WORKMANAGER SYNC",
                                        fontFamily = SansFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.sp,
                                        color = BrickRed
                                    )
                                }
                            }
                            Text(
                                text = "Cached offline (${if (cachedCount > 0) cachedCount else entries.size} routines) • Auto-syncs periodically when online",
                                fontFamily = SansFamily,
                                fontSize = 11.sp,
                                color = MutedInk,
                                maxLines = 1
                            )
                            if (lastSyncFormatted.isNotBlank()) {
                                Text(
                                    text = "Last synced with remote: $lastSyncFormatted",
                                    fontFamily = SansFamily,
                                    fontSize = 9.5.sp,
                                    color = MutedInk.copy(alpha = 0.85f),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isSyncingWithRemote || isRefreshingCache) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(18.dp)
                                    .testTag("sync_progress_indicator"),
                                strokeWidth = 2.dp,
                                color = DenimBlue
                            )
                        } else {
                            IconButton(
                                onClick = onTriggerRemoteSync,
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("trigger_sync_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Sync,
                                    contentDescription = "Sync with Remote",
                                    tint = DenimBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Category Filter Bar (Flat, newspaper subsection style)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                HorizontalDivider(thickness = 1.dp, color = HairlineRule)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DayCategory.values().forEach { category ->
                        val isSelected = category == selectedCategory
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) BrickRed else HairlineSubtle,
                                    shape = RectangleShape
                                )
                                .background(if (isSelected) BrickRed else WarmPaperCream)
                                .clickable { onSelectCategory(category) }
                                .padding(vertical = 7.dp, horizontal = 4.dp)
                                .testTag("filter_${category.name.lowercase()}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (category) {
                                    DayCategory.WEEKDAY -> "Weekday"
                                    DayCategory.WEEKEND -> if (isCanadian) "Weekend / Cottage" else "Weekend"
                                    DayCategory.HOLIDAYS -> "Traditions"
                                },
                                fontFamily = SansFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.5.sp,
                                color = if (isSelected) WarmPaperCream else NearBlackInk
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(thickness = 1.dp, color = HairlineRule)
            }
        }

        // Section 3: Chronological Timeline Entries
        itemsIndexed(entries, key = { _, entry -> entry.id }) { _, entry ->
            val isBookmarked = bookmarkedIds.contains(entry.id)

            TimelineEntryRow(
                entry = entry,
                isBookmarked = isBookmarked,
                tutorName = edition.tutorName,
                onEntryClick = { onEntryClick(entry) },
                onPracticeInChat = { onPracticeInChat(entry) },
                onSpeak = onSpeak,
                onToggleBookmark = { onToggleBookmark(entry.id) }
            )

            // Hairline separator between entries (1px rule, no drop shadow)
            HorizontalDivider(
                thickness = 1.dp,
                color = HairlineSubtle,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun TimelineEntryRow(
    entry: TimelineEntry,
    isBookmarked: Boolean,
    tutorName: String,
    onEntryClick: () -> Unit,
    onPracticeInChat: () -> Unit,
    onSpeak: (String) -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onEntryClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .height(IntrinsicSize.Min)
            .testTag("timeline_item_${entry.id}")
    ) {
        // Left Column: Fixed-width Time Stamp (76.dp)
        Column(
            modifier = Modifier
                .width(76.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = entry.time,
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = when (entry.category) {
                    DayCategory.WEEKDAY -> "WEEKDAY"
                    DayCategory.WEEKEND -> "WEEKEND"
                    DayCategory.HOLIDAYS -> "ANNUAL"
                },
                fontFamily = SansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp,
                color = MutedInk
            )
        }

        // Hairline Vertical Rule
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(HairlineSubtle)
        )

        Spacer(modifier = Modifier.width(14.dp))

        // Right Column: Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = entry.title,
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                lineHeight = 22.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.description,
                fontFamily = SansFamily,
                fontSize = 13.5.sp,
                lineHeight = 19.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Callout Box: Thin Mustard Left Border (marginal note)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = MustardGold,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 3.5.dp.toPx()
                        )
                    }
                    .background(PaperCardBg.copy(alpha = 0.5f))
                    .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .testTag("callout_${entry.id}")
            ) {
                Column {
                    Text(
                        text = entry.calloutType.displayBadge.uppercase(),
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.4.sp,
                        color = MustardDark
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = entry.calloutTitle,
                        fontFamily = SerifFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NearBlackInk
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = entry.calloutBody,
                        fontFamily = SansFamily,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp,
                        color = NearBlackInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .border(1.dp, DenimBlue, RectangleShape)
                            .background(WarmPaperCream)
                            .clickable(onClick = onPracticeInChat)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("practice_btn_${entry.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Practice in Chat",
                                tint = DenimBlue,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Practice with $tutorName",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp,
                                color = DenimBlue
                            )
                        }
                    }

                    if (entry.idiomSample != null) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, HairlineSubtle, RectangleShape)
                                .background(WarmPaperCream)
                                .clickable { onSpeak(entry.idiomSample) }
                                .padding(horizontal = 7.dp, vertical = 4.dp)
                                .testTag("speak_btn_${entry.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                    contentDescription = "Hear Pronunciation",
                                    tint = NearBlackInk,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Hear",
                                    fontFamily = SansFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = NearBlackInk
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("bookmark_btn_${entry.id}")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark entry",
                        tint = if (isBookmarked) BrickRed else MutedInk,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

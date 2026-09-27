package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TimelineRepository
import com.example.model.TimelineEntry
import com.example.ui.theme.BrickRed
import com.example.ui.theme.DenimBlue
import com.example.ui.theme.HairlineRule
import com.example.ui.theme.HairlineSubtle
import com.example.ui.theme.MutedInk
import com.example.ui.theme.NearBlackInk
import com.example.ui.theme.PaperCardBg
import com.example.ui.theme.SansFamily
import com.example.ui.theme.SerifFamily
import com.example.ui.theme.WarmPaperCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedNotesDialog(
    bookmarkedIds: Set<String>,
    onDismiss: () -> Unit,
    onSelectEntry: (TimelineEntry) -> Unit,
    onRemoveBookmark: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bookmarkedEntries = TimelineRepository.allEntries.filter { bookmarkedIds.contains(it.id) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(WarmPaperCream)
            .testTag("saved_notes_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FIELD DISPATCHES",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        letterSpacing = 0.5.sp,
                        color = BrickRed
                    )
                    Text(
                        text = "Saved Field Notes & Idioms",
                        fontFamily = SerifFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = NearBlackInk
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp).testTag("saved_notes_close_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NearBlackInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(thickness = 1.dp, color = HairlineRule)
            Spacer(modifier = Modifier.height(12.dp))

            if (bookmarkedEntries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No saved field notes yet.",
                            fontFamily = SerifFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = NearBlackInk
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap the bookmark icon on any timeline entry to save idioms and customs for quick reference.",
                            fontFamily = SansFamily,
                            fontSize = 12.5.sp,
                            color = MutedInk,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(bookmarkedEntries, key = { it.id }) { entry ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, HairlineSubtle, RectangleShape)
                                .background(PaperCardBg)
                                .clickable {
                                    onDismiss()
                                    onSelectEntry(entry)
                                }
                                .padding(10.dp)
                                .testTag("saved_note_item_${entry.id}")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${entry.time} • ${entry.title}",
                                        fontFamily = SerifFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NearBlackInk
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = entry.calloutTitle,
                                        fontFamily = SansFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = DenimBlue
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = entry.calloutBody,
                                        fontFamily = SansFamily,
                                        fontSize = 11.5.sp,
                                        lineHeight = 15.sp,
                                        color = NearBlackInk,
                                        maxLines = 2
                                    )
                                }

                                IconButton(
                                    onClick = { onRemoveBookmark(entry.id) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = "Remove Bookmark",
                                        tint = MutedInk,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

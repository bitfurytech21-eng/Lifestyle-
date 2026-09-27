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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineDetailDialog(
    entry: TimelineEntry,
    onDismiss: () -> Unit,
    onPracticeInChat: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(WarmPaperCream)
            .testTag("entry_detail_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Header Bar with Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FIELD NOTE • ${entry.time}",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                        color = BrickRed
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp).testTag("dialog_close_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NearBlackInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.title,
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                lineHeight = 26.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(thickness = 1.dp, color = HairlineRule)
            Spacer(modifier = Modifier.height(10.dp))

            // Routine Description
            Text(
                text = entry.description,
                fontFamily = SansFamily,
                fontSize = 14.5.sp,
                lineHeight = 21.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Callout Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = MustardGold,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 4.dp.toPx()
                        )
                    }
                    .background(PaperCardBg)
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = entry.calloutType.displayBadge.uppercase(),
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        color = MustardDark
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = entry.calloutTitle,
                        fontFamily = SerifFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NearBlackInk
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = entry.calloutBody,
                        fontFamily = SansFamily,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = NearBlackInk
                    )
                }
            }

            // Spoken Sample Section
            if (entry.idiomSample != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, HairlineSubtle, RectangleShape)
                        .background(WarmPaperCream)
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NATURAL SPOKEN USAGE",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp,
                                color = MutedInk
                            )

                            Box(
                                modifier = Modifier
                                    .border(1.dp, NearBlackInk, RectangleShape)
                                    .clickable { onSpeak(entry.idiomSample) }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                        contentDescription = "Listen",
                                        tint = NearBlackInk,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "Play Voice",
                                        fontFamily = SansFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 10.5.sp,
                                        color = NearBlackInk
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "\"${entry.idiomSample}\"",
                            fontFamily = SerifFamily,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontSize = 13.5.sp,
                            lineHeight = 19.sp,
                            color = NearBlackInk
                        )
                    }
                }
            }

            // Cultural Background Deep Dive
            if (entry.culturalContextExtra.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Cultural Context & Customs",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = entry.culturalContextExtra,
                    fontFamily = SansFamily,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    color = NearBlackInk
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(thickness = 1.dp, color = HairlineSubtle)
            Spacer(modifier = Modifier.height(12.dp))

            // Action Button: Practice in Chat
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DenimBlue)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .clickable(onClick = onPracticeInChat)
                    .padding(vertical = 12.dp)
                    .testTag("dialog_practice_chat_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = WarmPaperCream,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Practice this in Chat with ${entry.edition.tutorName}",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = WarmPaperCream
                    )
                }
            }
        }
    }
}

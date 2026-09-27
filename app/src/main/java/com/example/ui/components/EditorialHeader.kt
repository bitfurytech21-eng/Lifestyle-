package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CountryEdition
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

/**
 * Persistent Header Component that allows users to toggle between 'American' and 'Canadian'
 * content streams, updating the timeline data source and conversational context accordingly.
 */
@Composable
fun EditorialHeader(
    edition: CountryEdition,
    savedCount: Int,
    activeVoiceName: String,
    isCustomVoice: Boolean,
    onSwitchEdition: (CountryEdition) -> Unit,
    onOpenSavedNotes: () -> Unit,
    onOpenVoiceStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAmerican = edition == CountryEdition.AMERICAN

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WarmPaperCream)
            .testTag("persistent_header_container")
    ) {
        // --- Top Utility Bar: Voice Studio & Saved Notes ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Field Guide Edition Label Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Public,
                    contentDescription = null,
                    tint = MutedInk,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "FIELD GUIDE • ISSUE NO. 24",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.8.sp,
                    color = MutedInk
                )
            }

            // Voice Studio & Saved Notes Quick Access
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Voice Studio Button
                Box(
                    modifier = Modifier
                        .clickable(
                            role = Role.Button,
                            onClick = onOpenVoiceStudio
                        )
                        .border(1.dp, if (isCustomVoice) MustardDark else DenimBlue, RectangleShape)
                        .background(if (isCustomVoice) MustardGold.copy(alpha = 0.15f) else WarmPaperCream)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .heightIn(min = 28.dp)
                        .testTag("voice_studio_header_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.GraphicEq,
                            contentDescription = "Voice Clone Studio",
                            tint = if (isCustomVoice) MustardDark else DenimBlue,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (isCustomVoice) "Voice: $activeVoiceName" else "Voice Studio",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp,
                            color = if (isCustomVoice) NearBlackInk else DenimBlue,
                            maxLines = 1
                        )
                    }
                }

                // Saved Notes Button
                Box(
                    modifier = Modifier
                        .clickable(
                            role = Role.Button,
                            onClick = onOpenSavedNotes
                        )
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .background(WarmPaperCream)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .heightIn(min = 28.dp)
                        .testTag("saved_notes_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (savedCount > 0) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Saved Notes",
                            tint = if (savedCount > 0) BrickRed else NearBlackInk,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (savedCount > 0) "Notes ($savedCount)" else "Notes",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp,
                            color = NearBlackInk
                        )
                    }
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineRule)

        // --- Persistent Content Stream Toggle Segment ---
        PersistentStreamToggleBar(
            selectedEdition = edition,
            onSwitchEdition = onSwitchEdition,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 7.dp)
        )

        HorizontalDivider(thickness = 1.dp, color = HairlineSubtle)

        // --- Masthead Title Row (Reflects Active Content Stream) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = edition.fullTitle,
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    lineHeight = 26.sp,
                    color = NearBlackInk,
                    modifier = Modifier.testTag("app_masthead")
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = edition.subheader,
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.5.sp,
                    color = MutedInk,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Postage Stamp Emblem (USA Stamp vs Canadian Maple Stamp)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .padding(2.dp)
                    .testTag("edition_postage_stamp")
            ) {
                Image(
                    painter = painterResource(
                        id = if (isAmerican) R.drawable.daily_american_icon_1790523117496 else R.drawable.daily_canadian_stamp_1790524348008
                    ),
                    contentDescription = "${edition.shortName} Postage Stamp",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        }

        HorizontalDivider(thickness = 2.dp, color = HairlineRule)
        Spacer(modifier = Modifier.height(1.dp))
        HorizontalDivider(thickness = 1.dp, color = HairlineSubtle)
    }
}

/**
 * Dedicated Segmented Content Stream Switcher component for toggling between
 * American and Canadian content streams.
 */
@Composable
fun PersistentStreamToggleBar(
    selectedEdition: CountryEdition,
    onSwitchEdition: (CountryEdition) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.testTag("persistent_stream_toggle_bar"),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Stream selection row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NearBlackInk, RectangleShape)
                .background(PaperCardBg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // American Content Stream Option
            StreamToggleOption(
                title = "American Stream",
                subtitle = "USA Daily Life & Idioms",
                tag = "USA",
                isSelected = selectedEdition == CountryEdition.AMERICAN,
                activeColor = BrickRed,
                onClick = { onSwitchEdition(CountryEdition.AMERICAN) },
                testTag = "stream_toggle_american",
                modifier = Modifier.weight(1f)
            )

            // Vertical separator between streams
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(48.dp)
                    .background(NearBlackInk)
            )

            // Canadian Content Stream Option
            StreamToggleOption(
                title = "Canadian Stream",
                subtitle = "Canada Routines & Slang",
                tag = "CAN",
                isSelected = selectedEdition == CountryEdition.CANADIAN,
                activeColor = DenimBlue,
                onClick = { onSwitchEdition(CountryEdition.CANADIAN) },
                testTag = "stream_toggle_canadian",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StreamToggleOption(
    title: String,
    subtitle: String,
    tag: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else PaperCardBg,
        animationSpec = tween(durationMillis = 180),
        label = "stream_toggle_bg"
    )
    val titleColor by animateColorAsState(
        targetValue = if (isSelected) WarmPaperCream else NearBlackInk,
        animationSpec = tween(durationMillis = 180),
        label = "stream_toggle_title_color"
    )
    val subtitleColor by animateColorAsState(
        targetValue = if (isSelected) WarmPaperCream.copy(alpha = 0.85f) else MutedInk,
        animationSpec = tween(durationMillis = 180),
        label = "stream_toggle_sub_color"
    )

    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 7.dp)
            .testTag(testTag),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Stamp Tag badge
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (isSelected) WarmPaperCream else NearBlackInk,
                        shape = RectangleShape
                    )
                    .background(if (isSelected) Color.Black.copy(alpha = 0.25f) else WarmPaperCream)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tag,
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                    color = if (isSelected) WarmPaperCream else NearBlackInk
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        fontFamily = SansFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = titleColor
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Active",
                            tint = WarmPaperCream,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
                Text(
                    text = subtitle,
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 9.5.sp,
                    color = subtitleColor,
                    maxLines = 1
                )
            }
        }
    }
}


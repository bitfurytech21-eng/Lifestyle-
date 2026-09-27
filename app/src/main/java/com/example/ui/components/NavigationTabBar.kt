package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CountryEdition
import com.example.ui.theme.BrickRed
import com.example.ui.theme.DenimBlue
import com.example.ui.theme.HairlineRule
import com.example.ui.theme.HairlineSubtle
import com.example.ui.theme.MutedInk
import com.example.ui.theme.NearBlackInk
import com.example.ui.theme.PaperCardBg
import com.example.ui.theme.SerifFamily
import com.example.ui.theme.WarmPaperCream
import com.example.viewmodel.AppTab

@Composable
fun NavigationTabBar(
    selectedTab: AppTab,
    edition: CountryEdition,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WarmPaperCream)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AppTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val tabTitle = when (tab) {
                    AppTab.TIMELINE -> if (edition == CountryEdition.CANADIAN) "Day in Canada" else "Day in America"
                    AppTab.GALLERY -> "Photos Gallery"
                    AppTab.TUTOR -> if (edition == CountryEdition.CANADIAN) "Canadian Tutor" else "American Tutor"
                }
                val tag = when (tab) {
                    AppTab.TIMELINE -> "tab_day_in_country"
                    AppTab.GALLERY -> "tab_photos_gallery"
                    AppTab.TUTOR -> "tab_ai_tutor"
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSelected) PaperCardBg else WarmPaperCream)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = DenimBlue)
                        ) {
                            onTabSelected(tab)
                        }
                        .padding(vertical = 11.dp, horizontal = 4.dp)
                        .testTag(tag),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = tabTitle,
                            fontFamily = SerifFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (isSelected) NearBlackInk else MutedInk,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Active indicator bar: 3dp Brick Red line under the active tab
        Row(modifier = Modifier.fillMaxWidth()) {
            AppTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(if (isSelected) BrickRed else HairlineSubtle)
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineRule)
    }
}

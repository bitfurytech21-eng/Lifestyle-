package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ChatMessage
import com.example.model.CountryEdition
import com.example.model.PracticeScenario
import com.example.model.VoiceProfile
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
import com.example.ui.theme.White

/**
 * Conversational interface for practicing spoken English and idioms with an AI tutor,
 * integrated directly with the Gemini 3.5 Flash API.
 */
@Composable
fun TutorChatView(
    messages: List<ChatMessage>,
    edition: CountryEdition,
    selectedScenario: PracticeScenario = PracticeScenario.CASUAL_CHAT,
    isTutorThinking: Boolean,
    activeVoiceProfile: VoiceProfile,
    isSpeakingSlowly: Boolean = false,
    onSendMessage: (String) -> Unit,
    onSpeakText: (String, Boolean) -> Unit,
    onToggleSlowSpeech: () -> Unit = {},
    onSelectScenario: (PracticeScenario) -> Unit = {},
    onOpenVoiceStudio: () -> Unit,
    onResetConversation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val isCanadian = edition == CountryEdition.CANADIAN

    val starterTopics = selectedScenario.getPromptsFor(edition)

    LaunchedEffect(messages.size, isTutorThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmPaperCream)
            .testTag("tutor_chat_container")
    ) {
        // 1. Tutor Masthead Profile Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PaperCardBg)
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.dp, NearBlackInk, RectangleShape)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isCanadian) R.drawable.canadian_tutor_maple_1790524376131 else R.drawable.sam_tutor_avatar_1790523151328
                        ),
                        contentDescription = if (isCanadian) "Robin - Canadian Tutor" else "Sam - American Tutor",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = edition.tutorName,
                            fontFamily = SerifFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NearBlackInk
                        )
                        Box(
                            modifier = Modifier
                                .border(0.8.dp, DenimBlue, RectangleShape)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "GEMINI 3.5 FLASH • TUTOR",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 8.sp,
                                letterSpacing = 0.4.sp,
                                color = DenimBlue
                            )
                        }
                    }

                    // Active voice tag & switcher
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable(onClick = onOpenVoiceStudio)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.GraphicEq,
                            contentDescription = null,
                            tint = if (activeVoiceProfile.isCustomClone) MustardDark else MutedInk,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "Voice: ${activeVoiceProfile.name} • Tap to switch / clone",
                            fontFamily = SansFamily,
                            fontSize = 10.5.sp,
                            color = if (activeVoiceProfile.isCustomClone) MustardDark else MutedInk,
                            maxLines = 1
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Speech Rate Toggle Button
                Box(
                    modifier = Modifier
                        .border(
                            0.8.dp,
                            if (isSpeakingSlowly) MustardDark else HairlineSubtle,
                            RectangleShape
                        )
                        .background(if (isSpeakingSlowly) MustardDark.copy(alpha = 0.12f) else PaperCardBg)
                        .clickable(onClick = onToggleSlowSpeech)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("toggle_slow_speech_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Speed,
                            contentDescription = "Slow Speech Toggle",
                            tint = if (isSpeakingSlowly) MustardDark else MutedInk,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (isSpeakingSlowly) "Slow (0.7x)" else "Normal Speed",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp,
                            color = if (isSpeakingSlowly) MustardDark else MutedInk
                        )
                    }
                }

                IconButton(
                    onClick = onResetConversation,
                    modifier = Modifier
                        .size(30.dp)
                        .testTag("reset_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "New Conversation",
                        tint = NearBlackInk,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineRule)

        // 2. Practice Scenarios Selector Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PaperCardBg)
                .padding(vertical = 5.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item { Spacer(modifier = Modifier.width(10.dp)) }
                items(PracticeScenario.values()) { scenario ->
                    val isSelected = scenario == selectedScenario
                    Box(
                        modifier = Modifier
                            .border(
                                width = if (isSelected) 1.2.dp else 0.8.dp,
                                color = if (isSelected) BrickRed else HairlineSubtle,
                                shape = RectangleShape
                            )
                            .background(if (isSelected) BrickRed.copy(alpha = 0.08f) else WarmPaperCream)
                            .clickable { onSelectScenario(scenario) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("scenario_tab_${scenario.name}")
                    ) {
                        Text(
                            text = scenario.title,
                            fontFamily = SansFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp,
                            color = if (isSelected) BrickRed else NearBlackInk
                        )
                    }
                }
                item { Spacer(modifier = Modifier.width(10.dp)) }
            }

            Text(
                text = "Focus: ${selectedScenario.description}",
                fontFamily = SansFamily,
                fontSize = 10.5.sp,
                color = MutedInk,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 3.dp)
            )
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineSubtle)

        // 3. Quick Starter Prompts for Active Scenario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(WarmPaperCream)
                .padding(vertical = 6.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.width(8.dp)) }
                items(starterTopics) { topic ->
                    Box(
                        modifier = Modifier
                            .border(1.dp, HairlineSubtle, RectangleShape)
                            .background(PaperCardBg)
                            .clickable { onSendMessage(topic) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("quick_topic_${topic.take(10)}")
                    ) {
                        Text(
                            text = topic,
                            fontFamily = SansFamily,
                            fontSize = 11.5.sp,
                            color = NearBlackInk
                        )
                    }
                }
                item { Spacer(modifier = Modifier.width(8.dp)) }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineSubtle)

        // 4. Chat Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("chat_messages_list"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                ChatMessageBubble(
                    message = message,
                    tutorName = edition.tutorName,
                    activeVoiceName = activeVoiceProfile.name,
                    isSpeakingSlowly = isSpeakingSlowly,
                    onSpeak = { slowly -> onSpeakText(message.text, slowly) },
                    onSuggestedPromptClick = { onSendMessage(it) }
                )
            }

            if (isTutorThinking) {
                item {
                    TutorThinkingIndicator(tutorName = edition.tutorName)
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = HairlineRule)

        // 5. Input Field Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(WarmPaperCream)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .background(White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                if (inputText.isEmpty()) {
                    Text(
                        text = if (isCanadian) "Practice in English or ask about Canadian idioms..." else "Practice in English or ask about everyday phrases...",
                        fontFamily = SansFamily,
                        fontSize = 13.5.sp,
                        color = MutedInk
                    )
                }

                BasicTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    textStyle = TextStyle(
                        fontFamily = SansFamily,
                        fontSize = 14.sp,
                        color = NearBlackInk
                    ),
                    cursorBrush = SolidColor(BrickRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chat_input_field")
                )
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(if (inputText.isNotBlank()) BrickRed else HairlineSubtle)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .clickable(enabled = inputText.isNotBlank()) {
                        val text = inputText
                        inputText = ""
                        onSendMessage(text)
                    }
                    .testTag("chat_send_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Message",
                    tint = if (inputText.isNotBlank()) White else MutedInk,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    tutorName: String,
    activeVoiceName: String,
    isSpeakingSlowly: Boolean = false,
    onSpeak: (Boolean) -> Unit,
    onSuggestedPromptClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (message.isUser) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 285.dp)
                    .background(DenimBlue)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .padding(horizontal = 12.dp, vertical = 9.dp)
                    .testTag("user_message_bubble")
            ) {
                Column {
                    Text(
                        text = message.text,
                        fontFamily = SansFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp,
                        color = White
                    )
                    if (message.timestamp.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = message.timestamp,
                            fontFamily = SansFamily,
                            fontSize = 9.5.sp,
                            color = White.copy(alpha = 0.7f),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .background(PaperCardBg)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("tutor_message_bubble")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${tutorName.uppercase()} • TUTOR",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.4.sp,
                            color = BrickRed
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Listen Slow Button
                            IconButton(
                                onClick = { onSpeak(true) },
                                modifier = Modifier
                                    .size(26.dp)
                                    .testTag("speak_slow_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Speed,
                                    contentDescription = "Listen Slowly",
                                    tint = MustardDark,
                                    modifier = Modifier.size(15.dp)
                                )
                            }

                            // Listen Normal Button
                            IconButton(
                                onClick = { onSpeak(false) },
                                modifier = Modifier
                                    .size(26.dp)
                                    .testTag("speak_normal_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                    contentDescription = "Listen to pronunciation",
                                    tint = NearBlackInk,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = message.text,
                        fontFamily = SansFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 19.5.sp,
                        color = NearBlackInk
                    )

                    // Optional Native Speaker Tip Callout
                    if (!message.languageTip.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(WarmPaperCream)
                                .border(0.8.dp, MustardDark.copy(alpha = 0.5f), RectangleShape)
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("language_tip_callout")
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lightbulb,
                                    contentDescription = null,
                                    tint = MustardDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Column {
                                    Text(
                                        text = "NATIVE SPEAKER TIP",
                                        fontFamily = SansFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.5.sp,
                                        letterSpacing = 0.5.sp,
                                        color = MustardDark
                                    )
                                    Text(
                                        text = message.languageTip,
                                        fontFamily = SansFamily,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        color = NearBlackInk
                                    )
                                }
                            }
                        }
                    }

                    // Optional Idiom Spotlight Callout
                    if (!message.idiomSpotlight.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DenimBlue.copy(alpha = 0.08f))
                                .border(0.8.dp, DenimBlue.copy(alpha = 0.4f), RectangleShape)
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("idiom_spotlight_callout")
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Psychology,
                                    contentDescription = null,
                                    tint = DenimBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                                Column {
                                    Text(
                                        text = "IDIOM SPOTLIGHT",
                                        fontFamily = SansFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.5.sp,
                                        letterSpacing = 0.5.sp,
                                        color = DenimBlue
                                    )
                                    Text(
                                        text = message.idiomSpotlight,
                                        fontFamily = SansFamily,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        color = NearBlackInk
                                    )
                                }
                            }
                        }
                    }

                    if (message.timestamp.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = message.timestamp,
                            fontFamily = SansFamily,
                            fontSize = 9.5.sp,
                            color = MutedInk,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                    if (message.suggestedFollowUps.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(thickness = 1.dp, color = HairlineSubtle)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Suggested practice topics:",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp,
                            color = MutedInk
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        message.suggestedFollowUps.forEach { prompt ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, HairlineSubtle, RectangleShape)
                                    .background(WarmPaperCream)
                                    .clickable { onSuggestedPromptClick(prompt) }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                    .testTag("suggested_prompt_${prompt.take(10)}")
                            ) {
                                Text(
                                    text = prompt,
                                    fontFamily = SansFamily,
                                    fontSize = 11.5.sp,
                                    color = DenimBlue
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TutorThinkingIndicator(
    tutorName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(PaperCardBg)
                .border(1.dp, NearBlackInk, RectangleShape)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("tutor_thinking_indicator")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 1.5.dp,
                    color = BrickRed
                )
                Text(
                    text = "$tutorName is crafting an English response...",
                    fontFamily = SansFamily,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 12.sp,
                    color = MutedInk
                )
            }
        }
    }
}

package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SafetyCheck
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RefinerAction
import com.example.data.RefinerResult
import com.example.data.RefinerTone
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
import com.example.ui.theme.PaperDarker
import com.example.ui.theme.SansFamily
import com.example.ui.theme.SerifFamily
import com.example.ui.theme.WarmPaperCream
import com.example.ui.theme.White

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConversationRefinerView(
    edition: CountryEdition,
    inputText: String,
    refinedResult: RefinerResult?,
    isRefining: Boolean,
    selectedTone: RefinerTone,
    selectedAction: RefinerAction,
    errorMessage: String?,
    undoStack: List<String>,
    onInputTextChanged: (String) -> Unit,
    onToneSelected: (RefinerTone) -> Unit,
    onActionSelected: (RefinerAction) -> Unit,
    onRefineClicked: () -> Unit,
    onUndoClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onRefinedTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var copiedToastVisible by remember { mutableStateOf(false) }

    val sampleInputs = listOf(
        "Work Email" to "I am writing to inform you that I will be unable to attend the morning standup meeting due to a scheduling conflict. Please be advised that the presentation slides have been updated.",
        "Text to Friend" to "Hello Alex. I am inquiring if you are still willing to accompany me to the coffee shop at 2:00 PM today as previously discussed.",
        "Multi-turn Thread" to "Person A: I am concerned about the project deadline.\nPerson B: It is imperative that we work overtime to complete all deliverables.",
        "Apology Message" to "I am writing to formally apologize for my late response to your previous inquiry. I was occupied with urgent matters."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmPaperCream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("conversation_refiner_module")
    ) {
        // Module Banner Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, NearBlackInk, RectangleShape)
                .background(PaperCardBg)
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CONVERSATION REFINER ENGINE • GEMINI AI",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.5.sp,
                        letterSpacing = 0.5.sp,
                        color = BrickRed
                    )
                    Box(
                        modifier = Modifier
                            .background(DenimBlue)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "HUMAN NATURAL SPEECH",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                            color = White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Conversation Refiner & Polisher",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Transform rough, robotic, formal, or unclear text into smooth, human-sounding conversations while preserving facts, intent, and speaker structure.",
                    fontFamily = SansFamily,
                    fontSize = 12.5.sp,
                    lineHeight = 17.5.sp,
                    color = MutedInk
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION: Input Text Card & Quick Preset Chips
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, NearBlackInk, RectangleShape)
                .background(PaperCardBg)
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1. ENTER OR PASTE CONVERSATION / MESSAGE",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        color = DenimBlue
                    )
                    Text(
                        text = "${inputText.length} chars",
                        fontFamily = SansFamily,
                        fontSize = 10.sp,
                        color = MutedInk
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input Text Field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .background(White)
                        .padding(10.dp)
                ) {
                    if (inputText.isEmpty()) {
                        Text(
                            text = "Paste a draft email, text message, or chat thread here (e.g. 'Alex: Hey... / Sam: Sure...')...",
                            fontFamily = SansFamily,
                            fontSize = 12.5.sp,
                            color = MutedInk.copy(alpha = 0.6f)
                        )
                    }
                    BasicTextField(
                        value = inputText,
                        onValueChange = onInputTextChanged,
                        textStyle = TextStyle(
                            fontFamily = SansFamily,
                            fontSize = 13.5.sp,
                            color = NearBlackInk,
                            lineHeight = 18.5.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("refiner_input_text_field")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Sample Presets
                Text(
                    text = "Quick Sample Inputs:",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(4.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    sampleInputs.forEach { (label, content) ->
                        Box(
                            modifier = Modifier
                                .border(0.8.dp, HairlineRule, RectangleShape)
                                .background(WarmPaperCream)
                                .clickable { onInputTextChanged(content) }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                .testTag("sample_preset_$label")
                        ) {
                            Text(
                                text = "+ $label",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = DenimBlue
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION: Tone & Action Options Selection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, NearBlackInk, RectangleShape)
                .background(PaperCardBg)
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "2. CHOOSE TONE & REFINEMENT ACTION",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp,
                    color = DenimBlue
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tone Selector
                Text(
                    text = "Target Tone:",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RefinerTone.values().forEach { tone ->
                        val isSelected = tone == selectedTone
                        Box(
                            modifier = Modifier
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.8.dp,
                                    color = if (isSelected) BrickRed else HairlineSubtle,
                                    shape = RectangleShape
                                )
                                .background(if (isSelected) BrickRed else WarmPaperCream)
                                .clickable { onToneSelected(tone) }
                                .padding(horizontal = 9.dp, vertical = 6.dp)
                                .testTag("tone_chip_${tone.name}")
                        ) {
                            Text(
                                text = tone.displayName,
                                fontFamily = SansFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.5.sp,
                                color = if (isSelected) White else NearBlackInk
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Selector
                Text(
                    text = "Refinement Goal:",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RefinerAction.values().forEach { action ->
                        val isSelected = action == selectedAction
                        Box(
                            modifier = Modifier
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.8.dp,
                                    color = if (isSelected) DenimBlue else HairlineSubtle,
                                    shape = RectangleShape
                                )
                                .background(if (isSelected) DenimBlue else WarmPaperCream)
                                .clickable { onActionSelected(action) }
                                .padding(horizontal = 9.dp, vertical = 6.dp)
                                .testTag("action_chip_${action.name}")
                        ) {
                            Text(
                                text = action.displayName,
                                fontFamily = SansFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.5.sp,
                                color = if (isSelected) White else NearBlackInk
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Refine Action CTA Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (inputText.isBlank()) MutedInk else BrickRed)
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .clickable(enabled = inputText.isNotBlank() && !isRefining) {
                            onRefineClicked()
                        }
                        .padding(vertical = 12.dp)
                        .testTag("refine_conversation_submit_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isRefining) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = if (isRefining) "Refining Conversation..." else "Refine Conversation (${selectedTone.displayName})",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error message if any
        errorMessage?.let { err ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BrickRed, RectangleShape)
                    .background(BrickRed.copy(alpha = 0.1f))
                    .padding(10.dp)
            ) {
                Text(
                    text = err,
                    fontFamily = SansFamily,
                    fontSize = 12.sp,
                    color = BrickRed
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // SECTION: Refined Output Preview & Actions
        refinedResult?.let { res ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, BrickRed, RectangleShape)
                    .background(PaperCardBg)
                    .padding(14.dp)
                    .testTag("refined_output_container")
            ) {
                Column {
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
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = BrickRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "REFINED OUTPUT PREVIEW",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp,
                                color = BrickRed
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Undo Button
                            if (undoStack.isNotEmpty()) {
                                IconButton(
                                    onClick = onUndoClicked,
                                    modifier = Modifier.size(30.dp).testTag("refiner_undo_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Undo,
                                        contentDescription = "Undo refinement",
                                        tint = DenimBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            // Regenerate Button
                            IconButton(
                                onClick = onRefineClicked,
                                modifier = Modifier.size(30.dp).testTag("refiner_regenerate_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Regenerate",
                                    tint = NearBlackInk,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Editable Refined Text Field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NearBlackInk, RectangleShape)
                            .background(WarmPaperCream)
                            .padding(12.dp)
                    ) {
                        BasicTextField(
                            value = res.refinedText,
                            onValueChange = onRefineClicked.run { { newText -> onRefinedTextChanged(newText) } },
                            textStyle = TextStyle(
                                fontFamily = SerifFamily,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = NearBlackInk,
                                lineHeight = 20.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("refined_text_output_field")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Copy to Clipboard Action
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DenimBlue)
                            .border(1.dp, NearBlackInk, RectangleShape)
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Refined Conversation", res.refinedText)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied refined text to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 10.dp)
                            .testTag("copy_refined_text_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Copy Refined Text to Clipboard",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Breakdown of Changes Made
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, HairlineRule, RectangleShape)
                            .background(PaperDarker.copy(alpha = 0.3f))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "WHAT WAS REFINED (${res.toneUsed} • ${res.actionUsed}):",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = NearBlackInk
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            res.changesMade.forEach { changeItem ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "• ",
                                        fontFamily = SansFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        color = BrickRed
                                    )
                                    Text(
                                        text = changeItem,
                                        fontFamily = SansFamily,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        color = MutedInk
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // SECTION: Safeguards, Privacy & Data Protection Controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, HairlineRule, RectangleShape)
                .background(PaperCardBg)
                .padding(12.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = DenimBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "SAFEGUARDS & PRIVACY POLICY DISCLOSURE",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = DenimBlue
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "• Fact Preservation Guarantee: The refiner strictly preserves dates, commitments, names, and facts. No false claims or artificial experiences are added.\n" +
                        "• Data Protection: Your input conversations are processed securely and never retained or sold for ad training.\n" +
                        "• Responsible Use: Please review generated output before sending to avoid unintentional misunderstandings.",
                    fontFamily = SansFamily,
                    fontSize = 11.sp,
                    lineHeight = 15.5.sp,
                    color = MutedInk
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .border(0.8.dp, HairlineRule, RectangleShape)
                            .background(WarmPaperCream)
                            .clickable(onClick = onClearClicked)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("clear_refiner_session_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = BrickRed,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Clear Refiner Session",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = BrickRed
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.AccentStyle
import com.example.model.CALIBRATION_SENTENCES
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
import com.example.ui.theme.PaperDarker
import com.example.ui.theme.SansFamily
import com.example.ui.theme.SerifFamily
import com.example.ui.theme.WarmPaperCream
import com.example.ui.theme.White
import com.example.viewmodel.CloningStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCloneStudioDialog(
    show: Boolean,
    cloningStep: CloningStep,
    availableVoices: List<VoiceProfile>,
    activeVoiceProfile: VoiceProfile,
    currentSentenceIndex: Int,
    isRecordingVoice: Boolean,
    liveAmplitude: Float,
    waveformSamples: List<Float>,
    analyzedPitchHz: Float,
    draftCloneName: String,
    draftPitch: Float,
    draftSpeed: Float,
    draftStyle: AccentStyle,
    onDismiss: () -> Unit,
    onSelectVoice: (VoiceProfile) -> Unit,
    onStartCloning: () -> Unit,
    onStartRecording: () -> Unit,
    onStopAndNextRecording: () -> Unit,
    onUpdateDraftName: (String) -> Unit,
    onUpdateDraftPitch: (Float) -> Unit,
    onUpdateDraftSpeed: (Float) -> Unit,
    onUpdateDraftStyle: (AccentStyle) -> Unit,
    onPreviewDraft: () -> Unit,
    onSaveCustomClone: () -> Unit,
    onRenameCustomVoice: (String, String) -> Unit,
    onDeleteCustomVoice: (String) -> Unit,
    onTestVoicePhrase: (VoiceProfile, String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!show) return

    val context = LocalContext.current
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (isGranted) {
            onStartRecording()
        }
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(WarmPaperCream)
            .testTag("voice_clone_studio_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "VOICE ACOUSTIC STUDIO • PERSISTENT ROOM STORE",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        color = BrickRed
                    )
                    Text(
                        text = "Voice Cloner & Accent Profiles",
                        fontFamily = SerifFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = NearBlackInk
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp).testTag("voice_studio_close_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NearBlackInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp, color = HairlineRule)
            Spacer(modifier = Modifier.height(10.dp))

            // Wizard Step Content
            when (cloningStep) {
                CloningStep.INTRO -> {
                    VoiceStudioProfilesOverview(
                        availableVoices = availableVoices,
                        activeVoiceProfile = activeVoiceProfile,
                        onSelectVoice = onSelectVoice,
                        onStartCloning = onStartCloning,
                        onRenameCustomVoice = onRenameCustomVoice,
                        onDeleteCustomVoice = onDeleteCustomVoice,
                        onTestVoicePhrase = onTestVoicePhrase
                    )
                }

                CloningStep.RECORDING -> {
                    VoiceRecordingStep(
                        currentSentenceIndex = currentSentenceIndex,
                        isRecording = isRecordingVoice,
                        liveAmplitude = liveAmplitude,
                        waveformSamples = waveformSamples,
                        hasPermission = hasAudioPermission,
                        onRequestPermissionAndRecord = {
                            if (hasAudioPermission) {
                                onStartRecording()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        onStopAndNext = onStopAndNextRecording
                    )
                }

                CloningStep.ANALYZING -> {
                    VoiceAnalyzingStep()
                }

                CloningStep.CALIBRATION_COMPLETE -> {
                    VoiceCalibrationFineTuneStep(
                        analyzedPitchHz = analyzedPitchHz,
                        draftName = draftCloneName,
                        draftPitch = draftPitch,
                        draftSpeed = draftSpeed,
                        draftStyle = draftStyle,
                        onUpdateDraftName = onUpdateDraftName,
                        onUpdateDraftPitch = onUpdateDraftPitch,
                        onUpdateDraftSpeed = onUpdateDraftSpeed,
                        onUpdateDraftStyle = onUpdateDraftStyle,
                        onPreviewDraft = onPreviewDraft,
                        onSaveCustomClone = onSaveCustomClone
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceStudioProfilesOverview(
    availableVoices: List<VoiceProfile>,
    activeVoiceProfile: VoiceProfile,
    onSelectVoice: (VoiceProfile) -> Unit,
    onStartCloning: () -> Unit,
    onRenameCustomVoice: (String, String) -> Unit,
    onDeleteCustomVoice: (String) -> Unit,
    onTestVoicePhrase: (VoiceProfile, String) -> Unit
) {
    val savedClones = availableVoices.filter { it.isCustomClone }
    val presetVoices = availableVoices.filter { !it.isCustomClone }

    var editingProfile by remember { mutableStateOf<VoiceProfile?>(null) }
    var renameInputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Action: Clone Your Own Voice
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, DenimBlue, RectangleShape)
                .background(PaperCardBg)
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW VOICE CLONE • ROOM DATABASE PERSISTED",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        color = DenimBlue
                    )
                    Box(
                        modifier = Modifier
                            .background(BrickRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "FREE",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Clone Your Voice in 30 Seconds",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Record 3 everyday phrases to calibrate your acoustic frequency and save multiple personalized voice profiles in your local database.",
                    fontFamily = SansFamily,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp,
                    color = MutedInk
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrickRed)
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .clickable(onClick = onStartCloning)
                        .padding(vertical = 9.dp)
                        .testTag("start_clone_wizard_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Start Free Voice Calibration",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 1: SAVED CUSTOM CLONES
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Saved Voice Clones (${savedClones.size})",
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.5.sp,
                color = NearBlackInk
            )
            if (savedClones.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .border(0.8.dp, MustardDark, RectangleShape)
                        .background(MustardGold.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "SAVED IN ROOM DB",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp,
                        color = MustardDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (savedClones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, HairlineSubtle, RectangleShape)
                    .background(PaperDarker.copy(alpha = 0.3f))
                    .padding(12.dp)
            ) {
                Text(
                    text = "No saved voice clones yet. Tap 'Start Free Voice Calibration' above to record and name your first custom speech profile.",
                    fontFamily = SansFamily,
                    fontSize = 12.sp,
                    color = MutedInk
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                savedClones.forEach { profile ->
                    val isActive = profile.id == activeVoiceProfile.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isActive) 1.5.dp else 1.dp,
                                color = if (isActive) BrickRed else HairlineSubtle,
                                shape = RectangleShape
                            )
                            .background(if (isActive) WarmPaperCream else PaperCardBg.copy(alpha = 0.6f))
                            .clickable { onSelectVoice(profile) }
                            .padding(10.dp)
                            .testTag("voice_profile_item_${profile.id}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = profile.name,
                                        fontFamily = SerifFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NearBlackInk
                                    )
                                    if (isActive) {
                                        Box(
                                            modifier = Modifier
                                                .background(BrickRed)
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                fontFamily = SansFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 8.5.sp,
                                                color = White
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = profile.subtitle,
                                    fontFamily = SansFamily,
                                    fontSize = 11.5.sp,
                                    color = MutedInk
                                )
                                if (profile.dateCreated.isNotBlank()) {
                                    Text(
                                        text = "Saved: ${profile.dateCreated}",
                                        fontFamily = SansFamily,
                                        fontSize = 10.sp,
                                        color = DenimBlue
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                // Rename button
                                IconButton(
                                    onClick = {
                                        editingProfile = profile
                                        renameInputText = profile.name
                                    },
                                    modifier = Modifier.size(28.dp).testTag("rename_voice_${profile.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = "Rename voice",
                                        tint = DenimBlue,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                // Test voice sample
                                IconButton(
                                    onClick = {
                                        val phrase = "Hey there! This is ${profile.name} speaking. Let's practice authentic daily conversation."
                                        onTestVoicePhrase(profile, phrase)
                                    },
                                    modifier = Modifier.size(28.dp).testTag("test_voice_${profile.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                        contentDescription = "Test voice",
                                        tint = NearBlackInk,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                // Delete voice
                                IconButton(
                                    onClick = { onDeleteCustomVoice(profile.id) },
                                    modifier = Modifier.size(28.dp).testTag("delete_voice_${profile.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = "Delete custom voice",
                                        tint = MutedInk,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // SECTION 2: BUILT-IN PRESETS
        Text(
            text = "Built-in Speech Presets & Dialects",
            fontFamily = SerifFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.5.sp,
            color = NearBlackInk
        )
        Spacer(modifier = Modifier.height(6.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presetVoices.forEach { profile ->
                val isActive = profile.id == activeVoiceProfile.id
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isActive) 1.5.dp else 1.dp,
                            color = if (isActive) BrickRed else HairlineSubtle,
                            shape = RectangleShape
                        )
                        .background(if (isActive) WarmPaperCream else PaperCardBg.copy(alpha = 0.6f))
                        .clickable { onSelectVoice(profile) }
                        .padding(10.dp)
                        .testTag("voice_profile_item_${profile.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = profile.name,
                                    fontFamily = SerifFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = NearBlackInk
                                )
                                if (isActive) {
                                    Box(
                                        modifier = Modifier
                                            .background(BrickRed)
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "ACTIVE",
                                            fontFamily = SansFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.5.sp,
                                            color = White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = profile.subtitle,
                                fontFamily = SansFamily,
                                fontSize = 11.5.sp,
                                color = MutedInk
                            )
                        }

                        IconButton(
                            onClick = {
                                val testPhrase = when (profile.style) {
                                    AccentStyle.NEW_YORK -> "Hey, let's grab a slice of pizza and touch base before the meeting!"
                                    AccentStyle.SOUTHERN -> "Y'all have a wonderful afternoon and enjoy the backyard barbecue!"
                                    AccentStyle.MIDWESTERN -> "Can I get a quick iced coffee at the drive-thru window?"
                                    AccentStyle.CANADIAN_STANDARD -> "Let's head up to the cottage for the weekend and grab a double-double, eh?"
                                    AccentStyle.CANADIAN_MARITIMES -> "How's she goin' b'y? Beautiful afternoon out on the coast!"
                                    AccentStyle.BROADCASTER -> "Live from the studio, here is your daily North American speech guide."
                                    else -> "Welcome to Daily American. Let's practice authentic English speech together."
                                }
                                onTestVoicePhrase(profile, testPhrase)
                            },
                            modifier = Modifier.size(28.dp).testTag("test_voice_${profile.id}")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                contentDescription = "Test voice",
                                tint = NearBlackInk,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Rename Dialog Modal
    editingProfile?.let { targetProfile ->
        BasicAlertDialog(
            onDismissRequest = { editingProfile = null },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.5.dp, NearBlackInk, RectangleShape)
                .background(WarmPaperCream)
                .testTag("rename_voice_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "RENAME VOICE PROFILE",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp,
                    color = BrickRed
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter a new name for your cloned voice:",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NearBlackInk
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NearBlackInk, RectangleShape)
                        .background(White)
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    BasicTextField(
                        value = renameInputText,
                        onValueChange = { renameInputText = it },
                        textStyle = TextStyle(
                            fontFamily = SansFamily,
                            fontSize = 14.sp,
                            color = NearBlackInk
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("rename_input_field")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, HairlineRule, RectangleShape)
                            .background(PaperDarker.copy(alpha = 0.3f))
                            .clickable { editingProfile = null }
                            .padding(vertical = 10.dp)
                            .testTag("cancel_rename_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = NearBlackInk
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(BrickRed)
                            .border(1.dp, NearBlackInk, RectangleShape)
                            .clickable {
                                if (renameInputText.isNotBlank()) {
                                    onRenameCustomVoice(targetProfile.id, renameInputText.trim())
                                }
                                editingProfile = null
                            }
                            .padding(vertical = 10.dp)
                            .testTag("save_rename_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save Name",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceRecordingStep(
    currentSentenceIndex: Int,
    isRecording: Boolean,
    liveAmplitude: Float,
    waveformSamples: List<Float>,
    hasPermission: Boolean,
    onRequestPermissionAndRecord: () -> Unit,
    onStopAndNext: () -> Unit
) {
    val currentSentence = CALIBRATION_SENTENCES.getOrElse(currentSentenceIndex) { CALIBRATION_SENTENCES[0] }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Step progression bar & studio status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "STUDIO VOICE CALIBRATION",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.5.sp,
                    letterSpacing = 0.5.sp,
                    color = BrickRed
                )
                Text(
                    text = "Phrase ${currentSentenceIndex + 1} of ${CALIBRATION_SENTENCES.size}",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NearBlackInk
                )
            }

            // Live status badge (REC vs STANDBY)
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (isRecording) BrickRed else HairlineRule,
                        shape = RectangleShape
                    )
                    .background(if (isRecording) BrickRed.copy(alpha = 0.12f) else PaperDarker.copy(alpha = 0.4f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (isRecording) BrickRed else MutedInk,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = if (isRecording) "● REC ACTIVE" else "○ STANDBY",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (isRecording) BrickRed else MutedInk
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Multi-step progress visualizer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CALIBRATION_SENTENCES.indices.forEach { index ->
                val isCompleted = index < currentSentenceIndex
                val isCurrent = index == currentSentenceIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            when {
                                isCompleted -> BrickRed
                                isCurrent -> if (isRecording) BrickRed else DenimBlue
                                else -> HairlineSubtle
                            }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Target sentence card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, NearBlackInk, RectangleShape)
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
                        text = "READ OUT LOUD:",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        color = MutedInk
                    )
                    Text(
                        text = "Take 1",
                        fontFamily = SansFamily,
                        fontSize = 10.sp,
                        color = MutedInk
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "\"${currentSentence.text}\"",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.5.sp,
                    lineHeight = 23.sp,
                    color = NearBlackInk
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.GraphicEq,
                        contentDescription = null,
                        tint = DenimBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Acoustic Target: ${currentSentence.phoneticFocus}",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.5.sp,
                        color = DenimBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Waveform Indicator Monitor
        StudioWaveformVisualizer(
            isRecording = isRecording,
            liveAmplitude = liveAmplitude,
            waveformSamples = waveformSamples
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Start / Stop Recording Action Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isRecording) {
                // START RECORDING BUTTON
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(BrickRed)
                        .border(1.2.dp, NearBlackInk, RectangleShape)
                        .clickable(onClick = onRequestPermissionAndRecord)
                        .padding(vertical = 13.dp)
                        .testTag("start_recording_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Start Recording",
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Start Recording",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = White
                        )
                    }
                }
            } else {
                // STOP RECORDING BUTTON
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(NearBlackInk)
                        .border(1.2.dp, BrickRed, RectangleShape)
                        .clickable(onClick = onStopAndNext)
                        .padding(vertical = 13.dp)
                        .testTag("stop_recording_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(BrickRed, RectangleShape)
                        )
                        Text(
                            text = if (currentSentenceIndex < CALIBRATION_SENTENCES.size - 1) "Stop & Next Phrase" else "Stop & Analyze Voice",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Helper guidance info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isRecording) "Speaking now • Tap 'Stop' when finished reading" else "Speak naturally at conversational distance • 100% On-device processing",
                fontFamily = SansFamily,
                fontSize = 11.sp,
                color = MutedInk
            )
        }
    }
}

@Composable
fun StudioWaveformVisualizer(
    isRecording: Boolean,
    liveAmplitude: Float,
    waveformSamples: List<Float>
) {
    val darkOscilloscopeBg = Color(0xFF1E201E)
    val gridColor = Color(0xFF2C322C)
    val activeBarColor = BrickRed
    val peakBarColor = MustardGold

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.2.dp, NearBlackInk, RectangleShape)
            .background(darkOscilloscopeBg)
            .padding(8.dp)
            .testTag("waveform_visualizer_monitor")
    ) {
        Column {
            // Monitor HUD status line
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "WAVEFORM MONITOR",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp,
                        color = Color(0xFF9EAA9E)
                    )
                    if (isRecording) {
                        Box(
                            modifier = Modifier
                                .background(BrickRed)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "LIVE INPUT",
                                fontFamily = SansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.sp,
                                color = White
                            )
                        }
                    }
                }

                Text(
                    text = if (isRecording) {
                        val db = (20 * kotlin.math.log10(liveAmplitude.coerceAtLeast(0.01f))).toInt()
                        "RMS: $db dB"
                    } else "CH 1 • 44.1 kHz",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 9.5.sp,
                    color = Color(0xFF8DA38D)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Waveform Canvas Screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(68.dp)) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val centerY = canvasHeight / 2f

                    // Draw Oscilloscope Grid Lines
                    // Center Zero-Axis
                    drawLine(
                        color = gridColor.copy(alpha = 0.8f),
                        start = Offset(0f, centerY),
                        end = Offset(canvasWidth, centerY),
                        strokeWidth = 1.dp.toPx()
                    )
                    // Upper and Lower amplitude reference lines (+6dB / -6dB)
                    drawLine(
                        color = gridColor.copy(alpha = 0.4f),
                        start = Offset(0f, centerY - canvasHeight * 0.35f),
                        end = Offset(canvasWidth, centerY - canvasHeight * 0.35f),
                        strokeWidth = 0.8.dp.toPx()
                    )
                    drawLine(
                        color = gridColor.copy(alpha = 0.4f),
                        start = Offset(0f, centerY + canvasHeight * 0.35f),
                        end = Offset(canvasWidth, centerY + canvasHeight * 0.35f),
                        strokeWidth = 0.8.dp.toPx()
                    )

                    if (isRecording) {
                        // Dynamic Mirrored Waveform Bars
                        val displaySamples = if (waveformSamples.isNotEmpty()) waveformSamples else listOf(liveAmplitude)
                        val barCount = 32
                        val barSpacing = 2.dp.toPx()
                        val availableWidth = canvasWidth - (barSpacing * (barCount - 1))
                        val barWidth = (availableWidth / barCount).coerceAtLeast(3.dp.toPx())

                        for (i in 0 until barCount) {
                            // Extract sample or interpolate smoothly
                            val sampleIndex = ((i.toFloat() / barCount) * displaySamples.size).toInt().coerceIn(0, (displaySamples.size - 1).coerceAtLeast(0))
                            val sampleValue = if (displaySamples.isNotEmpty()) displaySamples[sampleIndex] else 0.1f
                            // Introduce slight natural modulation for visual realism
                            val modulatedAmp = (sampleValue * 0.85f + (liveAmplitude * 0.15f)).coerceIn(0.08f, 1.0f)
                            val halfBarHeight = (modulatedAmp * centerY * 0.9f).coerceAtLeast(3.dp.toPx())

                            val x = i * (barWidth + barSpacing)
                            val topY = centerY - halfBarHeight
                            val bottomHeight = halfBarHeight * 2f

                            val isPeak = modulatedAmp > 0.65f
                            val barColor = if (isPeak) peakBarColor else activeBarColor

                            drawRect(
                                color = barColor,
                                topLeft = Offset(x, topY),
                                size = Size(barWidth, bottomHeight)
                            )
                        }
                    } else {
                        // Ambient Standby Sine Wave
                        val pointCount = 60
                        var prevX = 0f
                        var prevY = centerY
                        for (i in 0..pointCount) {
                            val x = (i.toFloat() / pointCount) * canvasWidth
                            val y = centerY + kotlin.math.sin(i * 0.3f).toFloat() * 4.dp.toPx()
                            if (i > 0) {
                                drawLine(
                                    color = Color(0xFF4A5C4A),
                                    start = Offset(prevX, prevY),
                                    end = Offset(x, y),
                                    strokeWidth = 1.5.dp.toPx()
                                )
                            }
                            prevX = x
                            prevY = y
                        }
                    }
                }

                if (!isRecording) {
                    Text(
                        text = "Tap 'Start Recording' to begin live voice capture",
                        fontFamily = SansFamily,
                        fontSize = 11.5.sp,
                        color = Color(0xFFA5B8A5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // VU Meter level bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VU METER",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.5.sp,
                    color = Color(0xFF7A8A7A)
                )

                // Segmented visual level indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val activeSegments = if (isRecording) ((liveAmplitude * 12).toInt()).coerceIn(1, 10) else 0
                    for (seg in 1..10) {
                        val isLit = seg <= activeSegments
                        val segColor = when {
                            seg <= 6 -> Color(0xFF4CAF50) // Green
                            seg <= 8 -> MustardGold       // Amber
                            else -> BrickRed              // Red Peak
                        }
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .height(5.dp)
                                .background(if (isLit) segColor else Color(0xFF2A332A), RectangleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceAnalyzingStep() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                strokeWidth = 2.5.dp,
                color = BrickRed
            )
            Text(
                text = "Analyzing Vocal Spectrum...",
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = NearBlackInk
            )
            Text(
                text = "Extracting fundamental frequency (F0), formant resonances, and conversational tempo...",
                fontFamily = SansFamily,
                fontSize = 12.sp,
                color = MutedInk,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun VoiceCalibrationFineTuneStep(
    analyzedPitchHz: Float,
    draftName: String,
    draftPitch: Float,
    draftSpeed: Float,
    draftStyle: AccentStyle,
    onUpdateDraftName: (String) -> Unit,
    onUpdateDraftPitch: (Float) -> Unit,
    onUpdateDraftSpeed: (Float) -> Unit,
    onUpdateDraftStyle: (AccentStyle) -> Unit,
    onPreviewDraft: () -> Unit,
    onSaveCustomClone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Calibration Summary Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MustardDark, RectangleShape)
                .background(MustardGold.copy(alpha = 0.15f))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "VOICE SIGNATURE EXTRACTED",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.5.sp,
                        letterSpacing = 0.5.sp,
                        color = MustardDark
                    )
                    Text(
                        text = "Fundamental Pitch: ${analyzedPitchHz.toInt()} Hz",
                        fontFamily = SerifFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NearBlackInk
                    )
                }
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MustardDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Clone Profile Name Input
        Text(
            text = "Voice Profile Name (Saved to Room DB):",
            fontFamily = SansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = NearBlackInk
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NearBlackInk, RectangleShape)
                .background(White)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            BasicTextField(
                value = draftName,
                onValueChange = onUpdateDraftName,
                textStyle = TextStyle(
                    fontFamily = SansFamily,
                    fontSize = 13.5.sp,
                    color = NearBlackInk
                ),
                modifier = Modifier.fillMaxWidth().testTag("clone_name_input")
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Quick name suggestions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val suggestions = listOf("Daily American", "Canadian Slang", "Morning Coffee", "Office Pitch")
            suggestions.forEach { suggestion ->
                Box(
                    modifier = Modifier
                        .border(0.8.dp, HairlineRule, RectangleShape)
                        .background(PaperDarker.copy(alpha = 0.3f))
                        .clickable { onUpdateDraftName("My $suggestion Voice") }
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = suggestion,
                        fontFamily = SansFamily,
                        fontSize = 10.sp,
                        color = NearBlackInk
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pitch Shift Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Voice Pitch Tuning",
                fontFamily = SansFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = NearBlackInk
            )
            Text(
                text = "${String.format("%.2f", draftPitch)}x",
                fontFamily = SansFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = DenimBlue
            )
        }
        Slider(
            value = draftPitch,
            onValueChange = onUpdateDraftPitch,
            valueRange = 0.6f..1.5f,
            colors = SliderDefaults.colors(
                thumbColor = BrickRed,
                activeTrackColor = BrickRed,
                inactiveTrackColor = HairlineSubtle
            ),
            modifier = Modifier.testTag("pitch_slider")
        )

        // Cadence / Speed Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Speech Cadence (Speed)",
                fontFamily = SansFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = NearBlackInk
            )
            Text(
                text = "${String.format("%.2f", draftSpeed)}x",
                fontFamily = SansFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = DenimBlue
            )
        }
        Slider(
            value = draftSpeed,
            onValueChange = onUpdateDraftSpeed,
            valueRange = 0.7f..1.4f,
            colors = SliderDefaults.colors(
                thumbColor = BrickRed,
                activeTrackColor = BrickRed,
                inactiveTrackColor = HairlineSubtle
            ),
            modifier = Modifier.testTag("speed_slider")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Preview & Save Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Preview Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, DenimBlue, RectangleShape)
                    .background(WarmPaperCream)
                    .clickable(onClick = onPreviewDraft)
                    .padding(vertical = 11.dp)
                    .testTag("preview_clone_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                        contentDescription = "Test",
                        tint = DenimBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Hear Preview",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DenimBlue
                    )
                }
            }

            // Save & Activate Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(BrickRed)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .clickable(onClick = onSaveCustomClone)
                    .padding(vertical = 11.dp)
                    .testTag("save_and_activate_clone_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save & Activate",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = White
                )
            }
        }
    }
}

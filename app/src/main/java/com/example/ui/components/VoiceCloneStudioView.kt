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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsVoice
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Tune
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
import com.example.model.CountryEdition
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

/**
 * Dedicated Full-Module Voice Cloning UI for recording voice samples,
 * calibrating acoustic frequency parameters, and integrating with the speech processing workflow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCloneStudioView(
    edition: CountryEdition,
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
    onSelectVoice: (VoiceProfile) -> Unit,
    onStartCloning: () -> Unit,
    onStartRecording: () -> Unit,
    onStopAndNextRecording: () -> Unit,
    onUpdateDraftName: (String) -> Unit,
    onUpdateDraftPitch: (Float) -> Unit,
    onUpdateDraftSpeed: (Float) -> Unit,
    onUpdateDraftStyle: (AccentStyle) -> Unit,
    onPreviewDraft: (String?) -> Unit,
    onSaveCustomClone: () -> Unit,
    onRenameCustomVoice: (String, String) -> Unit,
    onDeleteCustomVoice: (String) -> Unit,
    onTestVoicePhrase: (VoiceProfile, String) -> Unit,
    modifier: Modifier = Modifier
) {
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

    var customTestText by remember {
        mutableStateOf("Good morning! This is my personalized cloned voice reading authentic North American speech.")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmPaperCream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("voice_clone_studio_module")
    ) {
        // Module Masthead Banner
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
                        text = "ACOUSTIC PROCESSING WORKFLOW • ON-DEVICE ENGINE",
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
                            text = "ROOM DB PERSISTED",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                            color = White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Custom Voice Cloning Studio",
                    fontFamily = SerifFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NearBlackInk
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Record raw voice samples through your microphone to extract acoustic pitch and cadence, synthesize custom speech profiles, and power your AI Tutor and daily idiom pronunciations.",
                    fontFamily = SansFamily,
                    fontSize = 12.5.sp,
                    lineHeight = 17.5.sp,
                    color = MutedInk
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Active Voice Pipeline Indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DenimBlue, RectangleShape)
                .background(PaperDarker.copy(alpha = 0.25f))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(DenimBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.RecordVoiceOver,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "ACTIVE SPEECH PIPELINE VOICE",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = DenimBlue
                        )
                        Text(
                            text = activeVoiceProfile.name,
                            fontFamily = SerifFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NearBlackInk
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(BrickRed)
                        .clickable {
                            val phrase = "Testing active voice profile: ${activeVoiceProfile.name}. All systems calibrated."
                            onTestVoicePhrase(activeVoiceProfile, phrase)
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("test_active_pipeline_voice_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Audition",
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION: Voice Recording & Calibration Wizard Step Container
        when (cloningStep) {
            CloningStep.INTRO -> {
                VoiceClonerHeroWizardCard(
                    onStartCloning = onStartCloning
                )
            }

            CloningStep.RECORDING -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, NearBlackInk, RectangleShape)
                        .background(PaperCardBg)
                        .padding(14.dp)
                ) {
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
            }

            CloningStep.ANALYZING -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, NearBlackInk, RectangleShape)
                        .background(PaperCardBg)
                        .padding(14.dp)
                ) {
                    VoiceAnalyzingStep()
                }
            }

            CloningStep.CALIBRATION_COMPLETE -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, NearBlackInk, RectangleShape)
                        .background(PaperCardBg)
                        .padding(14.dp)
                ) {
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
                        onPreviewDraft = { onPreviewDraft(null) },
                        onSaveCustomClone = onSaveCustomClone
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION: Live Speech Processing Test Bench
        SpeechProcessingTestBench(
            customText = customTestText,
            activeVoiceProfile = activeVoiceProfile,
            onTextChanged = { customTestText = it },
            onSynthesize = { text ->
                onTestVoicePhrase(activeVoiceProfile, text)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION: Saved Profiles Vault & Presets
        VoiceProfilesVaultSection(
            availableVoices = availableVoices,
            activeVoiceProfile = activeVoiceProfile,
            onSelectVoice = onSelectVoice,
            onRenameCustomVoice = onRenameCustomVoice,
            onDeleteCustomVoice = onDeleteCustomVoice,
            onTestVoicePhrase = onTestVoicePhrase
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun VoiceClonerHeroWizardCard(
    onStartCloning: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, DenimBlue, RectangleShape)
            .background(PaperCardBg)
            .padding(14.dp)
            .testTag("voice_cloner_hero_wizard_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STEP 1: VOICE SAMPLE COLLECTION",
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
                        text = "CALIBRATION READY",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp,
                        color = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Record & Clone Your Voice",
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "The system captures 3 distinct phonetic calibration sentences via your microphone to map your fundamental pitch (F0), formant envelope, and speaking cadence.",
                fontFamily = SansFamily,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MutedInk
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Step summary badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("1. Record 3 Samples", "2. Acoustic Analysis", "3. Save & Synthesize").forEach { stepLabel ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(0.8.dp, HairlineRule, RectangleShape)
                            .background(WarmPaperCream)
                            .padding(vertical = 6.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stepLabel,
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.5.sp,
                            color = NearBlackInk,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrickRed)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .clickable(onClick = onStartCloning)
                    .padding(vertical = 11.dp)
                    .testTag("start_calibration_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Begin Voice Recording Calibration",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = White
                    )
                }
            }
        }
    }
}

@Composable
fun SpeechProcessingTestBench(
    customText: String,
    activeVoiceProfile: VoiceProfile,
    onTextChanged: (String) -> Unit,
    onSynthesize: (String) -> Unit
) {
    val quickPhrases = listOf(
        "Can I get an iced coffee and a breakfast sandwich to-go?",
        "Let's touch base after lunch and circle back on the presentation.",
        "Traffic was bumper-to-bumper on the freeway this morning.",
        "How's she goin' b'y? Beautiful afternoon up at the cottage!"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, NearBlackInk, RectangleShape)
            .background(PaperCardBg)
            .padding(14.dp)
            .testTag("speech_processing_test_bench")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SPEECH PROCESSING TEST BENCH",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp,
                    color = DenimBlue
                )
                Text(
                    text = "SYNTHESIS ENGINE",
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = MutedInk
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Live Acoustic Synthesis Studio",
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = NearBlackInk
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Type any English phrase or select a daily idiom to synthesize using your active voice profile (${activeVoiceProfile.name}):",
                fontFamily = SansFamily,
                fontSize = 12.sp,
                color = MutedInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Text input box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .background(White)
                    .padding(10.dp)
            ) {
                BasicTextField(
                    value = customText,
                    onValueChange = onTextChanged,
                    textStyle = TextStyle(
                        fontFamily = SansFamily,
                        fontSize = 13.5.sp,
                        color = NearBlackInk,
                        lineHeight = 18.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("speech_test_input_field")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick preset chips
            Text(
                text = "Quick Sample Prompts:",
                fontFamily = SansFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = MutedInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                quickPhrases.forEach { phrase ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.8.dp, HairlineSubtle, RectangleShape)
                            .background(WarmPaperCream)
                            .clickable { onTextChanged(phrase) }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "“$phrase”",
                            fontFamily = SerifFamily,
                            fontSize = 11.5.sp,
                            color = NearBlackInk,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Synthesize Audio Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DenimBlue)
                    .border(1.dp, NearBlackInk, RectangleShape)
                    .clickable {
                        if (customText.isNotBlank()) {
                            onSynthesize(customText.trim())
                        }
                    }
                    .padding(vertical = 10.dp)
                    .testTag("synthesize_audio_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Synthesize",
                        tint = White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Synthesize Voice Audio (${activeVoiceProfile.name})",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceProfilesVaultSection(
    availableVoices: List<VoiceProfile>,
    activeVoiceProfile: VoiceProfile,
    onSelectVoice: (VoiceProfile) -> Unit,
    onRenameCustomVoice: (String, String) -> Unit,
    onDeleteCustomVoice: (String) -> Unit,
    onTestVoicePhrase: (VoiceProfile, String) -> Unit
) {
    val savedClones = availableVoices.filter { it.isCustomClone }
    val presetVoices = availableVoices.filter { !it.isCustomClone }

    var editingProfile by remember { mutableStateOf<VoiceProfile?>(null) }
    var renameInputText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved Voice Clones in Room Database (${savedClones.size})",
                fontFamily = SerifFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
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
                        text = "OFFLINE VAULT",
                        fontFamily = SansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp,
                        color = MustardDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (savedClones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, HairlineSubtle, RectangleShape)
                    .background(PaperDarker.copy(alpha = 0.3f))
                    .padding(14.dp)
            ) {
                Text(
                    text = "No custom voice profiles saved yet. Use the recording calibration above to add your personalized cloned voices.",
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
                            .background(if (isActive) WarmPaperCream else PaperCardBg)
                            .clickable { onSelectVoice(profile) }
                            .padding(12.dp)
                            .testTag("vault_profile_item_${profile.id}")
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
                                        fontSize = 14.5.sp,
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
                                    text = "${profile.subtitle} • Pitch: ${String.format("%.2f", profile.pitch)}x • Tempo: ${String.format("%.2f", profile.speechRate)}x",
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
                                    modifier = Modifier.size(28.dp).testTag("vault_rename_${profile.id}")
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
                                        val phrase = "Hello! This is ${profile.name} speaking with calibrated speech rate and pitch."
                                        onTestVoicePhrase(profile, phrase)
                                    },
                                    modifier = Modifier.size(28.dp).testTag("vault_test_${profile.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                        contentDescription = "Audition voice",
                                        tint = NearBlackInk,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                // Delete voice
                                IconButton(
                                    onClick = { onDeleteCustomVoice(profile.id) },
                                    modifier = Modifier.size(28.dp).testTag("vault_delete_${profile.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = "Delete voice",
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

        // Preset Voices Section
        Text(
            text = "Standard Dialect & Accent Presets",
            fontFamily = SerifFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = NearBlackInk
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                        .background(if (isActive) WarmPaperCream else PaperCardBg)
                        .clickable { onSelectVoice(profile) }
                        .padding(12.dp)
                        .testTag("preset_profile_${profile.id}")
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
                            modifier = Modifier.size(28.dp).testTag("preset_test_${profile.id}")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                contentDescription = "Audition voice",
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
                .testTag("vault_rename_dialog")
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

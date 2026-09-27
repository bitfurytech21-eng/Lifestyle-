package com.example.data

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sqrt

data class AudioAnalysisResult(
    val averagePitchHz: Float,
    val averageRmsDb: Float,
    val recommendedPitchMultiplier: Float,
    val recommendedRateMultiplier: Float,
    val audioWavPath: String
)

class VoiceRecorderHelper(private val context: Context) {

    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat).coerceAtLeast(4096)

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _amplitudeFlow = MutableStateFlow(0f)
    val amplitudeFlow: StateFlow<Float> = _amplitudeFlow.asStateFlow()

    private val _waveformSamples = MutableStateFlow<List<Float>>(emptyList())
    val waveformSamples: StateFlow<List<Float>> = _waveformSamples.asStateFlow()

    private var currentOutputFile: File? = null

    @SuppressLint("MissingPermission")
    fun startRecording(sentenceIndex: Int, scope: CoroutineScope): Boolean {
        try {
            stopRecording()

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                Log.e("VoiceRecorder", "AudioRecord initialization failed")
                return false
            }

            val pcmFile = File(context.cacheDir, "voice_clone_sample_$sentenceIndex.pcm")
            val wavFile = File(context.cacheDir, "voice_clone_sample_$sentenceIndex.wav")
            currentOutputFile = wavFile

            audioRecord?.startRecording()
            _isRecording.value = true

            recordingJob = scope.launch(Dispatchers.IO) {
                val outputStream = FileOutputStream(pcmFile)
                val buffer = ShortArray(bufferSize / 2)
                val waveformAccumulator = mutableListOf<Float>()
                var totalSamplesRead = 0

                try {
                    while (isActive && _isRecording.value) {
                        val readShorts = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                        if (readShorts > 0) {
                            // Write raw PCM bytes
                            val byteBuffer = ByteBuffer.allocate(readShorts * 2).order(ByteOrder.LITTLE_ENDIAN)
                            for (i in 0 until readShorts) {
                                byteBuffer.putShort(buffer[i])
                            }
                            outputStream.write(byteBuffer.array())
                            totalSamplesRead += readShorts

                            // Compute RMS amplitude
                            var sumSquares = 0.0
                            for (i in 0 until readShorts) {
                                val s = buffer[i].toDouble()
                                sumSquares += s * s
                            }
                            val rms = sqrt(sumSquares / readShorts)
                            val normalizedAmp = (rms / 32767.0).toFloat().coerceIn(0f, 1f)
                            _amplitudeFlow.value = normalizedAmp

                            // Keep a rolling window of recent amplitude samples for UI
                            waveformAccumulator.add(normalizedAmp)
                            if (waveformAccumulator.size > 40) {
                                waveformAccumulator.removeAt(0)
                            }
                            _waveformSamples.value = waveformAccumulator.toList()
                        }
                    }
                } finally {
                    outputStream.close()
                    // Convert raw PCM to WAV
                    if (pcmFile.exists()) {
                        rawToWave(pcmFile, wavFile, sampleRate, 1, 16)
                        pcmFile.delete()
                    }
                }
            }

            return true
        } catch (e: Exception) {
            Log.e("VoiceRecorder", "Error starting record", e)
            _isRecording.value = false
            return false
        }
    }

    fun stopRecording(): File? {
        _isRecording.value = false
        recordingJob?.cancel()
        recordingJob = null

        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (e: Exception) {
            Log.e("VoiceRecorder", "Error stopping record", e)
        } finally {
            audioRecord = null
            _amplitudeFlow.value = 0f
        }

        return currentOutputFile
    }

    /**
     * Acoustic Pitch and Frequency Analysis:
     * Analyzes recorded audio to estimate fundamental vocal frequency (F0 in Hz)
     * and maps it to a custom pitch & pace multiplier.
     */
    fun analyzeRecordedAudio(wavFile: File): AudioAnalysisResult {
        var pitchHz = 160f // Default human speech frequency baseline (Hz)
        var totalEnergy = 0.0
        var sampleCount = 0

        try {
            if (wavFile.exists() && wavFile.length() > 44) {
                val bytes = wavFile.readBytes()
                val shortCount = (bytes.size - 44) / 2
                val shorts = ShortArray(shortCount)
                val buffer = ByteBuffer.wrap(bytes, 44, bytes.size - 44).order(ByteOrder.LITTLE_ENDIAN)
                for (i in 0 until shortCount) {
                    shorts[i] = buffer.short
                }

                // Autocorrelation pitch estimation
                val frameSize = (sampleRate * 0.04).toInt().coerceAtMost(shortCount) // 40ms frame
                if (frameSize > 100) {
                    val frame = DoubleArray(frameSize)
                    for (i in 0 until frameSize) {
                        frame[i] = shorts[i].toDouble()
                        totalEnergy += abs(frame[i])
                        sampleCount++
                    }

                    // Autocorrelation for lag search between 80Hz (min human pitch) and 350Hz (max normal speaking pitch)
                    val minLag = sampleRate / 350
                    val maxLag = (sampleRate / 80).coerceAtMost(frameSize - 1)
                    var bestCorrelation = -1.0
                    var bestLag = minLag

                    for (lag in minLag..maxLag) {
                        var correlation = 0.0
                        for (i in 0 until (frameSize - lag)) {
                            correlation += frame[i] * frame[i + lag]
                        }
                        if (correlation > bestCorrelation) {
                            bestCorrelation = correlation
                            bestLag = lag
                        }
                    }

                    if (bestLag > 0) {
                        val calculatedHz = sampleRate.toFloat() / bestLag.toFloat()
                        if (calculatedHz in 70f..400f) {
                            pitchHz = calculatedHz
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("VoiceRecorder", "Error during audio analysis", e)
        }

        // Map pitch Hz to Android TTS pitch multiplier (standard male ~120Hz = 0.9f, female ~210Hz = 1.15f)
        val normalizedPitchMultiplier = when {
            pitchHz < 110f -> 0.82f // Deep Baritone
            pitchHz < 150f -> 0.92f // Mid Baritone / Tenor
            pitchHz < 200f -> 1.05f // Alto / Neutral
            pitchHz < 250f -> 1.18f // Soprano
            else -> 1.25f           // Higher pitch
        }

        val speechRateMultiplier = 0.95f // Comfortable, clear cadence

        return AudioAnalysisResult(
            averagePitchHz = pitchHz,
            averageRmsDb = -20f,
            recommendedPitchMultiplier = normalizedPitchMultiplier,
            recommendedRateMultiplier = speechRateMultiplier,
            audioWavPath = wavFile.absolutePath
        )
    }

    private fun rawToWave(rawFile: File, waveFile: File, sampleRate: Int, channels: Int, bitsPerSample: Int) {
        val rawData = rawFile.readBytes()
        val totalAudioLen = rawData.size.toLong()
        val totalDataLen = totalAudioLen + 36
        val byteRate = (sampleRate * channels * bitsPerSample / 8).toLong()

        val header = ByteArray(44)
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // PCM
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (sampleRate.toLong() and 0xff).toByte()
        header[25] = (sampleRate.toLong() shr 8 and 0xff).toByte()
        header[26] = (sampleRate.toLong() shr 16 and 0xff).toByte()
        header[27] = (sampleRate.toLong() shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (channels * bitsPerSample / 8).toByte()
        header[33] = 0
        header[34] = bitsPerSample.toByte()
        header[35] = 0
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()

        val out = FileOutputStream(waveFile)
        out.write(header)
        out.write(rawData)
        out.close()
    }
}

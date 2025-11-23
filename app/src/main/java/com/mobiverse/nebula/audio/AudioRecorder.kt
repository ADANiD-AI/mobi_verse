package com.mobiverse.nebula.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.jakewharton.timber.Timber
import java.io.File
import java.io.IOException

/**
 * A class to handle audio recording using the device's microphone.
 * It saves the recorded audio into a file in the app's cache directory.
 */
class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    /**
     * Starts the audio recording.
     *
     * @param fileName The name of the file to save the recording to (without extension).
     */
    fun start(fileName: String) {
        // Create the file to save the recording
        outputFile = File(context.cacheDir, "$fileName.3gp")

        // Initialize MediaRecorder based on Android version
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // Efficient format for voice
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile?.absolutePath)
            
            try {
                prepare()
                start()
                Timber.d("Audio recording started, saving to: ${outputFile?.absolutePath}")
            } catch (e: IOException) {
                Timber.e(e, "MediaRecorder prepare() or start() failed.")
            }
        }
    }

    /**
     * Stops the audio recording and releases the recorder.
     *
     * @return The [File] object of the saved recording, or null if it failed.
     */
    fun stop(): File? {
        try {
            recorder?.apply {
                stop()
                reset()   // Clear configuration
                release() // Release resources
            }
            Timber.d("Audio recording stopped. File saved: ${outputFile?.exists()}")
        } catch (e: Exception) {
            // This can happen if stop() is called without start()
            Timber.w(e, "Exception during MediaRecorder stop.")
            // Clean up the file if it was created but recording failed
            if (outputFile?.exists() == true) {
                outputFile?.delete()
                return null
            }
        }
        recorder = null
        return outputFile
    }\n}

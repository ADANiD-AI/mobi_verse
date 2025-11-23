package com.mobiverse.nebula.audio

import android.media.MediaPlayer
import com.jakewharton.timber.Timber
import java.io.IOException

/**
 * A simple audio player to play audio files using MediaPlayer.
 */
class AudioPlayer {

    private var player: MediaPlayer? = null

    /**
     * Plays an audio file from the given path.
     *
     * @param filePath The absolute path of the audio file to play.
     * @param onCompletion A callback to be invoked when playback is complete.
     */
    fun play(filePath: String, onCompletion: () -> Unit) {
        if (player != null && player!!.isPlaying) {
            Timber.d("Player is already playing. Stopping previous playback.")
            player?.stop()
            player?.release()
            player = null
        }

        player = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepareAsync() // Prepare asynchronously to not block the UI thread
                setOnPreparedListener {
                    Timber.d("MediaPlayer prepared. Starting playback.")
                    it.start()
                }
                setOnCompletionListener {
                    Timber.d("Playback completed.")
                    stop()
                    onCompletion()
                }
                setOnErrorListener { _, what, extra ->
                    Timber.e("MediaPlayer error. What: $what, Extra: $extra")
                    stop()
                    true // Error was handled
                }
            } catch (e: IOException) {
                Timber.e(e, "MediaPlayer setDataSource() failed")
                player = null
            }
        }
    }

    /**
     * Stops the playback and releases the MediaPlayer resources.
     */
    fun stop() {
        try {
            player?.stop()
            player?.release()
        } catch (e: Exception) {
            Timber.w(e, "Exception during MediaPlayer stop/release.")
        }
        player = null
        Timber.d("Audio player stopped and released.")
    }
}

package com.mobiverse.MobiVerse;

import android.content.Context;
import android.media.MediaPlayer;
import java.io.IOException;

public class AdvancedAudioPlayer {

    private MediaPlayer mediaPlayer;
    private Context context;

    public AdvancedAudioPlayer(Context context) {
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
    }

    public void play(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}

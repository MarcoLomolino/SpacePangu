package com.example.drawabletest.sounds;

import android.content.Context;
import android.media.SoundPool;

import java.util.Set;
import java.util.TreeSet;

import static android.media.SoundPool.*;

public class SoundPlayer {
    private SoundPool soundPool;
    private boolean loaded = false;
    private final Context context;

    public SoundPlayer(Context context) {
        this.context = context;
        this.createSP();
    }


    private void createSP() {
        Builder builder = new Builder();
        builder.setMaxStreams(500);
        soundPool = builder.build();
        soundPool.setOnLoadCompleteListener((SoundPool sp, int sampleId, int status) -> loaded = true);
        loaded = false;
    }

    public int loadSound(int resource) {
        return soundPool.load(context, resource, 1);
    }

    public void playSound(int loadedSound, float volume) {
        if(loaded){
            soundPool.play(loadedSound, volume, volume, 1, 0, 0.99f);
        }
    }

    public void releaseSP() {
        soundPool.release();
        soundPool = null;
        loaded = false;
        createSP();
    }


}

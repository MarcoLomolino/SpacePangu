package com.example.drawabletest;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {
    SoundPool soundPool;
    private boolean loaded = false;
    Context context;

    public SoundPlayer(Context context) {
        this.context = context;
    }

    //CREA LA SOUNDPOOL
    //E' DAVVERO NECESSARIO QUELL'IF ELSE? NON BASTA LA PRIMA DELLE DUE INIZIALIZZAZIONI PER TUTTI?
    public void createSP() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(500)
                    .build();
        }else{
            soundPool = new SoundPool(500, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool sp, int sampleId, int status) {
                loaded = true;
                return;
            }
        });
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

    /*public void releaseSP() {
        soundPool.release();
    }*/

}

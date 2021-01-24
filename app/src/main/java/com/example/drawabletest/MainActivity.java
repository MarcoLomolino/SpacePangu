package com.example.drawabletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    //tempo durata del mio splashscreen in ms
    private static int SPLASH_TIME_OUT = 1700;
    SoundPlayer sp;
    int startSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = new SoundPlayer(this);
        sp.createSP();
        startSound = sp.loadSound(R.raw.splash2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sp.playSound(startSound, 0.50f);
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    //LO LASCIO QUI PER OGNI EVENZIENZA
    /*private void playbuttonsound(int resource) {
        final MediaPlayer beepMP = MediaPlayer.create(context, resource);
        beepMP.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mprelease(beepMP);
    }

    private void mprelease(MediaPlayer soundmp) {
        soundmp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }*/
}
package com.example.drawabletest.activities.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.drawabletest.R;
import com.example.drawabletest.sounds.SoundPlayer;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private SoundPlayer sp;
    private int startSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = new SoundPlayer(this);
        startSound = sp.loadSound(R.raw.splash2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
        //tempo durata del mio splashscreen in ms
        int SPLASH_TIME_OUT = 1700;
        new Handler(Looper.myLooper()).postDelayed(() -> {
            sp.playSound(startSound, 0.50f);
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
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
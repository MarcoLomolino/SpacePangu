package com.example.drawabletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;

import com.example.drawabletest.play.PlayActivity;

public class HomeActivity extends AppCompatActivity {

    SoundPlayer sp = new SoundPlayer(this);
    int menuSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp.createSP();
        menuSound = sp.loadSound(R.raw.menu_101);
    }

    public void ShowHighscore(View view){
        Intent intent = new Intent(this, Highscores.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public void showOption(MenuItem item) {
        Intent intent = new Intent(this,Options.class);
        startActivity(intent);
    }

    public void showPlayGame(View view){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public void showEditor(View view){
        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
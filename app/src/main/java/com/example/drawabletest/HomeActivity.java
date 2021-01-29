package com.example.drawabletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    SoundPlayer sp = new SoundPlayer(this);
    int menuSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menuSound = sp.loadSound(R.raw.menu_101);
    }

    public void ShowHighscore(View view){
        SharedPreferences mPrefs = getSharedPreferences("salva_map", MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("lg",0);
        ed.apply();
        Intent intent = new Intent(this, HighscoresActivity.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public void showOption(MenuItem item) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void showPlayGame(View view){
        Intent intent = new Intent(this, DatabaseHelper.SinglePlayerActivity.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public void showEditor(View view){
        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public void showVersus(View view){
        Intent intent = new Intent(this, VersusActivity.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
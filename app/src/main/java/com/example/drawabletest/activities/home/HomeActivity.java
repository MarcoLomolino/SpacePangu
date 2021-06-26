package com.example.drawabletest.activities.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.drawabletest.activities.editor.EditorActivity;
import com.example.drawabletest.activities.game.SinglePlayerActivityLandscape;
import com.example.drawabletest.activities.highscores.HighscoresActivity;
import com.example.drawabletest.activities.impostazioni.ImpostazioniActivity;
import com.example.drawabletest.R;
import com.example.drawabletest.sounds.SoundPlayer;
import com.example.drawabletest.activities.game.PlaySinglePlayerActivity;
import com.example.drawabletest.activities.game.PlayMultyplayerActivity;

public class HomeActivity extends AppCompatActivity {

    private SoundPlayer sp;
    private int menuSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.sp = new SoundPlayer(this);
        this.menuSound = sp.loadSound(R.raw.menu_101);
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
        Intent intent = new Intent(this, ImpostazioniActivity.class);
        startActivity(intent);
    }

    public void showPlayGame(View view){
        Intent intent;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            intent = new Intent(this, PlaySinglePlayerActivity.class);
        } else{
            intent = new Intent(this, SinglePlayerActivityLandscape.class);
        }
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public void showEditor(View view){
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public void showVersus(View view){
        Intent intent = new Intent(this, PlayMultyplayerActivity.class);
        startActivity(intent);
        sp.playSound(menuSound, 0.99f);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
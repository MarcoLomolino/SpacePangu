package com.example.drawabletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.drawabletest.play.PlayActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void ShowHighscore(View view){
        Intent intent = new Intent(this, Highscores.class);
        startActivity(intent);
        playbuttonsound(R.raw.menu_101);
    }

    public void showOption(MenuItem item) {
        Intent intent = new Intent(this,Options.class);
        startActivity(intent);
    }

    public void showPlayGame(View view){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        playbuttonsound(R.raw.menu_101);
    }

    public void showEditor(View view){
        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
        playbuttonsound(R.raw.menu_101);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void playbuttonsound(int resource) {
        final MediaPlayer beepMP = MediaPlayer.create(this, resource);
        beepMP.start();
        mprelease(beepMP);
    }

    private void mprelease(MediaPlayer soundmp) {
        soundmp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }

}
package com.example.drawabletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toolbar;

import com.example.drawabletest.game.Game;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void ShowHighscore(View view){
        Intent intent = new Intent(this, Highscores.class);
        startActivity(intent);

    }

    public void PlayGame(View view){
        Intent intent2 = new Intent(this, PlayActivity.class);
        startActivity(intent2);
    }
}
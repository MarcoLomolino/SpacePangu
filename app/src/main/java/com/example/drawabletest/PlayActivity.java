package com.example.drawabletest;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.drawabletest.game.Game;

public class PlayActivity extends AppCompatActivity {

    private Game game;
    private Handler updateHandler;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // nastavi orientaciu obrazovky
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // vytvori novu hru
        difficulty = ((MyApplication) this.getApplication()).getDifficolta();
        game = new Game(this, difficulty);
        setContentView(game);

        // vytvori handler a thread
        VytvorHandler();
        UpdateThread myThread = new UpdateThread(updateHandler);
        myThread.start();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("HandlerLeak")
    private void VytvorHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        };
    }

    protected void onPause() {
        super.onPause();
        game.stopScanning();
    }

    protected void onResume() {
        super.onResume();
        game.runScanning();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


package com.example.drawabletest.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drawabletest.UpdateThread;
import com.example.drawabletest.play.game.Game;

public class PlayActivity extends AppCompatActivity {

    private Game game;
    private Handler updateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        game = new Game(this);
        setContentView(game);

        
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


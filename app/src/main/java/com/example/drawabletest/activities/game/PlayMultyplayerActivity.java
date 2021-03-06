package com.example.drawabletest.activities.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.drawabletest.game.play.MultiPlayer;

public class PlayMultyplayerActivity extends AppCompatActivity {

    private MultiPlayer game;
    private Handler updateHandler;
    UpdateThread myThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Impedisce allo schermo di spegnersi automaticamente durante la partita

        ActionBar ab = getSupportActionBar();
        assert ab!= null;
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

    @Override
    protected void onPause() {
        super.onPause();
        myThread.setPlay(false);
        /*try {
            myThread.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        game = new MultiPlayer(this);
        setContentView(game);
        VytvorHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();

        //game.runScanning();
        //myThread.setPlay(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myThread.setPlay(false);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
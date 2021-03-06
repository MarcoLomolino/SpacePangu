package com.example.drawabletest.activities.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drawabletest.game.play.EasterEgg;
import com.example.drawabletest.game.play.SinglePlayer;

public class PlaySinglePlayerActivity extends AppCompatActivity {

    private SinglePlayer game;
    private Handler updateHandler;
    private UpdateThread myThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Impedisce allo schermo di spegnersi automaticamente durante la partita

        ActionBar ab = getSupportActionBar();
        assert ab != null;
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
        game.stopScanning();
        myThread.setPlay(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        game = new SinglePlayer(this);
        setContentView(game);
        VytvorHandler();
        myThread = new UpdateThread(updateHandler);


        SharedPreferences mPreferences = getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE);
        mPreferences = getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE);
        String name = mPreferences.getString("username","UtenteDemo");
        if(name.equals("negroalbino"))
        {
            Intent intent = new Intent(this, EasterEggActivity.class);
            startActivity(intent);
        }
        else
        {
            game.runScanning();
            myThread.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        game.stopScanning();
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


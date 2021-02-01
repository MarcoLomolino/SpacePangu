package com.example.drawabletest.activities.game;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.drawabletest.game.play.EditedGame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PlayEditorActivity extends AppCompatActivity {

    private EditedGame game;
    private Handler updateHandler;
    private SharedPreferences mPrefs;
    private int mCur;
    UpdateThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Impedisce allo schermo di spegnersi automaticamente durante la partita

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        mPrefs = getSharedPreferences("salva_map",MODE_PRIVATE);
        mCur = mPrefs.getInt("view_mode",1);
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
        myThread.setPlay(false);
        /*try {
            myThread.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    protected void onResume() {
        super.onResume();
        game = new EditedGame(this,mCur);
        setContentView(game);
        game.runScanning();
        VytvorHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        game.stopScanning();
        myThread.setPlay(false);
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


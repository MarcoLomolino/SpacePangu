package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Permission;
import java.util.List;

public class Highscores extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private static final int PERMISSION_RESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        TextView score[] = {findViewById(R.id.score1),findViewById(R.id.score2),findViewById(R.id.score3),findViewById(R.id.score4),findViewById(R.id.score5)};
        highscoreGenerator(score[0],score[1],score[2],score[3],score[4],0);
        TextView hard[] = {findViewById(R.id.globalScore1),findViewById(R.id.globalScore2),findViewById(R.id.globalScore3),findViewById(R.id.globalScore4),findViewById(R.id.globalScore5)};
        highscoreGenerator(hard[0],hard[1],hard[2],hard[3],hard[4],1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean scoreShare(MenuItem item) {
        DatabaseHelper databaseHelper = new DatabaseHelper(Highscores.this);
        if (item.getItemId() == R.id.share_button){
            List<CustomerModel> records = databaseHelper.getScore(0);
            String share = getString(R.string.share) + "\n";
            if(records.get(0).getScore() != 0 ){
                share = share + getString(R.string.classic) + "\n";
                for (int i = 0; i < records.size(); i++) {
                    if (records.get(i).getScore() != 0)
                        share = share + (i + 1) + ") " + records.get(i).getScore().toString() + "\n";
                }
            }
            records = databaseHelper.getScore(1);
            if(records.get(0).getScore() != 0) {
                share = share + getString(R.string.hard) + "\n";
                for (int i = 0; i < records.size(); i++) {
                    if (records.get(i).getScore() != 0)
                        share = share + (i + 1) + ") " + records.get(i).getScore().toString() + "\n";
                }
            }
            if (share.equals(getString(R.string.share) + "\n")){
                Toast.makeText(this,getString(R.string.no_scores),Toast.LENGTH_SHORT).show();
            } else {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, share);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void highscoreGenerator(TextView score1, TextView score2, TextView score3, TextView score4, TextView score5, int difficulty){
        DatabaseHelper databaseHelper = new DatabaseHelper(Highscores.this);
        List<CustomerModel> records = databaseHelper.getScore(difficulty);
        if(records.size()>0 && records.get(0).getScore()!=0){
            score1.setText("1) "+records.get(0).getScore().toString());
        }
        if(records.size()>1){
            score2.setText("2) "+records.get(1).getScore().toString());
        }
        if(records.size()>2){
            score3.setText("3) "+records.get(2).getScore().toString());
        }
        if(records.size()>3){
            score4.setText("4) "+records.get(3).getScore().toString());
        }
        if(records.size()>4){
            score5.setText("5) "+records.get(4).getScore().toString());
        }
    }



}
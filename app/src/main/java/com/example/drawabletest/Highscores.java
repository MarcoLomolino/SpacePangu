package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class Highscores extends AppCompatActivity {


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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
                
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    public boolean screenshotShare(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share_button:

                try {
                    shareImage(store(getScreenShot(),"screenshot"));

                }
                catch (Exception e) {
                    System.out.println(e);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
     */

    public void scoreShare(MenuItem item){
        try {
            shareImage(store(getScreenShot(),"screenshot"));
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public Bitmap getScreenShot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public static File store(Bitmap bm, String fileName){
        final String dirPath = Environment.getStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
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
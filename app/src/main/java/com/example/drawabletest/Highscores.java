package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class Highscores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        highscoreGenerator();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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

    private void highscoreGenerator(){
        TextView score1, score2, score3, score4, score5;
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        score5 = findViewById(R.id.score5);

        DatabaseHelper databaseHelper = new DatabaseHelper(Highscores.this);
        List<CustomerModel> records = databaseHelper.getRecord();

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
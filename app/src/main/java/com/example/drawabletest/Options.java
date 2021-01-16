package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Options extends AppCompatActivity {

    private CheckBox difficulty_classic;
    private CheckBox difficulty_Hard;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        difficulty_classic = findViewById(R.id.checkBox_classic);
        difficulty_Hard = findViewById(R.id.checkBox_hard);

        mPreferences = getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        mPreferences = getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE);
        String difficulty = mPreferences.getString("difficulty", "classic");
        if (difficulty.equals("classic")) {
            difficulty_classic.setChecked(true);
            difficulty_classic.setClickable(false);
        } else {
            difficulty_Hard.setChecked(true);
            difficulty_Hard.setClickable(false);
        }

       difficulty_Hard.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (group, checkedId) -> {
           if(difficulty_Hard.isChecked()) {
               difficulty_classic.setChecked(false);
               difficulty_Hard.setClickable(false);
               difficulty_classic.setClickable(true);
           }
       });

        difficulty_classic.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (group, checkedId) -> {
            if(difficulty_classic.isChecked()) {
                difficulty_Hard.setChecked(false);
                difficulty_classic.setClickable(false);
                difficulty_Hard.setClickable(true);
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this, "PRESO", Toast.LENGTH_LONG);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onDifficultyClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkBox_classic:
                if (checked) {
                    Toast.makeText(this, "Classic Difficulty selected", Toast.LENGTH_SHORT).show();
                    mEditor.putString("difficulty","classic");
                    mEditor.commit();
                }
                break;
            case R.id.checkBox_hard:
                if (checked) {
                    Toast.makeText(this, "Hard Difficulty selected", Toast.LENGTH_SHORT).show();
                    mEditor.putString("difficulty","difficult");
                    mEditor.commit();
                }
                break;
        }
    }

}
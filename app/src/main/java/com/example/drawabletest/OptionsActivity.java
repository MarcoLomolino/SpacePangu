package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    private CheckBox difficulty_classic;
    private CheckBox difficulty_Hard;
    private CheckBox controller_accelerometer;
    private CheckBox controller_drag;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }


        difficulty_classic = findViewById(R.id.checkBox_classic);
        difficulty_Hard = findViewById(R.id.checkBox_hard);
        controller_accelerometer = findViewById(R.id.accelerometer);
        controller_drag = findViewById(R.id.drag);
        EditText username = findViewById(R.id.editTextTextPersonName2);
        Button conferma = findViewById(R.id.ConfirmButton);

        SharedPreferences mPreferences = getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();


        mPreferences = getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE);
        String name = mPreferences.getString("username","");
        username.setText(name);
        buttonClicked(conferma, username);

        String difficulty = mPreferences.getString("difficulty", "classic");
        if (difficulty.equals("classic")) {
            difficulty_classic.setChecked(true);
            difficulty_classic.setClickable(false);
        } else {
            difficulty_Hard.setChecked(true);
            difficulty_Hard.setClickable(false);
        }

        difficulty_Hard.setOnCheckedChangeListener((CompoundButton group, boolean checkedId) -> {
            if(difficulty_Hard.isChecked())
                ableCheck(difficulty_classic, difficulty_Hard);

        });

        difficulty_classic.setOnCheckedChangeListener((group, checkedId) -> {
            if(difficulty_classic.isChecked())
                ableCheck(difficulty_Hard, difficulty_classic);

        });


        String controller = mPreferences.getString("controller", "accelerometer");
        if (controller.equals("accelerometer")) {
            controller_accelerometer.setChecked(true);
            controller_accelerometer.setClickable(false);
        } else {
            controller_drag.setChecked(true);
            controller_drag.setClickable(false);
        }

        controller_drag.setOnCheckedChangeListener((group, checkedId) -> {
            if(controller_drag.isChecked())
                ableCheck(controller_accelerometer, controller_drag);

        });

        controller_accelerometer.setOnCheckedChangeListener((group, checkedId) -> {
            if(controller_accelerometer.isChecked())
                ableCheck(controller_drag, controller_accelerometer);

        });


    }

    private void ableCheck(CheckBox check1, CheckBox check2) {
        check1.setChecked(false);
        check2.setClickable(false);
        check1.setClickable(true);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    public void onDifficultyClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkBox_classic:
                if (checked) {
                    mEditor.putString("difficulty","classic");
                    mEditor.commit();
                }
                break;
            case R.id.checkBox_hard:
                if (checked) {
                    mEditor.putString("difficulty","hard");
                    mEditor.commit();
                }
                break;
            case R.id.accelerometer:
                if (checked) {
                    mEditor.putString("controller","accelerometer");
                    mEditor.commit();
                }
                break;
            case R.id.drag:
                if (checked) {
                    mEditor.putString("controller","drag");
                    mEditor.commit();
                }

                break;
        }
    }

    public void buttonClicked(Button btn, EditText text){
        btn.setOnClickListener(v -> {
            if(text.getText().toString().length()>15 || text.getText().toString().length()<3){
                Toast.makeText(OptionsActivity.this, getString(R.string.username_length), Toast.LENGTH_SHORT).show();
            }
            else if(!text.getText().toString().matches("[A-Za-z0-9]+")){
                Toast.makeText(OptionsActivity.this, getString(R.string.username_notValid), Toast.LENGTH_SHORT).show();
            }
            else{
                mEditor.putString("username",text.getText().toString().replaceAll(" ",""));
                mEditor.commit();
                text.setEnabled(false);
                text.setEnabled(true);
                Toast.makeText(OptionsActivity.this, getString(R.string.username_saved), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
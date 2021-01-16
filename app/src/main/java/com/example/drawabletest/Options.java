package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        difficulty_classic = findViewById(R.id.checkBox);
        difficulty_Hard = findViewById(R.id.checkBox2);


       difficulty_Hard.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (group, checkedId) -> {
            if(difficulty_Hard.isChecked())
                difficulty_classic.setChecked(false);
            else
                difficulty_classic.setChecked(true);
       });

        difficulty_classic.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (group, checkedId) -> {
            if(difficulty_classic.isChecked())
                difficulty_Hard.setChecked(false);
            else
                difficulty_Hard.setChecked(true);
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this, "PRESO", Toast.LENGTH_LONG);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
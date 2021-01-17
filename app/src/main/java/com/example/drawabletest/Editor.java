package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

public class Editor extends AppCompatActivity {
    DatabaseHelper database;
    int isClicked[][] = {{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        database = new DatabaseHelper(Editor.this);

        ImageButton btn[][] = {{(ImageButton) findViewById(R.id.btn1_1),(ImageButton) findViewById(R.id.btn1_2),(ImageButton) findViewById(R.id.btn1_3),(ImageButton) findViewById(R.id.btn1_4),(ImageButton) findViewById(R.id.btn1_5)},
                {(ImageButton) findViewById(R.id.btn2_1),(ImageButton) findViewById(R.id.btn2_2),(ImageButton) findViewById(R.id.btn2_3),(ImageButton) findViewById(R.id.btn2_4),(ImageButton) findViewById(R.id.btn2_5)},
                {(ImageButton) findViewById(R.id.btn3_1),(ImageButton) findViewById(R.id.btn3_2),(ImageButton) findViewById(R.id.btn3_3),(ImageButton) findViewById(R.id.btn3_4),(ImageButton) findViewById(R.id.btn3_5)},
                {(ImageButton) findViewById(R.id.btn4_1),(ImageButton) findViewById(R.id.btn4_2),(ImageButton) findViewById(R.id.btn4_3),(ImageButton) findViewById(R.id.btn4_4),(ImageButton) findViewById(R.id.btn4_5)}};

        Button save1 = (Button) findViewById(R.id.btnSave1);
        Button save2 = (Button) findViewById(R.id.btnSave2);
        Button save3 = (Button) findViewById(R.id.btnSave3);
        Button save4 = (Button) findViewById(R.id.btnSave4);

        salvaEditor(save1,btn,1);
        salvaEditor(save2,btn,2);
        salvaEditor(save3,btn,3);
        salvaEditor(save4,btn,4);
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

    public void cambiaColore(ImageButton b, int i, int j,int livello){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(isClicked[i][j]){
                    case 0:
                        b.setImageResource(R.drawable.brick_red);
                        isClicked[i][j] = 1;
                        database.modificaBlocchi(j,i,1,livello);
                        break;
                    case 1:
                        b.setImageResource(R.drawable.brick_healthup);
                        isClicked[i][j] = 2;
                        database.modificaBlocchi(j,i,2,livello);
                        break;
                    case 2:
                        b.setImageResource(R.drawable.brick_gold);
                        isClicked[i][j] = 3;
                        database.modificaBlocchi(j,i,3,livello);
                        break;
                    case 3:
                        b.setImageResource(R.drawable.brick_metal);
                        isClicked[i][j] = 4;
                        database.modificaBlocchi(j,i,4,livello);
                        break;
                    case 4:
                        b.setImageResource(R.drawable.brick_empty);
                        isClicked[i][j] = 0;
                        database.modificaBlocchi(j,i,0,livello);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void salvaEditor(Button save, ImageButton[][] btn, int livello){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulisci(btn);
                List<ModelEditor> records = database.getEditorFile(livello);
                for(ModelEditor cm: records){
                    switch(cm.getAttivo()){
                        case 0:
                            isClicked[cm.getY()][cm.getX()] = 0;
                            break;
                        case 1:
                            isClicked[cm.getY()][cm.getX()] = 1;
                            btn[cm.getY()][cm.getX()].setImageResource(R.drawable.brick_red);
                            break;
                        case 2:
                            isClicked[cm.getY()][cm.getX()] = 2;
                            btn[cm.getY()][cm.getX()].setImageResource(R.drawable.brick_healthup);
                            break;
                        case 3:
                            isClicked[cm.getY()][cm.getX()] = 3;
                            btn[cm.getY()][cm.getX()].setImageResource(R.drawable.brick_gold);
                            break;
                        case 4:
                            isClicked[cm.getY()][cm.getX()] = 4;
                            btn[cm.getY()][cm.getX()].setImageResource(R.drawable.brick_metal);
                            break;
                        default:
                            break;
                    }
                }

                for(int i=0;i<4;i++){
                    for(int j=0;j<5;j++){
                        cambiaColore(btn[i][j],i,j,livello);
                    }
                }
            }
        });
    }

    public void pulisci(ImageButton[][] btn){
        for(int i=0;i<4;i++){
            for(int j=0;j<5;j++){
                isClicked[i][j] = 0;
                btn[i][j].setImageResource(R.drawable.brick_empty);
            }
        }
    }
}
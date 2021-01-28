package com.example.drawabletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Editor extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button confirm;
    DatabaseHelper database;
    int isClicked[][] = {{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}};
    private SharedPreferences mPrefs;
    private int mCur;

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

        //imposto il valore dei bottoni con i valori passati
        mPrefs = getSharedPreferences("salva_map",MODE_PRIVATE);
        mCur = mPrefs.getInt("view_mode",1);

        Button save1 = (Button) findViewById(R.id.btnSave1);
        Button save2 = (Button) findViewById(R.id.btnSave2);
        Button save3 = (Button) findViewById(R.id.btnSave3);
        Button save4 = (Button) findViewById(R.id.btnSave4);

        salvaEditor(save1,btn,1);
        salvaEditor(save2,btn,2);
        salvaEditor(save3,btn,3);
        salvaEditor(save4,btn,4);

        switch (mCur){
            case 1:
                save1.callOnClick();
                break;
            case 2:
                save2.callOnClick();
                break;
            case 3:
                save3.callOnClick();
                break;
            case 4:
                save4.callOnClick();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //apro l'editor di preferenze
        SharedPreferences.Editor ed = mPrefs.edit();
        //metto il valore dentro l'editor di preferenze
        ed.putInt("view_mode",mCur);
        //salva il valore
        ed.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);
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
                        b.setImageResource(R.drawable.slow_brick);
                        isClicked[i][j] = 5;
                        database.modificaBlocchi(j,i,5,livello);
                        break;
                    case 5:
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
                mCur = livello;
                switch (livello){
                    case 1:
                        cambiaColoreMap((Button) findViewById(R.id.btnSave2),(Button) findViewById(R.id.btnSave3),(Button) findViewById(R.id.btnSave4));
                        break;
                    case 2:
                        cambiaColoreMap((Button) findViewById(R.id.btnSave1),(Button) findViewById(R.id.btnSave3),(Button) findViewById(R.id.btnSave4));
                        break;
                    case 3:
                        cambiaColoreMap((Button) findViewById(R.id.btnSave2),(Button) findViewById(R.id.btnSave1),(Button) findViewById(R.id.btnSave4));
                        break;
                    case 4:
                        cambiaColoreMap((Button) findViewById(R.id.btnSave2),(Button) findViewById(R.id.btnSave3),(Button) findViewById(R.id.btnSave1));
                        break;
                    default:
                        break;
                }
                save.setBackgroundColor(Color.GRAY);
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
                        case 5:
                            isClicked[cm.getY()][cm.getX()] = 5;
                            btn[cm.getY()][cm.getX()].setImageResource(R.drawable.slow_brick);
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

    public void createInfoDialog(MenuItem item){
        dialogBuilder = new AlertDialog.Builder(this);
        final View infoPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        TextView customTitle = new TextView(this);
        customTitle.setText(R.string.guide);
        customTitle.setBackgroundColor(Color.DKGRAY);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextSize(24);
        customTitle.setTextColor(Color.WHITE);

        dialogBuilder.setView(infoPopupView);
        dialogBuilder.setCustomTitle(customTitle);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setGravity(Gravity.CENTER);
        positiveButton.setBackgroundColor(Color.DKGRAY);
        positiveButton.setTextColor(Color.WHITE);
    }

    public void cambiaColoreMap(Button a, Button b, Button c){
        a.setBackgroundColor(Color.rgb(25,25,112));
        b.setBackgroundColor(Color.rgb(25,25,112));
        c.setBackgroundColor(Color.rgb(25,25,112));
    }

    public void showPlayEditedGame(View view){
        if(database.getEmpty()!=0){
            Intent intent = new Intent(this, PlayEditorActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, getString(R.string.empyt_slot), Toast.LENGTH_SHORT).show();
        }
    }
}
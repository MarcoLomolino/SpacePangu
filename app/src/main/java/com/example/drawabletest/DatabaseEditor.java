package com.example.drawabletest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseEditor extends SQLiteOpenHelper {
    private static final String EDITOR = "EDITOR";
    private static final String X_COLUMN = "X";
    private static final String Y_COLUMN = "Y";
    private static final String ATTIVO_COLUMN = "ATTIVO";

    public DatabaseEditor(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            String createTableStatement = "CREATE TABLE " + EDITOR + " (LIVELLO INT, " + X_COLUMN + " INT, " + Y_COLUMN + " INT, " + ATTIVO_COLUMN+ " INT, PRIMARY KEY(LIVELLO,X,Y))";
            db.execSQL(createTableStatement);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        for(int z=1;z<5;z++){
            for(int x=0;x<5;x++){
                for(int y=0;y<4;y++){
                    db.execSQL("INSERT INTO "+ EDITOR+ " VALUES("+z+","+x+","+y+",0)");
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    public void modificaBlocchi(int x, int y, int attivo, int livello){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE EDITOR SET ATTIVO="+attivo +" WHERE X="+x+" AND Y="+y+" AND LIVELLO="+livello);
    }

    public List<ModelEditor> getRecord(int livello){
        List<ModelEditor> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + EDITOR+ " WHERE LIVELLO="+livello;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int customerX = cursor.getInt(1);
                int customerY = cursor.getInt(2);
                int customerA = cursor.getInt(3);

                ModelEditor me = new ModelEditor(customerX,customerY,customerA);
                list.add(me);
            }while(cursor.moveToNext());
        }
        else{
            cursor.close();
            db.close();
            return list;
        }
        return list;
    }
}


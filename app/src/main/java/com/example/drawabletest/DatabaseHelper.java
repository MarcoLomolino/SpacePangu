package com.example.drawabletest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String EDITOR_TABLE = "EDITOR";
    private static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    private static final String HARD_TABLE = "HARD";
    private static final String SCORE_COLUMN = "SCORE";
    private static final String ID_COLUMN = "ID";
    private static final String X_COLUMN = "X";
    private static final String Y_COLUMN = "Y";
    private static final String ATTIVO_COLUMN = "ATTIVO";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SCORE_COLUMN + " INT)";
        db.execSQL(createTableStatement);
        String createTableStatement2 = "CREATE TABLE " + EDITOR_TABLE + " (LIVELLO INT, " + X_COLUMN + " INT, " + Y_COLUMN + " INT, " + ATTIVO_COLUMN+ " INT, PRIMARY KEY(LIVELLO,X,Y))";
        db.execSQL(createTableStatement2);
        String createTableStatement3 = "CREATE TABLE " + HARD_TABLE + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SCORE_COLUMN + " INT)";
        db.execSQL(createTableStatement3);
        db.execSQL("INSERT INTO "+ CUSTOMER_TABLE+ " VALUES(0,0)");
        for(int z=1;z<5;z++){
            for(int x=0;x<5;x++){
                for(int y=0;y<4;y++){
                    db.execSQL("INSERT INTO "+ EDITOR_TABLE+ " VALUES("+z+","+x+","+y+",0)");
                }
            }
        }
        db.execSQL("INSERT INTO "+ HARD_TABLE+ " VALUES(0,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(CustomerModel customerModel, int difficulty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        List<CustomerModel> list;
        if(difficulty==0){
            list = getScore(0);
        }
        else{
            list = getScore(1);
        }
        if(isDuplicatedRecord(list,customerModel.getScore())==false){
            if(list.size()>=5 && list.get(4).getScore()<customerModel.getScore()){
                deleteOne(list.get(4).getId(),difficulty);
            }
            if(list.size()<5 || list.get(4).getScore()<customerModel.getScore()){
                cv.put(SCORE_COLUMN, customerModel.getScore());

                if(list.get(0).getId()==0){
                    deleteOne(0,difficulty);
                }
                long insert;
                if(difficulty==0){
                    insert = db.insert(CUSTOMER_TABLE, null, cv);
                }
                else{
                    insert = db.insert(HARD_TABLE, null, cv);
                }
                if(insert==-1){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        return true;
    }

    public void deleteOne(int id, int difficulty){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteFrom;
        if(difficulty==0){
            deleteFrom = "DELETE FROM "+ CUSTOMER_TABLE + " WHERE "+ ID_COLUMN + " = "+id;
        }
        else{
            deleteFrom = "DELETE FROM "+ HARD_TABLE + " WHERE "+ ID_COLUMN + " = "+id;
        }
        db.execSQL(deleteFrom);
    }

    public List<CustomerModel> getScore(int difficulty){
        List<CustomerModel> list = new ArrayList<>();

        String queryString;
        if(difficulty==0){
            queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        }
        else{
            queryString = "SELECT * FROM " + HARD_TABLE;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int customerID = cursor.getInt(0);
                int customerScore = cursor.getInt(1);

                CustomerModel customerModel = new CustomerModel(customerID,customerScore);
                list.add(customerModel);
                Collections.sort(list, new CompareScore());
            }while(cursor.moveToNext());
        }
        else{
            cursor.close();
            db.close();
            return list;
        }
        return list;
    }

    private boolean isDuplicatedRecord(List<CustomerModel> lista, int score){
        for(CustomerModel cm: lista){
            if(score == cm.getScore()){
                return true;
            }
        }
        return false;
    }

    public void modificaBlocchi(int x, int y, int attivo, int livello){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE EDITOR SET ATTIVO="+attivo +" WHERE X="+x+" AND Y="+y+" AND LIVELLO="+livello);
    }

    public List<ModelEditor> getEditorFile(int livello){
        List<ModelEditor> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + EDITOR_TABLE+ " WHERE LIVELLO="+livello;
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

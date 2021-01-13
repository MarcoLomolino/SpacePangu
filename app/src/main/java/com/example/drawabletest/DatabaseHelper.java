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
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String SCORE_COLUMN = "SCORE";
    public static final String ID_COLUMN = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SCORE_COLUMN + " INT)";
        db.execSQL(createTableStatement);
        db.execSQL("INSERT INTO "+ CUSTOMER_TABLE+ " VALUES(0,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(CustomerModel customerModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        List<CustomerModel> list = getRecord();

        if(isDuplicatedRecord(list,customerModel.getScore())==false){
            if(list.size()>=5 && list.get(4).getScore()<customerModel.getScore()){
                deleteOne(list.get(4).getId());
            }
            if(list.size()<5 || list.get(4).getScore()<customerModel.getScore()){
                cv.put(SCORE_COLUMN, customerModel.getScore());

                if(list.get(0).getId()==0){
                    deleteOne(0);
                }
                long insert = db.insert(CUSTOMER_TABLE, null, cv);
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

    public void deleteOne(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteFrom = "DELETE FROM "+ CUSTOMER_TABLE + " WHERE "+ ID_COLUMN + " = "+id;
        db.execSQL(deleteFrom);
    }

    public List<CustomerModel> getRecord(){
        List<CustomerModel> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
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
}

package com.example.drawabletest.databases;

import android.content.Context;

import com.example.drawabletest.databases.sort.CompareScore;
import com.example.drawabletest.databases.model.ModelScore;
import com.example.drawabletest.connectivity.InternetConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class DatabaseRemote {
    private final Context context;
    private final String difficulty;

    public DatabaseRemote(Context ctx, String difficulty){
        this.context = ctx;
        this.difficulty = difficulty;
    }

    public ArrayList<ModelScore> selectDati(){
        BackgroundWorkers backgroundWorker = new BackgroundWorkers();
        InternetConnection conn = new InternetConnection(this.context);
        ArrayList<ModelScore> record = new ArrayList<>();
        if (conn.isOnline()) {
            backgroundWorker.execute("select",difficulty);
            try {
                String ris = backgroundWorker.get();
                String[] nomi = ris.split(" ");
                int i = 0;
                while (i < 14) {
                    record.add(new ModelScore(Integer.parseInt(nomi[i]), nomi[i + 1], Integer.parseInt(nomi[i + 2])));
                    i = i + 3;
                }
                Collections.sort(record, new CompareScore());
                return record;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void deleteDati(int id){
        BackgroundWorkers backgroundWorker = new BackgroundWorkers();
        new InternetConnection(this.context);
        backgroundWorker.execute("delete", difficulty, String.valueOf(id));
    }

    public void insertDati(String nome, String punteggio){
        BackgroundWorkers backgroundWorker = new BackgroundWorkers();
        InternetConnection conn = new InternetConnection(this.context);
        if (conn.isOnline()){
            ArrayList<ModelScore> list;
            list = selectDati();
            if(list.size()>=5 && list.get(4).getScore()<Integer.parseInt(punteggio)){
                deleteDati(list.get(4).getId());
            }
            if(list.size()<5 || list.get(4).getScore()<Integer.parseInt(punteggio)) {
                backgroundWorker.execute("insert", difficulty, nome, punteggio);
                if (list.get(0).getId() == 0) {
                    deleteDati(0);
                }
            }
        }
    }
}

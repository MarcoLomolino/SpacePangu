package com.example.drawabletest;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class DatabaseRemote {
    private Context context;
    private String difficulty;

    public DatabaseRemote(Context ctx, String difficulty){
        this.context = ctx;
        this.difficulty = difficulty;
    }

    public ArrayList<CustomerModel> selectDati(){
        BackgroundWorkers backgroundWorker = new BackgroundWorkers(this.context);
        InternetConnection conn = new InternetConnection(this.context);
        ArrayList<CustomerModel> record = new ArrayList<CustomerModel>();
        if (conn.isOnline()) {
            backgroundWorker.execute("select",difficulty);
            try {
                String ris = backgroundWorker.get();
                String[] nomi = ris.split(" ");
                int i = 0;
                while (i < 14) {
                    record.add(new CustomerModel(Integer.parseInt(nomi[i]), nomi[i + 1], Integer.parseInt(nomi[i + 2])));
                    i = i + 3;
                }
                Collections.sort(record, new CompareScore());
                return record;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void deleteDati(int id){
        BackgroundWorkers backgroundWorker = new BackgroundWorkers(this.context);
        InternetConnection conn = new InternetConnection(this.context);
        backgroundWorker.execute("delete", difficulty, String.valueOf(id));
    }

    public void insertDati(String nome, String punteggio){
        BackgroundWorkers backgroundWorker = new BackgroundWorkers(this.context);
        InternetConnection conn = new InternetConnection(this.context);
        if (conn.isOnline()){
            ArrayList<CustomerModel> list = new ArrayList<CustomerModel>();
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

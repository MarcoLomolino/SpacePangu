package com.example.drawabletest;

import android.app.Application;

public class MyApplication extends Application { //CLASSE CHE CONTIENE LE VARIABILI GLOBALI DELL'APPLICAZIONE

    private String difficolta = "classic";

    public String getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }
}

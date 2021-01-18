package com.example.drawabletest.play.game;

import android.content.Context;
import android.content.SharedPreferences;

public class Statistic {

    private int life;
    private int score;
    private int level;
    private String difficulty;
    private String controller;




    public Statistic(Context context) {
        this.difficulty = context.getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE).getString("difficulty", "classic");
        this.controller = context.getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE).getString("controller", "accelerometer");

        if(difficulty.equals("classic"))
            this.life = 3;
        else
            this.life = 1;

        this.score = 0;
        this.level = 1;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }
}

package com.example.drawabletest.game.game_tools.statistics;

import android.content.Context;

public class Statistic {

    private int life;
    private int score;
    private int level;
    private String difficulty;
    private String controller;
    private String username;


    //in this method 'context' is passed to get the preferences saved in the settings activity
    public Statistic(Context context) {
        this.difficulty = context.getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE).getString("difficulty", "classic");
        this.controller = context.getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE).getString("controller", "accelerometer");
        this.username = context.getSharedPreferences("com.example.drawabletest", Context.MODE_PRIVATE).getString("username", "");
        if(difficulty.equals("classic")) //if the selected difficulty mode is classic
            this.life = 3; //the the gamer has 3 lives
        else    //if the selected difficulty mode is hard
            this.life = 1; //the the gamer has only 1 life

        this.score = 0; //starting score
        this.level = 1; //starting level
    }

    /*
        GET AND SET METHODS
     */
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


    public String getController() {
        return controller;
    }


    public String getUsername() {
        return username;
    }


}

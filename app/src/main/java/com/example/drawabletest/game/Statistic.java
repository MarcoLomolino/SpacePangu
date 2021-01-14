package com.example.drawabletest.game;

public class Statistic {

    private int life;
    private int score;
    private int level;

    public Statistic(int life, int score, int level) {
        this.life = life;
        this.score = score;
        this.level = level;
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
}

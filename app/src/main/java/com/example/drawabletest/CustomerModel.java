package com.example.drawabletest;

public class CustomerModel {
    private int id;
    private int score;

    public CustomerModel(int id, int score) {
        this.id = id;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Punteggio-->" +
                "score=" + score;
    }
}

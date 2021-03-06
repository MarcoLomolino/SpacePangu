package com.example.drawabletest.databases.model;


public class ModelScore {
    private int id;
    private final String nome;
    private int score;

    public ModelScore(int id, String nome, int score) {
        this.id = id;
        this.score = score;
        this.nome = nome;
    }

    public ModelScore(int id, int score) {
        this.id = id;
        this.nome = "";
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

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Punteggio-->" +
                "score=" + score;
    }
}

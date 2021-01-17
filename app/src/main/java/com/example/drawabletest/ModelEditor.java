package com.example.drawabletest;

public class ModelEditor {
    private int x;
    private int y;
    private int attivo;

    public ModelEditor(int x, int y, int attivo) {
        this.x = x;
        this.y = y;
        this.attivo = attivo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAttivo() {
        return attivo;
    }

    public void setAttivo(int attivo) {
        this.attivo = attivo;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "x=" + x +
                ", y=" + y +
                ", attivo=" + attivo +
                '}';
    }
}


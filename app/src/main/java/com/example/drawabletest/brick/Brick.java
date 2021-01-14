package com.example.drawabletest.brick;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.example.drawabletest.game.Game;
import com.example.drawabletest.position.Position;

public abstract class Brick extends View {

    private Bitmap graphic_brick;
    private Position position;
    private float x;
    private float y;
    private int score;
    private int lives;

    public Brick(Context context, Position position, int score, int lives)
    {
        super(context);
        this.position = position;
        this.setGraphic_brick();
        this.score = score;
        this.lives = lives;
    }

    public Brick(Context context, float x, float y)
    {
        super(context);
        this.x = x;
        this.y = y;
        this.setGraphic_brick();
    }

    public float getXPosition()
    {
        return this.position.getX();
    }

    public float getYPosition()
    {
        return this.position.getY();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGraphic_brick(Bitmap graphic_brick) {
        this.graphic_brick = graphic_brick;
    }

    public Bitmap getGraphic_brick() {
        return graphic_brick;
    }

    public Position getPosition() {
        return position;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    /* public void setPosition(Position position) {
        this.position = position;
    }*/

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "Brick{" +
                "graphic_brick=" + graphic_brick +
                ", position=" + position +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public abstract void setGraphic_brick();

    public abstract void setEffect(Game game);

    //assegna un'immagine casuale al mattone
  /*  private void setGraphic_brick() {
        int a = (int) (Math.random() * 8);
        switch (a) {
            case 0:
                graphic_brick =  BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua);
                break;
            case 1:
                graphic_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue);
                break;
            case 2:
                graphic_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_green);
                break;
            case 3:
                graphic_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange);
                break;
            case 4:
                graphic_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink);
                break;
            case 5:
                graphic_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple);
                break;
            case 6:
                graphic_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_red);
                break;
            case 7:
                graphic_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow);
                break;
        }
    }*/


}

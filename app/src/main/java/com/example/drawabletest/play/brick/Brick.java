package com.example.drawabletest.play.brick;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.example.drawabletest.play.game.Game;
import com.example.drawabletest.play.position.Position;

public abstract class Brick extends View {

    private Bitmap graphic_brick;//brick texture
    private Position position;//x and y coordinates of the brick
    private float x;
    private float y;
    private int score;//the score of a single brick
    private int lives;//how many hit are needed to destroy a brick

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

	//a texture is assigned to the brick
    public abstract void setGraphic_brick();

	//the brick produce an effect 
    public abstract void setEffect(Game game);



}

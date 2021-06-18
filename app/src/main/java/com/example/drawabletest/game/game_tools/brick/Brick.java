package com.example.drawabletest.game.game_tools.brick;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.example.drawabletest.game.play.EditedGame;
import com.example.drawabletest.game.play.SinglePlayer;
import com.example.drawabletest.game.game_tools.position.Position;
import com.example.drawabletest.game.play.SinglePlayerLandscape;

public abstract class Brick extends View {

    private Bitmap graphic_brick;//brick texture
    private final Position position;//x and y coordinates of the brick
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
                '}';
    }

	//a texture is assigned to the brick
    public abstract void setGraphic_brick();

	//the brick produce an effect 
    public abstract void setEffect(SinglePlayer singlePlayer);

    //the brick produce an effect 
    public abstract void setEffect(SinglePlayerLandscape singlePlayerLandscape);

    //the brick produce an effect
    public abstract void setEffect(EditedGame game);

    public abstract String getType();



}

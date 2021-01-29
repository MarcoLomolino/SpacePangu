package com.example.drawabletest.play.paddle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.example.drawabletest.R;
import com.example.drawabletest.play.position.Position;

@SuppressLint("ViewConstructor")
public class Paddle extends View {
    private Bitmap graphic_paddle;
    private Position position;

    public Paddle(Context context, Position position)
    {
        super(context);
        this.position = position;
    }

    public Paddle(Context context, float x, float y) {
        super(context);
        this.position = new Position(x, y);
        this.graphic_paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
    }

    public float getXPosition(){ return position.getX();}

    public float getYPosition(){ return position.getY();}

    public void setXPosition(float x){this.position.setX(x);}

    /*public void setYPosition(int y){this.position.setY(y);}*/

    /*public Position getPosition() {
        return position;
    }*/

    public Bitmap getGraphic_paddle() {
        return graphic_paddle;
    }

   /* public void setGraphic_paddle(Bitmap graphic_paddle) {
        this.graphic_paddle = graphic_paddle;
    }

    public void setPosition(Position position) {
        this.position = position;
    }*/

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "Paddle{" +
                "graphic_paddle=" + graphic_paddle +
                ", position=" + position +
                '}';
    }
}

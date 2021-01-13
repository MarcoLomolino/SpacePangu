package com.example.drawabletest.brick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.View;

import com.example.drawabletest.R;
import com.example.drawabletest.position.Position;

public class Brick extends View {

    private Bitmap graphic_brick;
    private Position position;
    private float x;
    private float y;

    public Brick(Context context, Position position)
    {
        super(context);
        this.position = position;
        this.setGraphic_brick();
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

    public Bitmap getGraphic_brick() {
        return graphic_brick;
    }

    public Position getPosition() {
        return position;
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

    //assegna un'immagine casuale al mattone
    private void setGraphic_brick() {
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
    }


}

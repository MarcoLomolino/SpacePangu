package com.example.drawabletest.brick.sample_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.R;
import com.example.drawabletest.brick.Brick;
import com.example.drawabletest.game.Game;
import com.example.drawabletest.position.Position;

public class SampleBrick extends Brick {

    public SampleBrick(Context context, Position position) {
        super(context, position);
        super.setScore(80);
    }


    @Override
    public void setGraphic_brick() {
        int a = (int) (Math.random() * 5);
        switch (a) {
            case 0:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua));
                break;
            case 1:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue));
                break;
            case 2:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange));
                break;
            case 3:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink));
                break;
            case 4:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_red));
                break;
        }
    }

    @Override
    public void setEffect(Game game) {

    }


}

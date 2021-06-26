package com.example.drawabletest.game.game_tools.brick.sample_brick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.drawabletest.game.play.AbstractGame;

import com.example.drawabletest.R;
import com.example.drawabletest.game.game_tools.brick.Brick;

import com.example.drawabletest.game.game_tools.position.Position;


@SuppressLint("ViewConstructor")
public class SimpleBrick extends Brick {

    public SimpleBrick(Context context, Position position) {
        super(context, position, 80, 1);//set the brick position, a score of 80 and only a life
    }

	
	//set a random image of the brick
    @Override
    public void setGraphic_brick() {
        int a = (int) (Math.random() * 7);

        switch (a) {
            case 0:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua));
                Log.d("BRICK COLOR", "ACQUA");
                break;
            case 1:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue));
                Log.d("BRICK COLOR", "BLUE");
                break;
            case 2:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange));
                Log.d("BRICK COLOR", "ORANGE");
                break;
            case 3:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink));
                Log.d("BRICK COLOR", "PINK");
                break;
            case 4:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple));
                Log.d("BRICK COLOR", "PURPLE");
                break;
            case 5:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_red));
                Log.d("BRICK COLOR", "RED");
                break;
            case 6:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow));
                Log.d("BRICK COLOR", "YELLOW");
                break;
        }
    }
	
	//a simple brick produce none effect
    @Override
    public void setEffect(AbstractGame abstractGame) {

    }


    @Override
    public String getType() {
        return "simple";
    }
}

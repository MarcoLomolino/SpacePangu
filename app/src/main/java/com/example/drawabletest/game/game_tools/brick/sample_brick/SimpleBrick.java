package com.example.drawabletest.game.game_tools.brick.sample_brick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
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
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple));
                break;
            case 5:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_red));
                break;
            case 6:
                super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow));
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

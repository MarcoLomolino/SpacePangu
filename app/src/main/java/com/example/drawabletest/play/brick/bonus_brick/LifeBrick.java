package com.example.drawabletest.play.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.R;

import com.example.drawabletest.play.brick.Brick;
import com.example.drawabletest.play.game.Game;
import com.example.drawabletest.play.game.Statistic;
import com.example.drawabletest.play.position.Position;

public class LifeBrick extends Brick {

    public LifeBrick(Context context, Position position) {
        super(context, position, 20, 1);//assign a position, a score of 20 and only a life
    }


	
    @Override
    public void setGraphic_brick()
    {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_healthup));
    }
	
	//player's life is increased by one
    @Override
    public void setEffect(Game game)
    {
        int life = game.getStatistic().getLife();

        game.getStatistic().setLife(life + 1);
    }
}

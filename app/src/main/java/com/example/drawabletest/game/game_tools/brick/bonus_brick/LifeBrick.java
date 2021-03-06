package com.example.drawabletest.game.game_tools.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.game.play.AbstractGame;
import com.example.drawabletest.game.play.EditedGame;
import com.example.drawabletest.R;

import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.play.SinglePlayer;
import com.example.drawabletest.game.game_tools.position.Position;
import com.example.drawabletest.game.play.SinglePlayerLandscape;

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
    public void setEffect(AbstractGame abstractGame)
    {
        int life = abstractGame.getStatistic().getLife();

        abstractGame.getStatistic().setLife(life + 1);
    }



    @Override
    public String getType() {
        return "life";
    }
}

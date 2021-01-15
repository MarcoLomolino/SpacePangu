package com.example.drawabletest.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.R;
import com.example.drawabletest.brick.Brick;
import com.example.drawabletest.game.Game;
import com.example.drawabletest.game.Statistic;
import com.example.drawabletest.position.Position;

public class ScoreBrick extends Brick {

    public ScoreBrick(Context context, Position position) {
        super(context, position, 0, 1);//set position, 0 as score and only a life
    }

    @Override
    public void setGraphic_brick() {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_gold));
    }

	//double the actual player score TO CHANGE IN ANOTHER EFFECT
    @Override
    public void setEffect(Game game) {

        int life = game.getStatistic().getLife();
        int score = game.getStatistic().getScore();
        int level = game.getStatistic().getLevel();
        game.setStatistic(new Statistic(life, score + 200, level));

    }
}

package com.example.drawabletest.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.R;

import com.example.drawabletest.game.Game;
import com.example.drawabletest.game.Statistic;
import com.example.drawabletest.position.Position;

public class LifeBrick extends BonusBrick {

    public LifeBrick(Context context, Position position) {
        super(context, position);
    }



    @Override
    public void setGraphic_brick()
    {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_green));
    }

    @Override
    public void setEffect(Game game)
    {
        int life = game.getStatistic().getLife();
        int score = game.getStatistic().getScore();
        int level = game.getStatistic().getLevel();
        game.setStatistic(new Statistic(life + 1, score, level));

    }
}

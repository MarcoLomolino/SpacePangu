package com.example.drawabletest.game.game_tools.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.game.play.EditedGame;
import com.example.drawabletest.R;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.play.SinglePlayer;
import com.example.drawabletest.game.game_tools.position.Position;

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
    public void setEffect(SinglePlayer singlePlayer) {

        int score = singlePlayer.getStatistic().getScore();

        singlePlayer.getStatistic().setScore(score + 200);

    }

    @Override
    public void setEffect(EditedGame game) {

        int score = game.getStatistic().getScore();

        game.getStatistic().setScore(score + 200);

    }

    @Override
    public String getType() {
        return "score";
    }
}

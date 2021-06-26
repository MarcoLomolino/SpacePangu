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

public class SlowDownBrick extends Brick {

    public SlowDownBrick(Context context, Position position) {
        super(context, position, 20, 1);
    }

    @Override
    public void setGraphic_brick() {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.slow_brick));
    }

    @Override
    public void setEffect(AbstractGame abstractGame) {
        float directionX = 0, directionY = 0;

        if (abstractGame.getBall().getDirection().getX() > 0) {
            directionX = abstractGame.getBall().getMIN_X();
        } else if (abstractGame.getBall().getDirection().getX() < 0) {
            directionX = -abstractGame.getBall().getMIN_X();
        }
        if (abstractGame.getBall().getDirection().getY() > 0) {
            directionY = -abstractGame.getBall().getMAX_Y();
        } else if (abstractGame.getBall().getDirection().getY() < 0) {
            directionY = abstractGame.getBall().getMAX_Y();
        }

        abstractGame.getBall().resetPaddleHit();

        Position direction = new Position(directionX, directionY);
        abstractGame.setBallDirection(direction);
    }



    @Override
    public String getType() {
        return "slowdown";
    }
}

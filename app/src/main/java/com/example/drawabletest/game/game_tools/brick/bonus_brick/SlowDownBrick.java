package com.example.drawabletest.game.game_tools.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.game.play.EditedGame;
import com.example.drawabletest.R;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.play.SinglePlayer;
import com.example.drawabletest.game.game_tools.position.Position;

public class SlowDownBrick extends Brick {

    public SlowDownBrick(Context context, Position position) {
        super(context, position, 20, 1);
    }

    @Override
    public void setGraphic_brick() {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.slow_brick));
    }

    @Override
    public void setEffect(SinglePlayer singlePlayer) {
        float directionX = 0, directionY = 0;

        if (singlePlayer.getBall().getDirection().getX() > 0) {
            directionX = singlePlayer.getBall().getMIN_X();
        } else if (singlePlayer.getBall().getDirection().getX() < 0) {
            directionX = -singlePlayer.getBall().getMIN_X();
        }
        if (singlePlayer.getBall().getDirection().getY() > 0) {
            directionY = -singlePlayer.getBall().getMAX_Y();
        } else if (singlePlayer.getBall().getDirection().getY() < 0) {
            directionY = singlePlayer.getBall().getMAX_Y();
        }

        singlePlayer.getBall().resetPaddleHit();

        Position direction = new Position(directionX, directionY);
        singlePlayer.setBallDirection(direction);
    }

    @Override
    public void setEffect(EditedGame game) {
        float directionX = 0, directionY = 0;

        if (game.getBall().getDirection().getX() > 0) {
            directionX = game.getBall().getMIN_X();
        } else if (game.getBall().getDirection().getX() < 0) {
            directionX = -game.getBall().getMIN_X();
        }
        if (game.getBall().getDirection().getY() > 0) {
            directionY = -game.getBall().getMAX_Y();
        } else if (game.getBall().getDirection().getY() < 0) {
            directionY = game.getBall().getMAX_Y();
        }

        game.getBall().resetPaddleHit();

        Position direction = new Position(directionX, directionY);
        game.setBallDirection(direction);
    }

    @Override
    public String getType() {
        return "slowdown";
    }
}
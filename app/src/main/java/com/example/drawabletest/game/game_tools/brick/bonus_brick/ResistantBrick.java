package com.example.drawabletest.game.game_tools.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.game.play.EditedGame;
import com.example.drawabletest.R;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.play.SinglePlayer;
import com.example.drawabletest.game.game_tools.position.Position;

public class ResistantBrick extends Brick {

    public ResistantBrick(Context context, Position position) {
        super(context, position, 40, 3);//set a position, 40 as score and 3 lives
    }

    @Override
    public void setGraphic_brick() {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_metal));

    }


	//after an hit its texture change
    @Override
    public void setEffect(SinglePlayer singlePlayer) {

        super.setLives(super.getLives() - 1);
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_metalbroken));
    }

    @Override
    public void setEffect(EditedGame game) {

        super.setLives(super.getLives() - 1);
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_metalbroken));
    }

    @Override
    public String getType() {
        return "resistant";
    }
}

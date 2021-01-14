package com.example.drawabletest.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.R;
import com.example.drawabletest.game.Game;
import com.example.drawabletest.position.Position;

public class ResistantBrick extends BonusBrick{

    public ResistantBrick(Context context, Position position) {
        super(context, position, 40, 4);
    }

    @Override
    public void setGraphic_brick() {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_red));

    }

    @Override
    public void setEffect(Game game) {
        super.setLives(super.getLives() - 1);
    }
}

package com.example.drawabletest.brick.bonus_brick;

import android.content.Context;
import com.example.drawabletest.brick.Brick;
import com.example.drawabletest.game.Game;
import com.example.drawabletest.position.Position;

public abstract class BonusBrick extends Brick {


    public BonusBrick(Context context, Position position) {
        super(context, position);
        super.setScore(20);
    }







}

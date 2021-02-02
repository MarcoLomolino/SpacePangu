package com.example.drawabletest.game.play;

import android.content.Context;

import com.example.drawabletest.sounds.SoundPlayer;

public interface Game {

    void setBricks(Context context);

    void checkWalls();

    void checkVictory();

    void checkLifes(boolean b);

    void update();

    void setBackground();

    //set tha ball, the wall and the bricks
    void resetLevel(double v);

    void loadSounds(SoundPlayer sp);
}

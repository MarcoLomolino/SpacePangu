package com.example.drawabletest.play;

import android.content.Context;

public interface Game {

    void setBricks(Context context);


    void checkWalls();

    void checkVictory();

    void checkLifes(boolean b);

    void update();

    void setBackground();

    //set tha ball, the wall and the bricks
    void resetLevel(double v);
}

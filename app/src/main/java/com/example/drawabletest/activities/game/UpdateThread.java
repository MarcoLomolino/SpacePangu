package com.example.drawabletest.activities.game;

import android.os.Handler;

public class UpdateThread extends Thread {
    private Handler updatovaciHandler;
    private boolean play;

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public UpdateThread(Handler uh) {
        super();
        updatovaciHandler = uh;
        play = true;
    }

    public void run() {
        while (play) {
            try {
                sleep(32);
            } catch (Exception ex) {
            }
            updatovaciHandler.sendEmptyMessage(0);
        }
    }
}

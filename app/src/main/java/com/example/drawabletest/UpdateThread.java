package com.example.drawabletest;

import android.os.Handler;

public class UpdateThread extends Thread {
    Handler updatovaciHandler;
    boolean play;

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

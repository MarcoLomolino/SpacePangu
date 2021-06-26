/*
    Author: AmongBugs
 */
package com.example.drawabletest.game.play;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import com.example.drawabletest.game.game_tools.ball.Ball;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.game_tools.paddle.Paddle;
import com.example.drawabletest.game.game_tools.position.Position;
import com.example.drawabletest.game.game_tools.statistics.Statistic;
import com.example.drawabletest.sounds.SoundPlayer;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractGame extends View {

    private Ball ball;      //The ball used in game
    private Paddle paddle;  //The paddle used in game to avoid dropping the ball

    private CopyOnWriteArrayList<Brick> wall;   //A set of bricks
    private Statistic statistic;    //The match data and preferences

    private final SensorManager sManager;
    private final Sensor accelerometer;     //accelerometer'll used in case the player has chosen it

    private Point size;     //size of interactive screen

    private boolean start;      //check if the player is currently playing
    private boolean gameOver;
    private boolean stato;

    private Context context;    //context of activity where to set background and graphic side of game components (ball, paddle, ...)
    private final SoundPlayer sp;

    //constructor
    public AbstractGame(Context context) {
        super(context); //used for View class, to check which activity have to be drawn;

        this.context = context;
        this.statistic = new Statistic(context);    //set game statistics

        this.start = false;     //start is set to false, the player has not still touch the screen and the ball is not moving
        this.gameOver = false;  //gameover is set to false, the player is not even playing currently
        this.stato = false;

        // accelerometer SensorManager
        this.sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);   //setting up the sensor manager
        this.accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);          //initializing the accelerometer

        this.sp = new SoundPlayer(this.context);    //setting up the soundplayer
        this.loadSounds(sp);                        //loading start sound

        this.setBackground();               //set background image
        this.getSize();                     //get screen size
        this.resetLevel(size.y / 1.35);  //initialize ball, paddle and bricks
        this.setBricks(this.context);       //set brick's coordinates
    }

    //get display size (it's not hardware size but the size of the interactable activity)
    protected void getSize()
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    public void setBallDirection(Position direction) {
        this.ball.setDirection(direction);
    }


    /*
        ABSTRACT METHODS
     */
    abstract protected void setBackground();

    abstract protected void resetLevel(double v);

    abstract protected void setBricks(Context context);

    abstract protected void checkWalls();

    abstract protected void checkLives(boolean b);

    abstract protected void checkVictory();

    abstract public void update();

    abstract protected void loadSounds(SoundPlayer sp);

    abstract protected void playButtonSound();




    /*
        GET AND SET METHODS
     */
    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public CopyOnWriteArrayList<Brick> getWall() {
        return wall;
    }

    public void setWall(CopyOnWriteArrayList<Brick> wall) { this.wall = wall; }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public SensorManager getsManager() {
        return sManager;
    }

    public Sensor getAccelerometer() {
        return accelerometer;
    }

    public int getSizeX() {
        return this.size.x;
    }

    public int getSizeY() {
        return this.size.y;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public SoundPlayer getSp() {
        return sp;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }
}

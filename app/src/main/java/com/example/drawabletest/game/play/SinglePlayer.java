/*
    Author: AmongBugs
 */
package com.example.drawabletest.game.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import com.example.drawabletest.databases.model.ModelScore;
import com.example.drawabletest.databases.DatabaseHelper;
import com.example.drawabletest.databases.DatabaseRemote;
import com.example.drawabletest.R;
import com.example.drawabletest.game.game_tools.ball.Ball;
import com.example.drawabletest.game.game_tools.paddle.Paddle;
import com.example.drawabletest.game.game_tools.statistics.Statistic;
import com.example.drawabletest.sounds.SoundPlayer;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.LifeBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.ResistantBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.ScoreBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.SlowDownBrick;
import com.example.drawabletest.game.game_tools.brick.sample_brick.SimpleBrick;
import com.example.drawabletest.game.game_tools.position.Position;
import java.util.concurrent.CopyOnWriteArrayList;

public class SinglePlayer extends AbstractGame implements View.OnTouchListener, SensorEventListener {

    private Bitmap background;
    
    private final Paint paint;

    private int wallSound, brickSound, specialBrickSound, deathSound, gameoverSound, paddleSound;


    @SuppressLint("ClickableViewAccessibility")
    public SinglePlayer(Context context) {
        super(context);

        this.paint = new Paint();
        this.setOnTouchListener(this);
    }

    /*
        GRAPHIC METHOD
     */

    //get a Bitmap object from drawable resources and set it to background field
    @Override
    protected void setBackground() {
        this.background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
    }




    //main method of drawing elements
    protected void onDraw(Canvas canvas) {

        this.drawBackground(canvas);

        this.drawBall(canvas);

        this.drawPaddle(canvas);

        this.drawBricks(canvas);

        this.drawInfoText(canvas);

        this.drawGameOver(canvas);
    }


    private void drawBackground(Canvas canvas) {

        Bitmap roztiahnuty = Bitmap.createScaledBitmap(background, super.getSizeX(), super.getSizeY(), false);
        canvas.drawBitmap(roztiahnuty, 0, 0, paint);
    }

    private void drawGameOver(Canvas canvas) {
        if (super.isGameOver()) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", (float)super.getSizeX() / 4, (float)super.getSizeY() / 2, paint);
            if(super.getStatistic().getUsername().length() > 2 && !super.isStato()){
                DatabaseRemote db = new DatabaseRemote(super.getContext(), super.getStatistic().getDifficulty());
                db.insertDati(super.getStatistic().getUsername(),String.valueOf(super.getStatistic().getScore()));
                super.setStato(true);
            }
        }
    }

    private void drawInfoText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("" + super.getStatistic().getLife(), 400, 100, paint);
        canvas.drawText("" + super.getStatistic().getScore(), 700, 100, paint);
    }

    private void drawBricks(Canvas canvas) {
        RectF r;
        for (Brick b : super.getWall())
        {
            r = new RectF(b.getXPosition(), b.getYPosition(), b.getXPosition() + 100, b.getYPosition() + 80);
            canvas.drawBitmap(b.getGraphic_brick(), null, r, paint);
        }

    }

    private void drawPaddle(Canvas canvas) {
        paint.setColor(Color.WHITE);
        RectF r = new RectF(super.getPaddle().getXPosition(), super.getPaddle().getYPosition(), super.getPaddle().getXPosition() + 200, super.getPaddle().getYPosition() + 40);
        canvas.drawBitmap(super.getPaddle().getGraphic_paddle(), null, r, paint);
    }

    private void drawBall(Canvas canvas) {
        paint.setColor(Color.RED);
        canvas.drawBitmap(super.getBall().getGraphic_ball(), super.getBall().getXPosition(), super.getBall().getYPosition(), paint);
    }

    /*
        SET VALUES OBJECTS
     */
    @Override
    protected void setBricks(Context context) {
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 6; j++) {
                System.out.print(i + "-" + j + ": ");
                //coordinates of each brick
                Position position = new Position(j * 150, i * 100);

                //set percentage spawn for bricks type
                double a =  Math.random() * 100;
                if(super.getStatistic().getDifficulty().equals("hard")) { //if difficult is hard there is no life brick
                    if(a >= 30)
                        super.getWall().add(new SimpleBrick(context, position));
                    else if(a >= 13)
                        super.getWall().add(new ResistantBrick(context, position));
                    else if(a >= 3)
                        super.getWall().add(new ScoreBrick(context, position));
                    else if(a >= 0)
                        super.getWall().add(new SlowDownBrick(context, position));
                } else { //if difficult is standard there are all types of bricks
                    if (a >= 27)
                        super.getWall().add(new SimpleBrick(context, position));
                    else if (a >= 17)
                        super.getWall().add(new ScoreBrick(context, position));
                    else if (a >= 6)
                        super.getWall().add(new ResistantBrick(context, position));
                    else if (a >= 3)
                        super.getWall().add(new SlowDownBrick(context, position));
                    else if (a >= 0)
                        super.getWall().add(new LifeBrick(context, position));
                }
            }
        }
    }

    //set tha super.getBall(), the super.getWall() and the bricks
    @Override
    protected void resetLevel(double v) {
        super.setBall(new Ball(super.getContext(), (float)super.getSizeX() / 2, (float) v, super.getStatistic().getDifficulty()));
        this.setPaddle(new Paddle(super.getContext(), (float)super.getSizeX() / 2, (float) ((float)super.getSizeY() / 1.25)));
        super.setWall(new CopyOnWriteArrayList<>());
    }

    /*
        CONTROL METHODS
     */

    //check if the super.getBall() hit a super.getWall()
    @Override
    protected void checkWalls() {
        if (super.getBall().getXPosition() + super.getBall().getDirection().getX() >= super.getSizeX() - 60) {
            super.getBall().changeDirection("prava");
            super.getSp().playSound(wallSound, 0.99f);
        } else if (super.getBall().getXPosition() + super.getBall().getDirection().getX() <= 0) {
            super.getBall().changeDirection("lava");
            super.getSp().playSound(wallSound, 0.99f);
        } else if (super.getBall().getYPosition() + super.getBall().getDirection().getX() <= 150) {
            super.getBall().changeDirection("hore");
            super.getSp().playSound(wallSound, 0.99f);
        } else if (super.getBall().getYPosition() + super.getBall().getDirection().getX() >= super.getSizeY() - 200) {
            this.checkLives(true);
        }
    }


    @Override
    protected void checkLives(boolean b) {
        if (super.getStatistic().getLife() == 1) {//if the player lose
            ModelScore modelScore = new ModelScore(-1, super.getStatistic().getScore());
            DatabaseHelper databaseHelper = new DatabaseHelper(super.getContext());
            if(super.getStatistic().getDifficulty().equals("classic")){
                databaseHelper.addOne(modelScore,0); //set point in DB to show in top ten leaderboard
            }
            else{
                databaseHelper.addOne(modelScore,1); //set point in DB to show in top ten leaderboard
            }
           super.setGameOver(true); //set game over as true
           super.setStart(false);
           super.getSp().playSound(gameoverSound, 0.90f);
           invalidate();

        } else { //if the player can still play this match
            super.getStatistic().setLife(super.getStatistic().getLife() - 1);//decrease the life
            super.getSp().playSound(deathSound, 0.90f);
            super.setBall(new Ball(super.getContext(), (float)super.getSizeX() / 2, (float) (super.getSizeY() / 1.35), super.getStatistic().getDifficulty()));//set super.getBall() in the start
            super.setStart(false);
        }
    }

    //check if the player has won
    @Override
    protected void checkVictory() {
        if (super.getWall().isEmpty()) { //if there are no bricks
            super.getSp().releaseSP();
            loadSounds(super.getSp());
            super.getStatistic().setLevel(super.getStatistic().getLevel() + 1); //increase the level
            resetLevel((float) (super.getSizeY() / 1.35));
            setBricks(super.getContext());
            super.setStart(false); //wait to move the super.getBall() until the first touch of the player
        }
    }

    //main method call by PlayActivity. It manage game status
    @Override
    public void update() {
        Ball ball; //ball is initialize here as object to prevent lag in game
        if (super.isStart()) {//if the player touch the screen the first time
            checkVictory(); //check if the player has won
            ball = super.getBall();
            checkWalls(); //check if the ball hits a super.getWall()
            if(ball.nearPaddle(super.getPaddle().getXPosition(), super.getPaddle().getYPosition())) {
                super.getSp().playSound(paddleSound, 0.99f);
                ball.increaseSpeed(ball.getPaddlehit());
            }
             //check if the ball hits the super.getPaddle()
            for (Brick b : super.getWall()) { //for each brick
                if (ball.isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    if(b.getLives() == 1) //if the hitted brick has only a life
                        super.getWall().remove(b);	//then remove it

                    if(b.getType().equals("life") || b.getType().equals("score") || b.getType().equals("slowdown"))
                        super.getSp().playSound(specialBrickSound, 0.99f);
                    else
                        super.getSp().playSound(brickSound, 0.99f);

                    b.setEffect(this); //set the brick effect
                    super.getStatistic().setScore(super.getStatistic().getScore() + b.getScore()); //add brick score to the match general score
                }
            }
            ball.move(); //move the ball
        }
    }


    /*
        SOUND METHODS
     */

    @Override
    protected void loadSounds(SoundPlayer sp) {
        wallSound = sp.loadSound(R.raw.drum_low_28);
        brickSound = sp.loadSound(R.raw.drum_low_03);
        specialBrickSound = sp.loadSound(R.raw.pp_24);
        deathSound = sp.loadSound(R.raw.balldie);
        gameoverSound = sp.loadSound(R.raw.gameover);
        paddleSound = sp.loadSound(R.raw.drum_low_04);
    }

    @Override
    protected void playButtonSound() {
        final MediaPlayer beepMP = MediaPlayer.create(super.getContext(), R.raw.menu_101);
        beepMP.setOnPreparedListener(MediaPlayer::start);
        beepMP.setOnCompletionListener(MediaPlayer::release);
    }


    /*
        SENSOR METHODS
     */

    @Override
    public void onSensorChanged(SensorEvent event) {

        Paddle paddle = super.getPaddle(); //paddle and x are initialized to prevent lag in game
        float x = paddle.getXPosition();
        if (super.getStatistic().getController().equals("accelerometer") && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            paddle.setXPosition((x - 2 * event.values[0]));
            x = paddle.getXPosition();

            if (x + event.values[0] > super.getSizeX() - 240) {
                paddle.setXPosition(super.getSizeX() - 240);
            } else if (x - event.values[0] <= 20) {
                paddle.setXPosition(20);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void stopScanning() {
        super.getsManager().unregisterListener(this);
    }

    public void runScanning() {
        super.getsManager().registerListener(this, super.getAccelerometer(), SensorManager.SENSOR_DELAY_GAME);
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(super.getStatistic().getController().equals("drag") && event.getAction()==MotionEvent.ACTION_MOVE)
        {
            float x= event.getX();

            if(x <= super.getPaddle().getXPosition() + 160 && x >= super.getPaddle().getXPosition() - 160) {
                super.getPaddle().setXPosition((int) x);

                if (super.getPaddle().getXPosition() > super.getSizeX() - 240) {
                    super.getPaddle().setXPosition(super.getSizeX() - 240);
                } else if (super.getPaddle().getXPosition() <= 20) {
                    super.getPaddle().setXPosition(20);
                }
            }
        }
        else
        {
            this.setStart(true); //used in other methods to check if the super.getBall() can move itself
            if (super.isGameOver()) {//if the player has lost and touches the screen

                super.setStatistic(new Statistic(super.getContext()));
                resetLevel(super.getSizeY() / 1.35);
                setBricks(super.getContext());
                super.setGameOver(false);
                super.setStato(false);
            }
        }


        return true;
    }





}

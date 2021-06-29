package com.example.drawabletest.game.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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


import android.view.MotionEvent;
import android.view.View;


import com.example.drawabletest.databases.DatabaseHelper;
import com.example.drawabletest.databases.model.ModelEditor;
import com.example.drawabletest.R;
import com.example.drawabletest.sounds.SoundPlayer;
import com.example.drawabletest.game.game_tools.ball.Ball;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.LifeBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.ResistantBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.ScoreBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.SlowDownBrick;
import com.example.drawabletest.game.game_tools.brick.sample_brick.SimpleBrick;
import com.example.drawabletest.game.game_tools.statistics.Statistic;
import com.example.drawabletest.game.game_tools.paddle.Paddle;
import com.example.drawabletest.game.game_tools.position.Position;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ViewConstructor")
public class EditedGame extends AbstractGame implements View.OnTouchListener, SensorEventListener {

    private Bitmap background;
    private final Paint paint;
    private int wallSound, brickSound, specialBrickSound, deathSound, gameoverSound, paddleSound;

    @SuppressLint("ClickableViewAccessibility")
    public EditedGame(Context context) {
        super(context);

        this.paint = new Paint();

        this.setOnTouchListener(this);
    }

    /*
        GRAPHIC METHOD
     */

    //get a Bitmap object from drawable resources and set it to background field
    @Override
    public void setBackground() {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
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

    @Override
    protected void setBricks(Context context) {
        DatabaseHelper dbh = new DatabaseHelper(context);
        SharedPreferences mPrefs = context.getSharedPreferences("salva_map",MODE_PRIVATE);
        int slot = mPrefs.getInt("view_mode",1);
        List<ModelEditor> wall1 = dbh.getEditorFile(slot);
        for(ModelEditor w : wall1) {
            if(w.getAttivo()!=0){
                Position position = new Position((w.getX()+1) * 150, (w.getY()+3) * 100);
                if(w.getAttivo() == 1)
                    super.getWall().add(new SimpleBrick(context, position));
                else if(w.getAttivo()==2)
                    super.getWall().add(new LifeBrick(context, position));
                else if(w.getAttivo()==3)
                    super.getWall().add(new ScoreBrick(context, position));
                else if(w.getAttivo()==4)
                    super.getWall().add(new ResistantBrick(context, position));
                else if(w.getAttivo()==5)
                    super.getWall().add(new SlowDownBrick(context, position));
            }
        }
    }

    //check if the ball hit a wall
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
            super.setGameOver(true); //set game over as true
            super.setStart(false);
            super.getSp().playSound(gameoverSound, 0.90f);
            invalidate();
        } else { //if the player can still play this match
            super.getStatistic().setLife(super.getStatistic().getLife() - 1);//decrease the life
            super.getSp().playSound(deathSound, 0.90f);
            super.setBall(new Ball(super.getContext(), (float)super.getSizeX() / 2, (float) (super.getSizeY() / 1.35), super.getStatistic().getDifficulty()));//set ball in the start
            super.setStart(true);
        }
    }

    //main method call by PlayActivity. It manage game status
    @Override
    public void update() {
        Ball ball; //ball is initialize here as object to prevent lag in game
        if (super.isStart()) {//if the player touch the screen the first time
            checkVictory(); //check if the player has won
            ball = super.getBall();
            checkWalls(); //check if the ball hits a wall
            if(ball.nearPaddle(super.getPaddle().getXPosition(), super.getPaddle().getYPosition())) {
                super.getSp().playSound(paddleSound, 0.99f);
            }//check if the ball hits the paddle
            for (Brick b : super.getWall()) { //for each brick
                if (ball.isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    if(b.getLives() == 1) //if the hitted brick has only a life
                        super.getWall().remove(b);	//then remove it

                    LifeBrick temp1 = new LifeBrick(super.getContext(), b.getPosition());
                    ScoreBrick temp2 = new ScoreBrick(super.getContext(), b.getPosition());
                    SlowDownBrick temp3 = new SlowDownBrick(super.getContext(), b.getPosition());
                    if(b.getClass()==temp1.getClass() || b.getClass()==temp2.getClass() || b.getClass()==temp3.getClass()) {
                        super.getSp().playSound(specialBrickSound, 0.99f);
                    } else {
                        super.getSp().playSound(brickSound, 0.99f);
                    }

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

    }


    //check if the player has won
    @Override
    protected void checkVictory() {
        if (super.getWall().isEmpty()) { //if there are no bricks
            //playbuttonsound();
            super.getSp().releaseSP();
            loadSounds(super.getSp());
            super.getStatistic().setLevel(super.getStatistic().getLevel() + 1); //increase the level
            resetLevel(super.getSizeY() / 1.35);
            setBricks(super.getContext());
            super.setStart(false);//wait to move the ball until the first touch of the player
        }
    }

    /*private void reloadGameSoundPlayer(SoundPlayer sp) {
        wallSound = sp.loadSound(R.raw.drum_low_28);
        brickSound = sp.loadSound(R.raw.drum_low_03);
        specialBrickSound = sp.loadSound(R.raw.pp_24);
        deathSound = sp.loadSound(R.raw.balldie);
        gameoverSound = sp.loadSound(R.raw.gameover);
        paddleSound = sp.loadSound(R.raw.drum_low_04);
    }*/

    //set tha ball, the wall and the bricks
    @Override
    protected void resetLevel(double v) {
        super.setBall(new Ball(super.getContext(), (float)super.getSizeX() / 2, (float) v, super.getStatistic().getDifficulty()));
        super.setPaddle(new Paddle(super.getContext(), (float)super.getSizeX() / 2, super.getSizeY() - 400));
        super.setWall(new CopyOnWriteArrayList<>());
    }


    public void stopScanning() {
        super.getsManager().unregisterListener(this);
    }

    public void runScanning() {
        super.getsManager().registerListener(this, super.getAccelerometer(), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Paddle paddle = super.getPaddle(); //paddle and x are initialized to prevent lag in game
        float x = paddle.getXPosition();
        if (super.getStatistic().getController().equals("accelerometer") && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            paddle.setXPosition((int) (x - event.values[0] - event.values[0]));
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



    //the first touch
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(super.getStatistic().getController().equals("drag") && event.getAction()==MotionEvent.ACTION_MOVE)
        {
            float x= event.getX();

            super.getPaddle().setXPosition((int) x);

            if (super.getPaddle().getXPosition()> super.getSizeX() - 240) {
                super.getPaddle().setXPosition(super.getSizeX() - 240);
            } else if (super.getPaddle().getXPosition() <= 20) {
                super.getPaddle().setXPosition(20);
            }
        }
        else
        {
            super.setStart(true); //used in other methods to check if the ball can move itself
            if (super.isGameOver()) {//if the player has lost and touches the screen

                super.setStatistic(new Statistic(super.getContext()));
                resetLevel(super.getSizeY() / 1.35);
                setBricks(super.getContext());
                super.setGameOver(false);
            }
        }
        return true;
    }






}


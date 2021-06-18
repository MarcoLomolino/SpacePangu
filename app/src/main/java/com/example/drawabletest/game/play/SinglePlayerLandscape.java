package com.example.drawabletest.game.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.drawabletest.databases.model.ModelScore;
import com.example.drawabletest.databases.DatabaseHelper;
import com.example.drawabletest.databases.DatabaseRemote;
import com.example.drawabletest.R;
import com.example.drawabletest.sounds.SoundPlayer;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.game_tools.ball.Ball;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.LifeBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.ResistantBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.ScoreBrick;
import com.example.drawabletest.game.game_tools.brick.bonus_brick.SlowDownBrick;
import com.example.drawabletest.game.game_tools.brick.sample_brick.SimpleBrick;
import com.example.drawabletest.game.game_tools.statistics.Statistic;
import com.example.drawabletest.game.game_tools.paddle.Paddle;
import com.example.drawabletest.game.game_tools.position.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class SinglePlayerLandscape extends View implements View.OnTouchListener, SensorEventListener, Game {

    private Bitmap background;

    private Ball ball;
    private Paddle paddle;
    private CopyOnWriteArrayList<Brick> wall;
    private Statistic statistic;

    private Point size;
    private final Paint paint;

    private final SensorManager sManager;
    private final Sensor accelerometer;

    private boolean start;
    private boolean gameOver;

    private final Context context;

    private final SoundPlayer sp;
    int wallSound, brickSound, specialBrickSound, deathSound, gameoverSound, paddleSound;
    private boolean stato;

    @SuppressLint("ClickableViewAccessibility")
    public SinglePlayerLandscape(Context context) {
        super(context);

        this.context = context;
        this.statistic = new Statistic(context);
        this.paint = new Paint();
        this.stato = false;

        this.sp = new SoundPlayer(this.context);
        this.loadSounds(sp);

        //flag vars to start the game or to check a game over
        this.start = false;
        this.gameOver = false;

        // accelerometer SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.setBackground();
        this.getSize();//get screen size
        this.resetLevel(size.y / 1.35);//initialize ball, paddle and bricks

        this.setBricks(context);//set bricks coordinates position
        this.setOnTouchListener(this);
    }

    /*
        GRAPHIC METHOD
     */

    //get a Bitmap object from drawable resources and set it to background field
    @Override
    public void setBackground() {
        this.background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
    }

    //get display size (it's not hardware size but the size of the interactable activity)
    private void getSize()
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
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

        Bitmap roztiahnuty = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        canvas.drawBitmap(roztiahnuty, 0, 0, paint);
    }

    private void drawGameOver(Canvas canvas) {
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", (float)size.x / 4, (float)size.y / 2, paint);
            if(statistic.getUsername().length() > 2 && !stato){
                DatabaseRemote db = new DatabaseRemote(context, statistic.getDifficulty());
                db.insertDati(statistic.getUsername(),String.valueOf(statistic.getScore()));
                stato = true;
            }
        }
    }

    private void drawInfoText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("" + statistic.getLife(), 800, 50, paint);
        canvas.drawText("" + statistic.getScore(), 1350, 50, paint);
    }

    private void drawBricks(Canvas canvas) {
        RectF r;
        for (Brick b : wall)
        {
            r = new RectF(b.getXPosition(), b.getYPosition(), b.getXPosition() + 100, b.getYPosition() + 80);
            canvas.drawBitmap(b.getGraphic_brick(), null, r, paint);
        }

    }

    private void drawPaddle(Canvas canvas) {
        paint.setColor(Color.WHITE);
        RectF r = new RectF(paddle.getXPosition(), paddle.getYPosition(), paddle.getXPosition() + 200, paddle.getYPosition() + 40);
        canvas.drawBitmap(paddle.getGraphic_paddle(), null, r, paint);
    }

    private void drawBall(Canvas canvas) {
        paint.setColor(Color.RED);
        canvas.drawBitmap(ball.getGraphic_ball(), ball.getXPosition(), ball.getYPosition(), paint);
    }

    /*
        SET VALUES OBJECTS
     */
    @Override
    public void setBricks(Context context) {
        for (int i = 2; i < 4; i++) {
            for (int j = 2; j < 12; j++) {

                //coordinates of each brick
                Position position = new Position(j * 150, i * 100);

                //set percentage spawn for bricks type
                double a =  Math.random() * 100;
                if(statistic.getDifficulty().equals("hard")) { //if difficult is hard there is no life brick
                    if(a >= 30)
                        wall.add(new SimpleBrick(context, position));
                    else if(a >= 13)
                        wall.add(new ResistantBrick(context, position));
                    else if(a >= 3)
                        wall.add(new ScoreBrick(context, position));
                    else if(a >= 0)
                        wall.add(new SlowDownBrick(context, position));
                } else { //if difficult is standard there are all types of bricks
                    if (a >= 27)
                        wall.add(new SimpleBrick(context, position));
                    else if (a >= 17)
                        wall.add(new ScoreBrick(context, position));
                    else if (a >= 6)
                        wall.add(new ResistantBrick(context, position));
                    else if (a >= 3)
                        wall.add(new SlowDownBrick(context, position));
                    else if (a >= 0)
                        wall.add(new LifeBrick(context, position));
                }
            }
        }
    }

    //set tha ball, the wall and the bricks
    @Override
    public void resetLevel(double v) {
        this.ball = new Ball(context, (float)size.x / 2, (float) v - 100, statistic.getDifficulty());
        this.paddle = new Paddle(context, (float)size.x / 2, (float) ((float)size.y / 1.40));
        this.wall = new CopyOnWriteArrayList<>();
    }

    /*
        CONTROL METHODS
     */

    //check if the ball hit a wall
    @Override
    public void checkWalls() {
        if (ball.getXPosition() + ball.getDirection().getX() >= size.x - 60) {
            ball.changeDirection("prava");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getXPosition() + ball.getDirection().getX() <= 0) {
            ball.changeDirection("lava");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getYPosition() + ball.getDirection().getX() <= 150) {
            ball.changeDirection("hore");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getYPosition() + ball.getDirection().getX() >= size.y - 200) {
            checkLifes(true);
        }
    }


    @Override
    public void checkLifes(boolean b) {
        if (statistic.getLife() == 1) {//if the player lose
            ModelScore modelScore = new ModelScore(-1, statistic.getScore());
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            if(statistic.getDifficulty().equals("classic")){
                databaseHelper.addOne(modelScore,0); //set point in DB to show in top ten leaderboard
            }
            else{
                databaseHelper.addOne(modelScore,1); //set point in DB to show in top ten leaderboard
            }
            gameOver = true; //set game over as true
            start = false;
            sp.playSound(gameoverSound, 0.90f);
            invalidate();

        } else { //if the player can still play this match
            statistic.setLife(statistic.getLife() - 1);//decrease the life
            sp.playSound(deathSound, 0.90f);
            ball = new Ball(context, (float)size.x / 2, size.y - 480, statistic.getDifficulty());//set ball in the start
            start = false;
        }
    }

    //check if the player has won
    @Override
    public void checkVictory() {
        if (wall.isEmpty()) { //if there are no bricks
            //playButtonSound();
            sp.releaseSP();
            loadSounds(sp);
            statistic.setLevel(statistic.getLevel() + 1); //increase the level
            resetLevel((float) (size.y / 1.35));
            setBricks(context);
            start = false; //wait to move the ball until the first touch of the player
        }
    }

    //main method call by PlayActivity. It manage game status
    @Override
    public void update() {
        if (start) {//if the player touch the screen the first time
            checkVictory(); //check if the player has won
            checkWalls(); //check if the ball hits a wall
            if(ball.nearPaddle(paddle.getXPosition(), paddle.getYPosition())) {
                sp.playSound(paddleSound, 0.99f);
                ball.increaseSpeed(ball.getPaddlehit());
            }
            //check if the ball hits the paddle
            for (Brick b : wall) { //for each brick
                if (ball.isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    if(b.getLives() == 1) //if the hitted brick has only a life
                        wall.remove(b);	//then remove it

                    if(b.getType().equals("life") || b.getType().equals("score") || b.getType().equals("slowdown"))
                        sp.playSound(specialBrickSound, 0.99f);
                    else
                        sp.playSound(brickSound, 0.99f);

                    b.setEffect(this); //set the brick effect
                    statistic.setScore(statistic.getScore() + b.getScore()); //add brick score to the match general score
                }
            }
            this.ball.move(); //move the ball
        }
    }


    /*
        SOUND METHODS
     */

    @Override
    public void loadSounds(SoundPlayer sp) {
        wallSound = sp.loadSound(R.raw.drum_low_28);
        brickSound = sp.loadSound(R.raw.drum_low_03);
        specialBrickSound = sp.loadSound(R.raw.pp_24);
        deathSound = sp.loadSound(R.raw.balldie);
        gameoverSound = sp.loadSound(R.raw.gameover);
        paddleSound = sp.loadSound(R.raw.drum_low_04);
    }

    private void playButtonSound() {
        final MediaPlayer beepMP = MediaPlayer.create(context, R.raw.menu_101);
        beepMP.setOnPreparedListener(MediaPlayer::start);
        beepMP.setOnCompletionListener(MediaPlayer::release);
    }


    /*
        SENSOR METHODS
     */

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (statistic.getController().equals("accelerometer") && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            paddle.setXPosition((int) (paddle.getXPosition() - event.values[0] - event.values[0]));

            if (paddle.getXPosition() + event.values[0] > size.x - 240) {
                paddle.setXPosition(size.x - 240);
            } else if (paddle.getXPosition() - event.values[0] <= 20) {
                paddle.setXPosition(20);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void stopScanning() {
        sManager.unregisterListener(this);
    }

    public void runScanning() {
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(statistic.getController().equals("drag") && event.getAction()==MotionEvent.ACTION_MOVE)
        {
            float x= event.getX();

            if(x <= paddle.getXPosition() + 160 && x >= paddle.getXPosition() - 160) {
                paddle.setXPosition((int) x);

                if (paddle.getXPosition() > size.x - 240) {
                    paddle.setXPosition(size.x - 240);
                } else if (paddle.getXPosition() <= 20) {
                    paddle.setXPosition(20);
                }
            }
        }
        else
        {
            start = true; //used in other methods to check if the ball can move itself
            if (gameOver) {//if the player has lost and touches the screen

                statistic = new Statistic(context);
                resetLevel(size.y / 1.35);
                setBricks(context);
                gameOver = false;
                stato = false;
            }
        }


        return true;
    }


    /*
        get and set
     */

    public Statistic getStatistic() {
        return statistic;
    }

    public Ball getBall() {return ball; }

    public void setBallDirection(Position direction) {
        ball.setDirection(direction);
    }



}


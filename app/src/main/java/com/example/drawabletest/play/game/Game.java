package com.example.drawabletest.play.game;

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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.drawabletest.CustomerModel;
import com.example.drawabletest.DatabaseHelper;
import com.example.drawabletest.R;
import com.example.drawabletest.play.brick.Brick;
import com.example.drawabletest.play.ball.Ball;
import com.example.drawabletest.play.brick.bonus_brick.LifeBrick;
import com.example.drawabletest.play.brick.bonus_brick.ResistantBrick;
import com.example.drawabletest.play.brick.bonus_brick.ScoreBrick;
import com.example.drawabletest.play.brick.sample_brick.SampleBrick;
import com.example.drawabletest.play.paddle.Paddle;
import com.example.drawabletest.play.position.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends View implements View.OnTouchListener, SensorEventListener {

    private Bitmap background;
    private Bitmap roztiahnuty;

    private Ball ball;
    private Paddle paddle;
    private CopyOnWriteArrayList<Brick> wall;

    private Point size;
    private final Paint paint;

    private final SensorManager sManager;
    private final Sensor accelerometer;

    private Statistic statistic;
    private String difficulty;

    private boolean start;
    private boolean gameOver;
    private final Context context;

    public Game(Context context, String difficolta) {
        super(context);
        difficulty = difficolta;

        if (difficulty.equals("difficult")) {//if hard mode has been set
            this.statistic = new Statistic(1,0,1); //only a life is setted, 0 actual score and start from level 1
        } else {
            //if standard mode has been set
            this.statistic = new Statistic(3, 0, 1); //3 lives, 0 actual score, start from level 1
        }
        //set playActivity context
        this.context = context;
        paint = new Paint();


        //flag vars to start the game or to check a game over
        start = false;
        gameOver = false;

        // accelerometer SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.setBackground();//set background image
        this.getSize();//get screen size
        this.resetLevel();//initialize ball, paddle and bricks

        setBricks(context);//set bricks coordinates position
        this.setOnTouchListener(this);
    }

    /*
        GRAPHIC METHOD
     */

    //get a Bitmap object from drawable resources and set it to background field
    private void setBackground() {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
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
        if (roztiahnuty == null) { //create background only once
            roztiahnuty = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(roztiahnuty, 0, 0, paint);

    }

    private void drawGameOver(Canvas canvas) {
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", (float)size.x / 4, (float)size.y / 2, paint);
        }
    }

    private void drawInfoText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("" + statistic.getLife(), 400, 100, paint);
        canvas.drawText("" + statistic.getScore(), 700, 100, paint);
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

    private void setBricks(Context context) {
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 6; j++) {

                //coordinates of each brick
                Position position = new Position(j * 150, i * 100);

                //set percentage spawn for bricks type
                int a = (int) (Math.random() * 100);
                if(difficulty.equals("difficult")) { //if difficult is hard there is no life brick
                    if(a >= 20)
                        wall.add(new SampleBrick(context, position));
                    else if(a >= 10 && a < 20)
                        wall.add(new ResistantBrick(context, position));
                    else if(a >= 0 && a < 10)
                        wall.add(new ScoreBrick(context, position));
                } else { //if difficult is standard there are all types of bricks
                    if (a >= 20)
                        wall.add(new SampleBrick(context, position));
                    else if (a >= 10 && a < 20)
                        wall.add(new ScoreBrick(context, position));
                    else if (a >= 3 && a < 10)
                        wall.add(new ResistantBrick(context, position));
                    else if (a >= 0 && a < 3)
                        wall.add(new LifeBrick(context, position));
                }
            }
        }
    }

    //check if the ball hit a wall
    private void checkWalls() {
        if (ball.getXPosition() + ball.getDirection().getX() >= size.x - 60) {
            ball.changeDirection("prava");
        } else if (ball.getXPosition() + ball.getDirection().getX() <= 0) {
            ball.changeDirection("lava");
        } else if (ball.getYPosition() + ball.getDirection().getX() <= 150) {
            ball.changeDirection("hore");
        } else if (ball.getYPosition() + ball.getDirection().getX() >= size.y - 200) {
            checkLives();
        }
    }


        private void checkLives() {
        if (statistic.getLife() == 1) {//if the player lose
            CustomerModel customerModel = new CustomerModel(-1, statistic.getScore());
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            boolean success = databaseHelper.addOne(customerModel); //set point in DB to show in top ten leaderboard
            gameOver = true; //set game over as true
            start = false; 
            invalidate();
        } else { //if the player can still play this match 
            statistic.setLife(statistic.getLife() - 1);//decrease the life
            ball = new Ball(context, (float)size.x / 2, size.y - 480, difficulty);//set ball in the start
            start = false;
        }
    }

    //main method call by PlayActivity. It manage game status
    public void update() {
        if (start) {//if the player touch the screen the first time
            victory(); //check if the player has won
            checkWalls(); //check if the ball hits a wall
            ball.nearPaddle(paddle.getXPosition(), paddle.getYPosition()); //check if the ball hits the paddle
            for (Brick b : wall) { //for each brick
                if (ball.isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    if(b.getLives() == 1) //if the hitted brick has only a life
                        wall.remove(b);	//then remove it

                    b.setEffect(this); //set the brick effect
                    statistic.setScore(statistic.getScore() + b.getScore()); //add brick score to the match general score
                }
            }
            ball.move(); //move the ball
        }
    }


    //check if the player has won
    private void victory() {
        if (wall.isEmpty()) { //if there are no bricks
            statistic.setLevel(statistic.getLevel() + 1); //increase the level
            resetLevel();
            setBricks(context);
            start = false; //wait to move the ball until the first touch of the player
        }
    }

    //set tha ball, the wall and the bricks
    private void resetLevel() {
        ball = new Ball(context, (float)size.x / 2, size.y - 480, difficulty);
        paddle = new Paddle(context, (float)size.x / 2, size.y - 400);
        wall = new CopyOnWriteArrayList<>();
    }


    public void stopScanning() {
        sManager.unregisterListener(this);
    }

    public void runScanning() {
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }


    //the first touch 
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(start && event.getAction()==MotionEvent.ACTION_DOWN)
        {
            int x=(int)event.getX();

            int move;

            if(x > size.x / 2)
                move = - 80;
            else
                move = + 80;

            paddle.setXPosition((int) (paddle.getXPosition() - move));

            if (paddle.getXPosition() + move > size.x - 240) {
                paddle.setXPosition(size.x - 240);
            } else if (paddle.getXPosition() - move <= 20) {
                paddle.setXPosition(20);
            }
        }
        else
        {
            start = true; //used in other methods to check if the ball can move itself
            if (gameOver && start) {//if the player has lost and touches the screen
                //prova committ
                if (difficulty.equals("difficult")) {
                    statistic = new Statistic(1,0,1);
                } else {
                    statistic = new Statistic(3, 0, 1);
                }
                resetLevel();
                setBricks(context);
                gameOver = false;

            }
        }


        return false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

}

package com.example.drawabletest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.drawabletest.play.ball.Ball;
import com.example.drawabletest.play.brick.Brick;
import com.example.drawabletest.play.brick.sample_brick.SampleBrick;
import com.example.drawabletest.play.paddle.Paddle;
import com.example.drawabletest.play.position.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class Multigame extends View implements View.OnTouchListener {

    private Bitmap background;
    private Bitmap roztiahnuty;

    private Ball ball;
    private Paddle paddle;
    private Paddle paddle2;
    private CopyOnWriteArrayList<Brick> wall;

    private Point size;
    private final Paint paint;

    private final SensorManager sManager;
    private final Sensor accelerometer;

    private boolean start;
    private boolean gameOver;
    private final Context context;
    SoundPlayer sp;
    int wallSound, brickSound, victorySound, paddleSound;;


    private int player_score1;
    private int player_score2;

    public Multigame(Context context) {
        super(context);

        this.context = context;
        this.paint = new Paint();


        sp = new SoundPlayer(this.context);
        sp.createSP();
        wallSound = sp.loadSound(R.raw.drum_low_28);
        brickSound = sp.loadSound(R.raw.drum_low_03);
        victorySound = sp.loadSound(R.raw.pp_05);
        paddleSound = sp.loadSound(R.raw.drum_low_04);

        //flag vars to start the game or to check a game over
        this.start = false;
        this.gameOver = false;


        // accelerometer SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.setBackground();//set background image
        this.getSize();//get screen size
        this.resetLevel(480, 0, 0);//initialize ball, paddle and bricks

        this.setBricks(context);//set bricks coordinates position
        this.setOnTouchListener(this);

    }

    /*
        GRAPHIC METHOD
     */

    //get a Bitmap object from drawable resources and set it to background field
    private void setBackground() {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.versuslayout));
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

        this.drawPaddles(canvas);

        this.drawBricks(canvas);

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


    private void drawBricks(Canvas canvas) {
        RectF r;
        for (Brick b : wall)
        {
            r = new RectF(b.getXPosition(), b.getYPosition(), b.getXPosition() + 100, b.getYPosition() + 80);
            canvas.drawBitmap(b.getGraphic_brick(), null, r, paint);
        }

    }

    private void drawPaddles(Canvas canvas) {
        paint.setColor(Color.WHITE);
        RectF r = new RectF(paddle.getXPosition(), paddle.getYPosition(), paddle.getXPosition() + 200, paddle.getYPosition() + 40);
        canvas.drawBitmap(paddle.getGraphic_paddle(), null, r, paint);

        paint.setColor(Color.WHITE);
        r = new RectF(paddle2.getXPosition(), paddle2.getYPosition(), paddle2.getXPosition() + 200, paddle2.getYPosition() + 40);
        canvas.drawBitmap(paddle2.getGraphic_paddle(), null, r, paint);
    }

    private void drawBall(Canvas canvas) {
        paint.setColor(Color.RED);
        canvas.drawBitmap(ball.getGraphic_ball(), ball.getXPosition(), ball.getYPosition(), paint);
    }

    private void setBricks(Context context) {
        for (int j = 1; j < 6; j++) {

            //coordinates of each brick
            Position position = new Position(j * 150, 10 * 100);

            wall.add(new SampleBrick(context, position));
        }
    }

    //check if the ball hit a wall
    private void checkWalls() {
        if (ball.getXPosition() + ball.getDirection().getX() >= size.x - 60) {
            ball.changeDirection("prava");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getXPosition() + ball.getDirection().getX() <= 0) {
            ball.changeDirection("lava");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getYPosition() + ball.getDirection().getX() <= 0) {
            player_score1++;
            checkLives( true );

        } else if (ball.getYPosition() + ball.getDirection().getX() >= size.y - 200) {
            player_score2++;
            checkLives( false );
        }
    }

    //check if the player has won
    private void victory() {

        start = false; //wait to move the ball until the first touch of the player
        gameOver = true;

        sp.playSound(victorySound, 0.90f);
        playbuttonsound(R.raw.pp_05);
        sp.releaseSP();
        wallSound = sp.loadSound(R.raw.drum_low_28);
        brickSound = sp.loadSound(R.raw.drum_low_03);
        victorySound = sp.loadSound(R.raw.pp_05);
        paddleSound = sp.loadSound(R.raw.drum_low_04);
        resetLevel(480, 0, 0);
        setBricks(context);

    }


    private void checkLives(boolean b) {
        if(player_score1 == 3 || player_score2 == 3)
            victory();
        else
        {
            sp.playSound(brickSound, 0.90f);

            if(b)
            {
                this.resetLevel(1920, this.player_score1, this.player_score2);
                ball.getDirection().setX(- ball.getDirection().getX());
                ball.getDirection().setY(- ball.getDirection().getY());
            }
            else
                this.resetLevel(480, this.player_score1, this.player_score2);

            start = false;
            invalidate();
            setBricks(context);
        }
    }

    //main method call by PlayActivity. It manage game status
    public void update() {
        if (start) {//if the player touch the screen the first time

            checkWalls(); //check if the ball hits a wall
            if(ball.nearPaddle(paddle.getXPosition(), paddle.getYPosition()))
                sp.playSound(paddleSound, 0.99f);
            else if(ball.nearPaddle(paddle2.getXPosition(), paddle2.getYPosition()))
                sp.playSound(paddleSound, 0.99f);

            for (Brick b : wall) { //for each brick
                if (ball.isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    wall.remove(b);	//then remove it
                    sp.playSound(brickSound, 0.99f);
                }
            }
            ball.move(); //move the ball
        }
    }




    //set tha ball, the wall and the bricks
    private void resetLevel(int ball_position, int player_score1, int player_score2) {
        ball = new Ball(context, (float)size.x / 2, size.y - ball_position);
        paddle = new Paddle(context, (float)size.x / 2, size.y - 400);
        paddle2 = new Paddle(context, (float)size.x / 2, size.y - 2000);
        wall = new CopyOnWriteArrayList<>();
        this.player_score1 = player_score1;
        this.player_score2 = player_score2;

    }



    //the first touch
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            int i;
            int pointer = event.getPointerCount();
            for (i = 0; i < pointer; i++) {
                float x = event.getX(i);
                float y = event.getY(i);
                if (y > size.y / 2) {
                    paddle.setXPosition((int) x);
                    if (paddle.getXPosition() > size.x - 240) {
                        paddle.setXPosition(size.x - 240);
                    } else if (paddle.getXPosition() <= 20) {
                        paddle.setXPosition(20);
                    }
                } else {
                    paddle2.setXPosition((int) x);
                    if (paddle2.getXPosition() > size.x - 240) {
                        paddle2.setXPosition(size.x - 240);
                    } else if (paddle2.getXPosition() <= 20) {
                        paddle2.setXPosition(20);
                    }
                }
            }
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            this.start = true;
            this.gameOver = false;
        }

        return true;
    }



    public Ball getBall() {return ball; }

    public void setBallDirection(/*Position position,*/ Position direction) {
        //ball.setPosition(position);
        ball.setDirection(direction);
    }


    private void playbuttonsound(int resource) {
        final MediaPlayer beepMP = MediaPlayer.create(context, resource);
        beepMP.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mprelease(beepMP);
    }

    private void mprelease(MediaPlayer soundmp) {
        soundmp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }
}




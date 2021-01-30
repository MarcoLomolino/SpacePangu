package com.example.drawabletest.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.drawabletest.R;
import com.example.drawabletest.SoundPlayer;
import com.example.drawabletest.play.ball.Ball;
import com.example.drawabletest.play.brick.Brick;
import com.example.drawabletest.play.brick.sample_brick.SimpleBrick;
import com.example.drawabletest.play.paddle.Paddle;
import com.example.drawabletest.play.position.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class MultiPlayer extends View implements View.OnTouchListener, Game {

    private Bitmap background;
    private Bitmap roztiahnuty;

    private Ball ball;
    private Paddle paddle;
    private Paddle paddle2;
    private CopyOnWriteArrayList<Brick> wall;

    private Point size;
    private final Paint paint;

    private boolean start;
    private boolean gameOver;
    private final Context context;
    private final SoundPlayer sp;
    int wallSound, brickSound, paddleSound;


    private int player_score1 = 0;
    private int player_score2 = 0;
    private int winner;

    @SuppressLint("ClickableViewAccessibility")
    public MultiPlayer(Context context) {
        super(context);

        this.context = context;
        this.paint = new Paint();

        sp = new SoundPlayer(this.context);
        this.loadSounds(sp);

        //flag vars to start the game or to check a game over
        this.start = false;
        this.gameOver = false;

        this.setBackground();//set background image
        this.getSize();//get screen size
        this.resetLevel(size.y / 1.35);//initialize ball, paddle and bricks
        this.setScores(0, 0);

        this.setBricks(context);//set bricks coordinates position
        this.setOnTouchListener(this);

    }

    /*
        GRAPHIC METHOD
     */

    //get a Bitmap object from drawable resources and set it to background field
    @Override
    public void setBackground() {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.versuslayout));

    }

    //get display size (it's not hardware size but the size of the interactable activity)
    private void getSize()
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //Display display = context.getDisplay();
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

        this.drawScore(canvas);
    }


    private void drawBackground(Canvas canvas) {
        if (roztiahnuty == null) { //create background only once
            roztiahnuty = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(roztiahnuty, 0, 0, paint);

    }

    private void drawScore(Canvas canvas){
         paint.setColor(Color.RED);
         paint.setTextSize(100);
         canvas.rotate(90,(float) size.x / 2 ,(float) size.y / 2);
         canvas.drawText(player_score2 + " - " + player_score1,(float)size.x / 2 - 110, (float)(size.y / 3), paint);
         canvas.rotate(-90,(float) size.x / 2 ,(float) size.y / 2);
    }

    private void drawGameOver(Canvas canvas) {
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.rotate(90,(float) size.x / 2 ,(float) size.y / 2);
            if(winner == 1){
                canvas.drawText(context.getResources().getString(R.string.player1wins), (float)size.x / 2, (float)size.y / 2, paint);
            }
            else {
                canvas.drawText(context.getResources().getString(R.string.player2wins), (float)size.x / 2, (float)size.y / 2, paint);
            }
            canvas.rotate(-90,(float) size.x / 2 ,(float) size.y / 2);

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

    @Override
    public void setBricks(Context context) {
        for (int j = 1; j < 6; j++) {

            //coordinates of each brick
            Position position = new Position(j * 150, (float)(size.y/2) - 64);

            wall.add(new SimpleBrick(context, position));
        }
    }



    //check if the ball hit a wall
    @Override
    public void checkWalls() {
        if (ball.getXPosition() + ball.getDirection().getX() >= size.x - 60) {
            ball.changeDirection("prava");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getXPosition() + ball.getDirection().getX() <= 0) {
            ball.changeDirection("lava");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getYPosition() + ball.getDirection().getX() <= 0) {
            player_score1++;
            checkLifes( true );

        } else if (ball.getYPosition() + ball.getDirection().getX() >= size.y - 200) {
            player_score2++;
            checkLifes( false );
        }
    }


    //check if the player has won
    @Override
    public void checkVictory() {

        start = false; //wait to move the ball until the first touch of the player
        gameOver = true;

        if(player_score1==3)
            winner = 1;
        else
            winner = 2;

        playbuttonsound();
        sp.releaseSP();
        loadSounds(sp);
        resetLevel(size.y / 1.35);
        this.setScores(0, 0);
        setBricks(context);

    }

    @Override
    public void loadSounds(SoundPlayer sp) {
        wallSound = sp.loadSound(R.raw.drum_low_28);
        brickSound = sp.loadSound(R.raw.drum_low_03);
        paddleSound = sp.loadSound(R.raw.drum_low_04);
    }


    public void checkLifes(boolean b) {
        if(player_score1 == 3 || player_score2 == 3) //if a player has 3 as score
            checkVictory();
        else
        {
            if(b) //if player 1 scores
            {
                this.resetLevel((float)size.y / 13);
                ball.getDirection().setX(- ball.getDirection().getX());//set opposite direction for player 2's first throw of the ball
                ball.getDirection().setY(- ball.getDirection().getY());
            }
            else//if player 2 scores
                this.resetLevel((float) (size.y / 1.35));

            this.setScores(this.player_score1, this.player_score2);
            playbuttonsound();
            sp.releaseSP();
            loadSounds(sp);
            start = false;
            invalidate();
            setBricks(context);
        }
    }

    private void setScores(int player_score1, int player_score2) {
        this.player_score1 = player_score1;
        this.player_score2 = player_score2;
    }

    //main method call by PlayActivity. It manage game status
    public void update() {
        if (start) {//if the player touch the screen the first time

            this.checkWalls(); //check if the ball hits a wall
            if(ball.nearPaddle(paddle.getXPosition(), paddle.getYPosition()))
            {
                sp.playSound(paddleSound, 0.99f);
                this.ball.increaseSpeed();
            }
            else if(ball.nearPaddle(paddle2.getXPosition(), paddle2.getYPosition()))
                sp.playSound(paddleSound, 0.99f);

            for (Brick b : wall) { //for each brick
                if (ball.isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    this.wall.remove(b);	//then remove it
                    this.sp.playSound(brickSound, 0.99f);
                }
            }
            this.ball.move(); //move the ball
        }
    }




    //set tha ball, the wall and the bricks
    @Override
    public void resetLevel(double ball_position) {
        this.ball = new Ball(context, (float)size.x / 2, (float) ball_position);
        this.paddle = new Paddle(context, (float)size.x / 2, (float) (size.y / 1.25));
        this.paddle2 = new Paddle(context, (float)size.x / 2, (float) size.y / 20);
        this.wall = new CopyOnWriteArrayList<>();
    }


    //operations on drag and on touch
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int i;
            int pointer = event.getPointerCount();
            for (i = 0; i < pointer; i++) {//for each touch gets the coordinates
                float x = event.getX(i);
                float y = event.getY(i);

            if (y > (float) size.y / 2) { //if the tap comes from the lowest half part of the screen
                if (x <= paddle.getXPosition() + 160 && x >= paddle.getXPosition() - 160) { //if the finger is on the paddle, in a range of +-160 from it
                    paddle.setXPosition(x); //move the paddle
                    if (paddle.getXPosition() > size.x - 240)   //if the paddle touches the right wall
                        paddle.setXPosition(size.x - 240); //stop here
                    else if (paddle.getXPosition() <= 20)   //if the paddle touches the left wall
                        paddle.setXPosition(20);    //stop here
                }
            }
            else {  //if the tap comes from the highest half part of the screen
                if (x <= paddle2.getXPosition() + 160 && x >= paddle2.getXPosition() - 160) { //if the finger is on the paddle, in a range of +-160 from it
                    paddle2.setXPosition(x); //move the paddle
                    if (paddle2.getXPosition() > size.x - 240) //if the paddle touches the right wall
                        paddle2.setXPosition(size.x - 240);//stop here
                    else if (paddle2.getXPosition() <= 20)//if the paddle touches the left wall
                        paddle2.setXPosition(20);//stop here
                }
            }
        }
    }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//on tap
            this.start = true;  //let's the match begin
            this.gameOver = false; //set previous game over to false
        }
        return true;
    }




    private void playbuttonsound() {
        final MediaPlayer beepMP = MediaPlayer.create(context, R.raw.levelup);
        beepMP.setOnPreparedListener(MediaPlayer::start);
        beepMP.setOnCompletionListener(MediaPlayer::release);
    }

}




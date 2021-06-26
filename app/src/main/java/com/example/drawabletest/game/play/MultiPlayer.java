package com.example.drawabletest.game.play;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.RectF;
import android.media.MediaPlayer;

import android.view.MotionEvent;
import android.view.View;


import com.example.drawabletest.R;
import com.example.drawabletest.game.game_tools.ball.Ball;
import com.example.drawabletest.game.game_tools.paddle.Paddle;
import com.example.drawabletest.sounds.SoundPlayer;
import com.example.drawabletest.game.game_tools.brick.Brick;
import com.example.drawabletest.game.game_tools.brick.sample_brick.SimpleBrick;
import com.example.drawabletest.game.game_tools.position.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class MultiPlayer extends AbstractGame implements View.OnTouchListener {

    private Bitmap background;
    
    private Paddle paddle2;

    private final Paint paint;

    private int wallSound, brickSound, paddleSound;


    private int player_score1;
    private int player_score2;
    private int winner;

    @SuppressLint("ClickableViewAccessibility")
    public MultiPlayer(Context context) {
        super(context);
        
        this.paint = new Paint();
        
        this.setScores(0, 0);
        
        this.setOnTouchListener(this);

    }

    /*
        GRAPHIC METHOD
     */

    //get a Bitmap object from drawable resources and set it to background field
    @Override
    protected void setBackground() {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.versuslayout));

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
        Bitmap roztiahnuty = Bitmap.createScaledBitmap(background, super.getSizeX(), super.getSizeY(), false);
        canvas.drawBitmap(roztiahnuty, 0, 0, paint);

    }

    private void drawScore(Canvas canvas){
         paint.setColor(Color.RED);
         paint.setTextSize(100);
         canvas.rotate(90,(float) super.getSizeX() / 2 ,(float) super.getSizeY() / 2);
         canvas.drawText(player_score2 + " - " + player_score1,(float)super.getSizeX() / 2 - 110, (float)(super.getSizeY() / 3), paint);
         canvas.rotate(-90,(float) super.getSizeX() / 2 ,(float) super.getSizeY() / 2);
    }

    private void drawGameOver(Canvas canvas) {
        if (super.isGameOver()) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.rotate(90,(float) super.getSizeX() / 2 ,(float) super.getSizeY() / 2);
            if(winner == 1){
                canvas.drawText(super.getContext().getResources().getString(R.string.player1wins), (float)super.getSizeX() / 2, (float)super.getSizeY() / 2, paint);
            }
            else {
                canvas.drawText(super.getContext().getResources().getString(R.string.player2wins), (float)super.getSizeX() / 2, (float)super.getSizeY() / 2, paint);
            }
            canvas.rotate(-90,(float) super.getSizeX() / 2 ,(float) super.getSizeY() / 2);

        }
    }


    private void drawBricks(Canvas canvas) {
        RectF r;
        for (Brick b : super.getWall())
        {
            r = new RectF(b.getXPosition(), b.getYPosition(), b.getXPosition() + 100, b.getYPosition() + 80);
            canvas.drawBitmap(b.getGraphic_brick(), null, r, paint);
        }

    }

    private void drawPaddles(Canvas canvas) {
        paint.setColor(Color.WHITE);
        RectF r = new RectF(super.getPaddle().getXPosition(), super.getPaddle().getYPosition(), super.getPaddle().getXPosition() + 200, super.getPaddle().getYPosition() + 40);
        canvas.drawBitmap(super.getPaddle().getGraphic_paddle(), null, r, paint);

        paint.setColor(Color.WHITE);
        r = new RectF(paddle2.getXPosition(), paddle2.getYPosition(), paddle2.getXPosition() + 200, paddle2.getYPosition() + 40);
        canvas.drawBitmap(paddle2.getGraphic_paddle(), null, r, paint);
    }

    private void drawBall(Canvas canvas) {
        paint.setColor(Color.RED);
        canvas.drawBitmap(super.getBall().getGraphic_ball(), super.getBall().getXPosition(), super.getBall().getYPosition(), paint);
    }

    @Override
    protected void setBricks(Context context) {
        for (int j = 1; j < 6; j++) {

            //coordinates of each brick
            Position position = new Position(j * 150, (float)(super.getSizeY()/2) - 64);

            super.getWall().add(new SimpleBrick(super.getContext(), position));
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
        } else if (super.getBall().getYPosition() + super.getBall().getDirection().getX() <= 0) {
            player_score1++;
            checkLives( true );

        } else if (super.getBall().getYPosition() + super.getBall().getDirection().getX() >= super.getSizeY() - 200) {
            player_score2++;
            checkLives( false );
        }
    }


    //check if the player has won
    @Override
    protected void checkVictory() {

        super.setStart(false); //wait to move the ball until the first touch of the player
        super.setGameOver(true);

        if(player_score1==3)
            winner = 1;
        else
            winner = 2;

        playButtonSound();
        super.getSp().releaseSP();
        loadSounds(super.getSp());
        resetLevel(super.getSizeY() / 1.35);
        this.setScores(0, 0);
        setBricks(super.getContext());

    }

    @Override
    protected void loadSounds(SoundPlayer sp) {
        wallSound = super.getSp().loadSound(R.raw.drum_low_28);
        brickSound = super.getSp().loadSound(R.raw.drum_low_03);
        paddleSound = super.getSp().loadSound(R.raw.drum_low_04);
    }


    @Override
    protected void checkLives(boolean b) {
        if(player_score1 == 3 || player_score2 == 3) //if a player has 3 as score
            checkVictory();
        else
        {
            if(b) //if player 1 scores
            {
                this.resetLevel((float)super.getSizeY() / 13);
                super.getBall().getDirection().setX(- super.getBall().getDirection().getX());//set opposite direction for player 2's first throw of the ball
                super.getBall().getDirection().setY(- super.getBall().getDirection().getY());
            }
            else//if player 2 scores
                this.resetLevel((float) (super.getSizeY() / 1.35));

            this.setScores(this.player_score1, this.player_score2);
            playButtonSound();
            super.getSp().releaseSP();
            loadSounds(super.getSp());
            super.setStart(false);
            invalidate();
            setBricks(super.getContext());
        }
    }

    private void setScores(int player_score1, int player_score2) {
        this.player_score1 = player_score1;
        this.player_score2 = player_score2;
    }

    //main method call by PlayActivity. It manage game status
    @Override
    public void update() {
        if (super.isStart()) {//if the player touch the screen the first time

            this.checkWalls(); //check if the ball hits a wall
            if(super.getBall().nearPaddle(super.getPaddle().getXPosition(), super.getPaddle().getYPosition()))
            {
                super.getSp().playSound(paddleSound, 0.99f);
                super.getBall().increaseSpeed();
            }
            else if(super.getBall().nearPaddle(paddle2.getXPosition(), paddle2.getYPosition()))
                super.getSp().playSound(paddleSound, 0.99f);

            for (Brick b : super.getWall()) { //for each brick
                if (super.getBall().isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    super.getWall().remove(b);	//then remove it
                    super.getSp().playSound(brickSound, 0.99f);
                }
            }
            super.getBall().move(); //move the ball
        }
    }




    //set tha ball, the wall and the bricks
    @Override
    protected void resetLevel(double ball_position) {
        super.setBall(new Ball(super.getContext(), (float)super.getSizeX() / 2, (float) ball_position));
        super.setPaddle(new Paddle(super.getContext(), (float)super.getSizeX() / 2, (float) (super.getSizeY() / 1.25)));
        this.paddle2 = new Paddle(super.getContext(), (float)super.getSizeX() / 2, (float) super.getSizeY() / 20);
        super.setWall(new CopyOnWriteArrayList<>());
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

            if (y > (float) super.getSizeY() / 2) { //if the tap comes from the lowest half part of the screen
                if (x <= super.getPaddle().getXPosition() + 160 && x >= super.getPaddle().getXPosition() - 160) { //if the finger is on the paddle, in a range of +-160 from it
                    super.getPaddle().setXPosition(x); //move the paddle
                    if (super.getPaddle().getXPosition() > super.getSizeX() - 240)   //if the paddle touches the right wall
                        super.getPaddle().setXPosition(super.getSizeX() - 240); //stop here
                    else if (super.getPaddle().getXPosition() <= 20)   //if the paddle touches the left wall
                        super.getPaddle().setXPosition(20);    //stop here
                }
            }
            else {  //if the tap comes from the highest half part of the screen
                if (x <= paddle2.getXPosition() + 160 && x >= paddle2.getXPosition() - 160) { //if the finger is on the paddle, in a range of +-160 from it
                    paddle2.setXPosition(x); //move the paddle
                    if (paddle2.getXPosition() > super.getSizeX() - 240) //if the paddle touches the right wall
                        paddle2.setXPosition(super.getSizeX() - 240);//stop here
                    else if (paddle2.getXPosition() <= 20)//if the paddle touches the left wall
                        paddle2.setXPosition(20);//stop here
                }
            }
        }
    }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//on tap
            super.setStart(true);  //let's the match begin
            super.setGameOver(false); //set previous game over to false
        }
        return true;
    }



    @Override
    protected void playButtonSound() {
        final MediaPlayer beepMP = MediaPlayer.create(super.getContext(), R.raw.levelup);
        beepMP.setOnPreparedListener(MediaPlayer::start);
        beepMP.setOnCompletionListener(MediaPlayer::release);
    }

}




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
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.drawabletest.play.ball.Ball;
import com.example.drawabletest.play.brick.Brick;
import com.example.drawabletest.play.brick.bonus_brick.LifeBrick;
import com.example.drawabletest.play.brick.bonus_brick.ResistantBrick;
import com.example.drawabletest.play.brick.bonus_brick.ScoreBrick;
import com.example.drawabletest.play.brick.bonus_brick.SlowDownBrick;
import com.example.drawabletest.play.brick.sample_brick.SimpleBrick;
import com.example.drawabletest.play.game.Statistic;
import com.example.drawabletest.play.paddle.Paddle;
import com.example.drawabletest.play.position.Position;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EditedGame extends View implements View.OnTouchListener, SensorEventListener {

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

    private boolean start;
    private boolean gameOver;
    private final Context context;
    private int slot;
    SoundPlayer sp;
    int wallSound, brickSound, specialBrickSound;
    int deathSound, gameoverSound, paddleSound;

    public EditedGame(Context context,int slot) {
        super(context);

        this.slot = slot;
        this.context = context;
        this.statistic = new Statistic(context);
        this.paint = new Paint();

        sp = new SoundPlayer(this.context);
        sp.createSP();
        reloadGameSoundPlayer(sp);

        //flag vars to start the game or to check a game over
        this.start = false;
        this.gameOver = false;

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
        DatabaseHelper dbh = new DatabaseHelper(context);
        List<ModelEditor> wall1 = dbh.getEditorFile(slot);
        for(ModelEditor w : wall1) {
            if(w.getAttivo()!=0){
                Position position = new Position((w.getX()+1) * 150, (w.getY()+3) * 100);
                if(w.getAttivo() == 1)
                    wall.add(new SimpleBrick(context, position));
                else if(w.getAttivo()==2)
                    wall.add(new LifeBrick(context, position));
                else if(w.getAttivo()==3)
                    wall.add(new ScoreBrick(context, position));
                else if(w.getAttivo()==4)
                    wall.add(new ResistantBrick(context, position));
                else if(w.getAttivo()==5)
                    wall.add(new SlowDownBrick(context, position));
            }
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
        } else if (ball.getYPosition() + ball.getDirection().getX() <= 150) {
            ball.changeDirection("hore");
            sp.playSound(wallSound, 0.99f);
        } else if (ball.getYPosition() + ball.getDirection().getX() >= size.y - 200) {
            checkLives();
        }
    }


    private void checkLives() {
        if (statistic.getLife() == 1) {//if the player lose
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

    //main method call by PlayActivity. It manage game status
    public void update() {
        if (start) {//if the player touch the screen the first time
            victory(); //check if the player has won
            checkWalls(); //check if the ball hits a wall
            if(ball.nearPaddle(paddle.getXPosition(), paddle.getYPosition())) {
                sp.playSound(paddleSound, 0.99f);
            }; //check if the ball hits the paddle
            for (Brick b : wall) { //for each brick
                if (ball.isCollisionBrick(b.getPosition())) { //check if the ball hits this brick
                    if(b.getLives() == 1) //if the hitted brick has only a life
                        wall.remove(b);	//then remove it

                    LifeBrick temp1 = new LifeBrick(context, b.getPosition());
                    ScoreBrick temp2 = new ScoreBrick(context, b.getPosition());
                    SlowDownBrick temp3 = new SlowDownBrick(context, b.getPosition());
                    if(b.getClass()==temp1.getClass() || b.getClass()==temp2.getClass() || b.getClass()==temp3.getClass()) {
                        sp.playSound(specialBrickSound, 0.99f);
                    } else {
                        sp.playSound(brickSound, 0.99f);
                    }

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
            playbuttonsound(R.raw.levelup);
            sp.releaseSP();
            reloadGameSoundPlayer(sp);
            statistic.setLevel(statistic.getLevel() + 1); //increase the level
            resetLevel();
            setBricks(context);
            start = false; //wait to move the ball until the first touch of the player
        }
    }

    private void reloadGameSoundPlayer(SoundPlayer sp) {
        wallSound = sp.loadSound(R.raw.drum_low_28);
        brickSound = sp.loadSound(R.raw.drum_low_03);
        specialBrickSound = sp.loadSound(R.raw.pp_24);
        deathSound = sp.loadSound(R.raw.balldie);
        gameoverSound = sp.loadSound(R.raw.gameover);
        paddleSound = sp.loadSound(R.raw.drum_low_04);
    }

    //set tha ball, the wall and the bricks
    private void resetLevel() {
        ball = new Ball(context, (float)size.x / 2, size.y - 480, statistic.getDifficulty());
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

        if(statistic.getController().equals("drag") && event.getAction()==MotionEvent.ACTION_MOVE)
        {
            float x= event.getX();

            paddle.setXPosition((int) x);

            if (paddle.getXPosition()> size.x - 240) {
                paddle.setXPosition(size.x - 240);
            } else if (paddle.getXPosition() <= 20) {
                paddle.setXPosition(20);
            }

        }
        else
        {
            start = true; //used in other methods to check if the ball can move itself
            if (gameOver) {//if the player has lost and touches the screen

                statistic = new Statistic(context);
                resetLevel();
                setBricks(context);
                gameOver = false;
            }
        }


        return true;
    }


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

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public Ball getBall() {return ball; }

    public void setBallDirection(Position direction) {
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


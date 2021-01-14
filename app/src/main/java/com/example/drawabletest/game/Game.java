package com.example.drawabletest.game;

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
import com.example.drawabletest.PlayActivity;
import com.example.drawabletest.R;
import com.example.drawabletest.brick.Brick;
import com.example.drawabletest.ball.Ball;
import com.example.drawabletest.brick.bonus_brick.BonusBrick;
import com.example.drawabletest.brick.bonus_brick.LifeBrick;
import com.example.drawabletest.brick.bonus_brick.ResistantBrick;
import com.example.drawabletest.brick.bonus_brick.ScoreBrick;
import com.example.drawabletest.brick.sample_brick.SampleBrick;
import com.example.drawabletest.paddle.Paddle;
import com.example.drawabletest.position.Position;

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

    private boolean start;
    private boolean gameOver;
    private final Context context;

    public Game(Context context) {
        super(context);
        paint = new Paint();

        this.statistic = new Statistic(3, 0, 1);
        // nastavi context, zivoty, skore a level
        this.context = context;


        //flag vars to start the game or to check a game over
        start = false;
        gameOver = false;

        // accelerometer SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setBackground();//set background image
        getSize();//get screen size

        // vytvorí novú lopticku, pádlo, a zoznam tehliciek
        ball = new Ball(context, (float)size.x / 2, size.y - 480);
        paddle = new Paddle(context, (float)size.x / 2, size.y - 400);
        wall = new CopyOnWriteArrayList<>();

        setBricks(context);//set bricks coordinates position
        this.setOnTouchListener(this);
    }

    public Game(PlayActivity playActivity, int a)
    {
        super(playActivity);
        this.context = playActivity;
        paint = new Paint();
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }



    private void setBackground() {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
    }

    private void getSize()
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    private void setBricks(Context context) {
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 6; j++) {
                int a = (int) (Math.random() * 100);
                if(a >= 20)
                    wall.add(new SampleBrick(context, new Position(j * 150, i * 100)));
                else if(a >= 10 && a < 20)
                    wall.add(new LifeBrick(context, new Position(j * 150, i * 100)));
                else if(a >= 5 && a < 10)
                    wall.add(new ResistantBrick(context, new Position(j * 150, i * 100)));
                else if(a > 0 && a < 5)
                    wall.add(new ScoreBrick(context, new Position(j * 150, i * 100)));
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        //create background only once
        RectF r;
        if (roztiahnuty == null) {
            roztiahnuty = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(roztiahnuty, 0, 0, paint);

        //draw ball
        paint.setColor(Color.RED);
        canvas.drawBitmap(ball.getGraphic_ball(), ball.getXPosition(), ball.getYPosition(), paint);

        //draw paddle
        paint.setColor(Color.WHITE);
        r = new RectF(paddle.getXPosition(), paddle.getYPosition(), paddle.getXPosition() + 200, paddle.getYPosition() + 40);
        canvas.drawBitmap(paddle.getGraphic_paddle(), null, r, paint);

        //draw bricks
        paint.setColor(Color.GREEN);
        for (Brick b : wall)
        {
            r = new RectF(b.getXPosition(), b.getYPosition(), b.getXPosition() + 100, b.getYPosition() + 80);
            canvas.drawBitmap(b.getGraphic_brick(), null, r, paint);
        }

        //draw info text
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("" + statistic.getLife(), 400, 100, paint);
        canvas.drawText("" + statistic.getScore(), 700, 100, paint);

        //draw game over text
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", (float)size.x / 4, (float)size.y / 2, paint);
        }
    }

    // skontroluje či sa ball nedotkla okraju
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

    // skontroluje stav hry. či ma životy alebo či hra konči
    private void checkLives() {
        if (statistic.getLife() == 1) {
            CustomerModel customerModel = new CustomerModel(-1, statistic.getScore());
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            boolean success = databaseHelper.addOne(customerModel);
            gameOver = true;
            start = false;
            invalidate();
        } else {
            statistic.setLife(statistic.getLife() - 1);
            ball = new Ball(context, (float)size.x / 2, size.y - 480);
            start = false;
        }
    }

    // kazdy krok kontroluje ci nedoslo ku kolizii, k prehre alebo k vyhre atd
    public void update() {
        if (start) {
            victory();
            checkWalls();
            ball.nearPaddle(paddle.getXPosition(), paddle.getYPosition());
            for (Brick b : wall) {
                if (ball.isCollisionBrick(b.getPosition())) {
                    if(b.getLives() == 1)
                        wall.remove(b);

                    b.setEffect(this);
                    statistic.setScore(statistic.getScore() + b.getScore());
                }
            }
            ball.move();
        }
    }


    // zisti ci hrac vyhral alebo nie
    private void victory() {
        if (wall.isEmpty()) {
            statistic.setLevel(statistic.getLevel() + 1);
            resetLevel();
            ball.increaseSpeed(statistic.getLevel());
            start = false;
        }
    }

    // nastavi hru na zaciatok
    private void resetLevel() {
        ball = new Ball(context, (float)size.x / 2, size.y - 480);
        wall = new CopyOnWriteArrayList<>();
        setBricks(context);
    }


    public void stopScanning() {
        sManager.unregisterListener(this);
    }

    public void runScanning() {
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }


    // sluzi na pozastavenie hry v pripade novej hry
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        start = true;
        if (gameOver && start) {
            statistic = new Statistic(3, 0, 1);
            resetLevel();
            gameOver = false;

        } /*else
            start = true;
            */
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

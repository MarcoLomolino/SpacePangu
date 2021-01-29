package com.example.drawabletest.play.ball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.example.drawabletest.R;
import com.example.drawabletest.play.position.Position;

@SuppressLint("ViewConstructor")
public class Ball extends View {
    private Position position; //x and y position of the ball
    private Position direction; //x and y position where the ball goes
    private final Bitmap graphic_ball; //ball texture
    private int paddlehit;
    Context context1;

    public Ball(Context context, float x, float y, String difficulty) {
        super(context);
        context1 = context;

        this.position = new Position(x, y);
        this.paddlehit = 0;

        if(difficulty.equals("hard"))
            this.direction = new Position(getMIN_X() + 3, getMAX_Y() - 3);
         else
            this.direction = new Position(getMIN_X() + 1, getMAX_Y() - 1);

        this.graphic_ball = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
    }

    public Ball(Context context, float x, float y) {
        super(context);
        context1 = context;

        this.position = new Position(x, y);
        this.paddlehit = 0;

        //direction is get randomly in a range of values
        this.direction = new Position(getMIN_X() + 3, getMAX_Y() - 3);

        this.graphic_ball = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
    }

    public float getMAX_X() {
        return 13;
    }

    public float getMIN_X() {
        return 7;
    }

    public float getMAX_Y() {
        return -17;
    }

    public float getMIN_Y() {
        return -23;
    }


    /*public void setPosition(Position position) {
        this.position = position;
    }*/

    public Position getDirection() {
        return direction;
    }

    /*public void setDirection(Position direction) {
        this.direction = direction;
    }*/

    public Bitmap getGraphic_ball() {
        return graphic_ball;
    }

    /*public void setGraphic_ball(Bitmap graphic_ball) {
        this.graphic_ball = graphic_ball;
    }*/
    public float getXPosition(){ return position.getX();}

    public float getYPosition(){ return position.getY();}

    public Position getPosition () {return position; }

    public void setPosition (Position position) {this.position=position; }

    public void setDirection (Position direction) {this.direction=direction; }

    public int getPaddlehit() {
        return paddlehit;
    }

    //change direction after an hit
    public void changeDirection() {
        if (direction.getX() > 0 && direction.getY() < 0) {
            changeXDirection();
        } else if (direction.getX() < 0 && direction.getY() < 0) {
            changeYDirection();
        } else if (direction.getX() < 0 && direction.getY() > 0) {
            changeXDirection();
        } else if (direction.getX() > 0 && direction.getY() > 0) {
            changeYDirection();
        }
    }

    //the speed is increased by the level TO CHANGE BY EACH PADDLE HIT
    public void increaseSpeed(float phit) {
        if (direction.getX() <= this.getMAX_X() && direction.getY() >= this.getMIN_Y()) {
            direction.setX((float) (direction.getX() + (phit * 0.03)));
            direction.setY((float) (direction.getY() - (phit * 0.03)));
        }
    }

    public void increaseSpeed() {
            direction.setX((float) (direction.getX() + (paddlehit * 0.03)));
            direction.setY((float) (direction.getY() - (paddlehit * 0.03)));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "Ball{" +
                "position=" + position +
                ", direction=" + direction +
                ", graphic_ball=" + graphic_ball +
                '}';
    }

    //change direction after an hit of a wall
    public void changeDirection(String wall) {
        if (direction.getX() > 0 && direction.getY() < 0 && wall.equals("prava")) {
            changeXDirection();
        } else if (direction.getX() > 0 && direction.getY() < 0 && wall.equals("hore")) {
            changeYDirection();
        } else if (direction.getX() < 0 && direction.getY() < 0 && wall.equals("hore")) {
            changeYDirection();
        } else if (direction.getX() < 0 && direction.getY() < 0 && wall.equals("lava")) {
            changeXDirection();
        } else if (direction.getX() < 0 && direction.getY() > 0 && wall.equals("lava")) {
            changeXDirection();
        } else if (direction.getX() > 0 && direction.getY() > 0 && wall.equals("dole")) {
            changeYDirection();
        } else if (direction.getX() > 0 && direction.getY() > 0 && wall.equals("prava")) {
            changeXDirection();
        }
    }

    //check if the ball is near the paddle
    public boolean nearPaddle(float xPaddle, float yPaddle) {
        if (isNear(xPaddle, yPaddle, getXPosition(), getYPosition())) {
            changeDirection();
            paddlehit++;
            return true;
        } else
            return false;
    }

    //check if the ball has a brick collision
    public boolean isCollisionBrick(Position position) {
        if (isNearBrick(position.getX(), position.getY(), getXPosition(), getYPosition())) {
            changeDirection();//if the ball is near a brick then change ball direction to simulate an hit
            return true;
        } else return false;
    }

    //check if the ball is near a brick
    private boolean isNearBrick(float ax, float ay, float bx, float by) {
	//ball position points are increased in bx and by.
	//ball position is a point inside the ball, bx and by are coordinates of the position
	//external to the ball, in this way the graphical hit of the ball happens to the border of the ball
	//and there is no compenetration with the brick
        bx += 12;
        by += 11;
        double d = Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow((ay + 40) - by, 2));
        return d < 80;
    }

    
     //check if the ball is near something
    private boolean isNear(float ax, float ay, float bx, float by) {

	//ball position points are increased in bx and by.
	//ball position is a point inside the ball, bx and by are coordinates of the position
	//external to the ball, in this way the graphical hit of the ball happens to the border of the ball
	//and there is no compenetration with the brick or the paddle
        bx += 12;
        by += 11;
        if ((Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow(ay - by, 2))) < 80) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 100) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        } else return (Math.sqrt(Math.pow((ax + 150) - bx, 2) + Math.pow(ay - by, 2))) < 60;
    }

    //increase position of the ball whit its direction, the ball position is a new point
    public void move() {
        position.setX(this.position.getX() + this.direction.getX());
        position.setY(this.position.getY() + this.direction.getY());
    }


    public void changeXDirection() {
        direction.setX( - this.direction.getX());
    }

    public void changeYDirection() {
        direction.setY( - this.direction.getY());
    }

    public void resetPaddleHit() {
        this.paddlehit = 0;
    }

}

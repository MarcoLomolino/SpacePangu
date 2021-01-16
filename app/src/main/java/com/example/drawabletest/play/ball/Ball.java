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
    Position position; //x and y position of the ball
    Position direction; //x and y position where the ball goes
    Bitmap graphic_ball; //ball texture

    public Ball(Context context, float x, float y) {
        super(context);

        this.position = new Position(x, y);
	//direction is get randomly in a range of values TO CHANGE IN A BASIC START DIRECTION
        this.direction = new Position(generateXDirection(), generateYDirection());
        this.graphic_ball = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
    }

    private float generateXDirection() {
        float rangeX = this.getMAX_X() - this.getMIN_X() + 1;
        return (float) ((Math.random() * rangeX) + this.getMIN_X()); //imposta una velocità x alla palla (da 7 a 13)
    }

    private float generateYDirection() {
        float rangeY = this.getMAX_Y() - this.getMIN_Y() + 1;
        return (float) ((Math.random() * rangeY) + this.getMIN_Y()); //imposta una velocità y alla palla (da -17 a -23)
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

	
	//change direction after an hit
       protected void changeDirection() {
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
    public void increaseSpeed(float level) {
        direction.setX(direction.getX() + level);
        direction.setY(direction.getY() - level);
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
    public void nearPaddle(float xPaddle, float yPaddle) {
        if (isNear(xPaddle, yPaddle, getXPosition(), getYPosition())) changeDirection();
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

}

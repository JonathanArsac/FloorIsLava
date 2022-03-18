package helloandroid.ut3.floorislava.Ball;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball implements Movable {


    private float x, y;
    private float speedX;
    private float speedY;
    private float radius;
    private Direction directionX;
    private Direction directionY;



    public Ball(float x, float y, float radius) {
        directionX = Direction.GAUCHE;
        directionY = Direction.HAUT;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void setDirectionX(Direction dir) {
        this.directionX = dir;
    }

    @Override
    public void setDirectionY(Direction dir) {
        this.directionY = dir;
    }

    @Override
    public void setSpeedX(float speed) {
        this.speedX = speed;
    }

    @Override
    public void setSpeedY(float speed) {
        this.speedY = speed;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getRadius() {
        return radius;
    }

    public Direction getDirectionX() {
        return directionX;
    }

    public Direction getDirectionY() {
        return directionY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(x, y, radius, paint);
    }
}

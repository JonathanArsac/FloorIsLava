package helloandroid.ut3.floorislava.Ball;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Ball implements Movable {

   private float x, y, speedX, speedY;
   private float radius;
   private int color;

   Ball(int color) {
      this.color = color;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   @Override
   public void setSpeedX(float speedX) {
      this.speedX = speedX;
   }

   @Override
   public void setSpeedY(float speedY) {
      this.speedY = speedY;
   }

   public void update(int width, int height) {
      radius = width*0.019f;
      x += speedX;
      y += speedY;

      if (x - radius <= 0) {
         this.x = radius;
      } else if (x + radius >= width) {
         x = width - radius;
      }
      if (y - radius <= 0) {
         this.y = radius;
      } else if (y + radius >= height) {
         y = height - radius;
      }

   }

   public Rect getHitbox() {
      RectF hitboxF = new RectF(x-radius, y-radius, x+radius, y+radius);
      Rect hitbox = new Rect();
      hitboxF.roundOut(hitbox);
      return hitbox;
   }

   public float getRadius() {
      return radius;
   }

   public float getX() {
      return x;
   }

   public float getY() {
      return y;
   }

   public void draw(Canvas canvas) {
      Paint paint = new Paint();
      paint.setColor(color);
      canvas.drawCircle(x, y, radius, paint);

   }
}

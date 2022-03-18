package helloandroid.ut3.floorislava.Ball;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball implements Movable {

   private float x, y, speedX, speedY;
   private float radius;
   private int color;

   Ball(int color) {
      this.color = color;
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

   public void draw(Canvas canvas) {
      Paint paint = new Paint();
      paint.setColor(color);
      canvas.drawCircle(x, y, radius, paint);

   }
}

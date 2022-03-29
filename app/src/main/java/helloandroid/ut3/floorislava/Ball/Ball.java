package helloandroid.ut3.floorislava.Ball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.List;

public class Ball implements Movable {

   private float x, y, speedX, speedY;
   private float radius, initialRadius;
   private int color;
   private boolean isJumping;

   private int currentStep = 0;
   private final static int LAST_STEP = 45;


   private List<float[]> trail = new TrailQueue<>(10);

   Ball(int color) {
      this.color = color;
   }

   public void setJumping(boolean jumping) {
      isJumping = jumping;
   }

   public boolean isJumping() {
      return isJumping;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public void setRadius(float radius) {
      this.radius = radius;
   }

   public void setInitialRadius(float initialRadius) {
      this.initialRadius = initialRadius;
      setRadius(initialRadius);
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

      if (isJumping) {
         jumpAnimation();
         color = Color.CYAN;
      } else {
         color = Color.BLUE;
         currentStep = 0;
      }

      updateTrail();
   }

   private boolean updateTrail = false;
   private void updateTrail() {
      updateTrail = !updateTrail;
      if (updateTrail)
         trail.add(new float[]{getX(), getY(), getRadius()});
   }

   private void jumpAnimation() {
      if (currentStep <= LAST_STEP) {
         float piDiv2 = (float) Math.PI / 2;
         float rad = mapTo(currentStep, 0, LAST_STEP, -piDiv2, piDiv2);
         float factor = (float) Math.cos(rad);
         float toAdd = initialRadius * factor;
         setRadius(initialRadius + toAdd);
      }
      if (currentStep >= LAST_STEP) {
         setJumping(false);
      }
      currentStep++;
   }

   private float mapTo(float input, float input_start, float input_end, float output_start, float output_end) {
      return output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
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
      drawTrail(canvas);
      canvas.drawCircle(x, y, radius, paint);
   }

   private void drawTrail(Canvas canvas) {
      float dirX = speedX == 0 ? 1 : Math.signum(speedX);
      float dirY = speedY == 0 ? 1 : Math.signum(speedY);
      for (int trailSize = trail.size(), i = trailSize - 1; i >= 0; i--) {
         float[] t = trail.get(i);
         Paint paint = new Paint();
         int red = Color.red(color);
         int blue = Color.blue(color);
         int green = Color.green(color);
         int alpha = Math.round(mapTo(i, 0, trailSize, 0, 255f/2f));
         paint.setColor(Color.argb(alpha, red, green, blue));
         canvas.drawCircle(t[0], t[1], t[2], paint);
      }
   }
}

package helloandroid.ut3.floorislava.Ball;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.Display;
import android.view.WindowManager;

public class AcceloSpeedController implements SensorEventListener {

   private static final float ACCELO_SPEED_FACTOR = 80;
   private static final float MAX_ACCELO_SPEED = 400;
   private static final float V_THRESHOLD = 10;

   private final Movable target;
   private Point displaySize = new Point();

   public AcceloSpeedController(Context context, Movable movable) {
      Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
      d.getSize(displaySize);
      target = movable;
   }

   private float getMaxMovableSpeed() {
      return displaySize.x*0.019f;
   }

   @Override
   public void onSensorChanged(SensorEvent sensorEvent) {
      if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
         target.setSpeedX(getSpeedFrom(sensorEvent.values[1]));
         target.setSpeedY(getSpeedFrom(sensorEvent.values[0]));
      }
   }

   private float getSpeedFrom(float vFormSensor) {
      // get direction depending on sign + screen orientation (- -> left, + -> right)
      int newDir = vFormSensor > 0f ? 1 : -1;

      // Scale up Y acceleration from sensor to have a better value to work with
      // Restrain it to an arbitrary max to avoid absurd values
      float vy = Math.min(Math.abs(vFormSensor * ACCELO_SPEED_FACTOR), MAX_ACCELO_SPEED);
      // Take into account only values above threshold to avoid unwanted movement
      if (vy > V_THRESHOLD) {
         // map vy value to a restricted range [0, maxMovableSpeed]
         float movableSpeed = vy * getMaxMovableSpeed() / MAX_ACCELO_SPEED;
         // set new speed
         return newDir*movableSpeed;
      }
      return 0f;
   }

   @Override
   public void onAccuracyChanged(Sensor sensor, int i) {
      // do nothing...
   }
}
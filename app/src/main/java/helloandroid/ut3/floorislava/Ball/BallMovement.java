package helloandroid.ut3.floorislava.Ball;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;

public class BallMovement extends OrientationEventListener implements SensorEventListener {

    private int orientation = 1;
    private final Movable target;
    private static final float ACCELO_SPEED_FACTOR = 80;
    private static final float MAX_ACCELO_SPEED = 400;
    private static final float VY_THRESHOLD = 5;
    private static final float VX_THRESHOLD = 5;

    private Point displaySize = new Point();
    private int height, width;


    public BallMovement(int height, int width, Context context, int rate, Movable movable) {
        super(context, rate);
        target = movable;
        Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        d.getSize(displaySize);
        this.height = height;
        this.width = width;

    }

    private float getMaxMovableSpeedX() {
        return width * 0.019f;
    }

    private float getMaxMovableSpeedY() {
        return height * 0.019f;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // retrieve Y acceleration from sensor

            float vxFromSensor = sensorEvent.values[0];
            float vyFromSensor = sensorEvent.values[1];
            //Log.println(Log.DEBUG, "NTM2", "" + vyFromSensor);


            Direction newDirY = vyFromSensor > 0f ? Direction.DROITE : Direction.GAUCHE;
            float vitesseY = Math.min(Math.abs(vyFromSensor * ACCELO_SPEED_FACTOR), MAX_ACCELO_SPEED);


            Direction newDirX = vxFromSensor > 0f ? Direction.BAS : Direction.HAUT;
            float vitesseX = Math.min(Math.abs(vxFromSensor * ACCELO_SPEED_FACTOR), MAX_ACCELO_SPEED);

            Log.println(Log.DEBUG, "NTM2", "" +  " " + vyFromSensor);

            // Scale up Y acceleration from sensor to have a better value to work with
            // Restrain it to an arbitrary max to avoid absurd values
            // Take into account only values above threshold to avoid unwanted movement
            if (vitesseY > VY_THRESHOLD) {
                // map vy value to a restricted range [0, maxMovableSpeed]
                float movableSpeed = vitesseY * getMaxMovableSpeedX() / MAX_ACCELO_SPEED;
                Log.println(Log.DEBUG, "NTM2", "" +  "movable " + movableSpeed);
                // set new speed and direction
                target.setSpeedX(movableSpeed);
                target.setDirectionX(newDirY);
            } else {
                // set speed of movable to zero in case of values under threshold
                target.setSpeedX(0f);
            }

            /*if (vitesseX > VX_THRESHOLD) {
                // map vy value to a restricted range [0, maxMovableSpeed]
                float movableSpeed = vitesseX * getMaxMovableSpeedX() / MAX_ACCELO_SPEED;
                // set new speed and direction
                target.setSpeedX(movableSpeed);
                target.setDirectionX(newDirX);
            } else {
                // set speed of movable to zero in case of values under threshold
                target.setSpeedX(0f);
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onOrientationChanged(int i) {
        if (orientation >= 60 && orientation <= 120) this.orientation = -1;
        if (orientation >= 240 && orientation <= 300) this.orientation = 1;
    }
}

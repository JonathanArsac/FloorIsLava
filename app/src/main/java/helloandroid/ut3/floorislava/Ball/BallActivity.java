package helloandroid.ut3.floorislava.Ball;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import helloandroid.ut3.floorislava.R;

public class  BallActivity extends Activity implements View.OnTouchListener, SensorEventListener {
    private GameView gameView;
    private SensorManager sm = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        gameView = new GameView(this);
        gameView.setOnTouchListener(this);

        setContentView(gameView);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensor = sensorEvent.sensor.getType();
        float[] values = sensorEvent.values;

        synchronized (this) {
            if (sensor == Sensor.TYPE_MAGNETIC_FIELD) {
                float magField_x = values[0];
                float magField_y = values[1];
                float magField_z = values[2];
                this.gameView.setDirection(magField_x,magField_y);

                //Log.println(Log.DEBUG, "Orientation", "x : " + magField_x + ", y :" + magField_y + ", z :" + magField_z);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float posX = motionEvent.getX();
        float posY = motionEvent.getY();
        this.gameView.setDirection(posX, posY);
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor mMagneticField = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD
        );
        sm.registerListener(this, mMagneticField, SensorManager.
                SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        sm.unregisterListener(this, sm.getDefaultSensor(Sensor.
                TYPE_MAGNETIC_FIELD));
        super.onStop();
    }
}
package helloandroid.ut3.floorislava.Ball;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import helloandroid.ut3.floorislava.picture.CameraActivity;

public class  BallActivity extends Activity {
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

        Bitmap picture = getIntent().getParcelableExtra(CameraActivity.PHOTO_EXTRA_KEY);
        gameView = new GameView(this, picture);
        gameView.setPicture(picture);
        gameView.setSensorManager((SensorManager) getSystemService(SENSOR_SERVICE));
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    @Override
    protected void onStop() {
        gameView.onStop();
        super.onStop();
    }
}
package helloandroid.ut3.floorislava.Ball;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThreadDraw threadDraw;
    private GameThreadUpdate threadUpdate;
    private Ball balle;
    private BallMovement balleMovement;
    private SensorManager sensorManager;


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        threadDraw = new GameThreadDraw(getHolder(), this);
        threadUpdate = new GameThreadUpdate(getHolder(), this);
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadDraw.setRunning(true);
        threadUpdate.setRunning(true);
        balle = new Ball(1250, 1250, 20);
        balleMovement = new BallMovement(this.getResources().getDisplayMetrics().heightPixels, this.getResources().getDisplayMetrics().widthPixels,getContext(), SensorManager.SENSOR_DELAY_NORMAL, balle);
        registerSpeedController();
        threadDraw.start();
        threadUpdate.start();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                threadUpdate.setRunning(false);
                threadUpdate.join();
                threadDraw.setRunning(false);
                threadDraw.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setColor(Color.rgb(250, 0, 0));
            balle.draw(canvas, paint);
        }
    }

    public void update() {

        if (balle.getX() < 0)
            balle.setX(0);
        else if (balle.getX() > this.getResources().getDisplayMetrics().widthPixels)
            balle.setX(this.getResources().getDisplayMetrics().widthPixels);
        if (balle.getY() < 0)
            balle.setY(0);
        else if (balle.getY() > this.getResources().getDisplayMetrics().heightPixels)
            balle.setY(this.getResources().getDisplayMetrics().heightPixels);

        if (balle.getDirectionX() == Direction.HAUT)
            balle.setX(balle.getX() + balle.getSpeedX());
        else if((balle.getDirectionX() == Direction.BAS))
            balle.setX(balle.getX() - balle.getSpeedX());
        if (balle.getDirectionY() == Direction.GAUCHE)
            balle.setY(balle.getY() - balle.getSpeedY());
        else if( balle.getDirectionY() == Direction.DROITE)
            balle.setY(balle.getY() + balle.getSpeedY());


    }


    private void registerSpeedController() {
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(balleMovement, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        if (balleMovement != null)
            balleMovement.enable();
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public void onStop() {
        sensorManager.unregisterListener(balleMovement);
        balleMovement.disable();
    }
}

package helloandroid.ut3.floorislava.Ball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import helloandroid.ut3.floorislava.picture.processing.PictureProcessor;
import helloandroid.ut3.floorislava.picture.processing.PictureProcessorListener;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, PictureProcessorListener {
    private GameThreadDraw threadDraw;
    private GameThreadUpdate threadUpdate;
    private SensorManager sensorManager;

    private Ball ball;
    private AcceloSpeedController ballSpeedController;

    private PictureProcessor pictureProcessor;

    private Bitmap picture;


    public GameView(Context context, Bitmap picture) {
        super(context);
        setPicture(picture.copy(picture.getConfig(), true));
        getHolder().addCallback(this);
        threadDraw = new GameThreadDraw(getHolder(), this);
        threadUpdate = new GameThreadUpdate(getHolder(), this);
        pictureProcessor = new PictureProcessor(getPicture());
        pictureProcessor.setListener(this);
        setFocusable(true);

        ball = new Ball(Color.BLUE);
        ballSpeedController = new AcceloSpeedController(getContext(), ball);
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public Bitmap getScaledPicture() {
        return Bitmap.createScaledBitmap(picture, getWidth(), getHeight(), false);
    }

    @Override
    public void imageProcessed(Bitmap imgProcessed) {
        setPicture(imgProcessed);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadDraw.setRunning(true);
        threadUpdate.setRunning(true);
        pictureProcessor.setRunning(true);
        threadDraw.start();
        threadUpdate.start();
        pictureProcessor.start();
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
                pictureProcessor.setRunning(false);
                pictureProcessor.join();
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
            if (picture != null) {
                canvas.drawBitmap(getScaledPicture(), 0, 0, null);
            }
            ball.draw(canvas);
        }
    }

    public void update() {
        ball.update(getWidth(), getHeight());
    }


    private void registerSpeedController() {
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(ballSpeedController, accelSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onResume() {
        registerSpeedController();
    }

    public void onStop() {
        sensorManager.unregisterListener(ballSpeedController);
    }
}

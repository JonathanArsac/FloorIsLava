package helloandroid.ut3.floorislava.Ball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import helloandroid.ut3.floorislava.picture.processing.PictureProcessor;
import helloandroid.ut3.floorislava.picture.processing.PictureProcessorListener;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, PictureProcessorListener, View.OnTouchListener {
    private GameThreadDraw threadDraw;
    private GameThreadUpdate threadUpdate;
    private SensorManager sensorManager;

    private Ball ball;
    private Timer timer;
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
        this.setOnTouchListener(this);
        timer = new Timer();
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
        ball.setX(getWidth()/2);
        ball.setY(getHeight()/2);
        ball.setInitialRadius(getWidth()*0.019f);
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
            timer.draw(canvas, getHeight());
            ball.draw(canvas);
        }
    }

    public void update() {
        ball.update(getWidth(), getHeight());
        timer.update();
        if (!ball.isJumping() && isBallInLava()) {
            System.out.println("The floor is lava");
        }

    }

    private boolean isBallInLava() {
        Rect ballHitbox = ball.getHitbox();
        Bitmap pixelIntersectingBall = Bitmap.createBitmap(getScaledPicture(), ballHitbox.left, ballHitbox.top, ballHitbox.width(), ballHitbox.height());
        Rect rectBitmap = new Rect(0, 0, pixelIntersectingBall.getWidth(), pixelIntersectingBall.getHeight());

        for (int x = 0; x < pixelIntersectingBall.getWidth(); x++) {
            for (int y = 0; y < pixelIntersectingBall.getHeight(); y++) {
                int pixel = pixelIntersectingBall.getPixel(x, y);
                if (pixel == Color.RED) {
                    return true;
                }
            }
        }
        return false;
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ball.setJumping(true);
        }
        return true;
    }
}

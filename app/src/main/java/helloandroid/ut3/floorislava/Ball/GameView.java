package helloandroid.ut3.floorislava.Ball;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import helloandroid.ut3.floorislava.R;
import helloandroid.ut3.floorislava.picture.processing.PictureProcessor;
import helloandroid.ut3.floorislava.picture.processing.PictureProcessorListener;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, PictureProcessorListener {
    private GameThreadDraw threadDraw;
    private GameThreadUpdate threadUpdate;
    private int directionX = 1;
    private float x_balle = this.getResources().getDisplayMetrics().widthPixels / 2f;
    private float y_balle = this.getResources().getDisplayMetrics().heightPixels / 2f;
    private int radius_balle = 30;
    private float vitesse_balle = 1;
    private boolean drawImg = false;

    SharedPreferences preferences;

    private PictureProcessor pictureProcessor;

    private Bitmap img;


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        threadDraw = new GameThreadDraw(getHolder(), this);
        threadUpdate = new GameThreadUpdate(getHolder(), this);
        setFocusable(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadDraw.setRunning(true);
        threadUpdate.setRunning(true);
        threadDraw.start();
        threadUpdate.start();
        Bitmap test_image = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
        test_image = Bitmap.createScaledBitmap(test_image, getWidth(), getHeight(), false);
        Handler handler = new Handler();
        pictureProcessor = new PictureProcessor(test_image, handler);
        pictureProcessor.setDelay(500);
        pictureProcessor.setListener(this);
        pictureProcessor.setRunning(true);
        handler.postDelayed(pictureProcessor, 0);


    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        pictureProcessor.setRunning(false);
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
            if (drawImg) {
                canvas.drawBitmap(img, 0, 0, null);
            }
            Paint paint = new Paint();
            paint.setColor(Color.rgb(250, 0, 0));
            canvas.drawCircle(x_balle, y_balle, radius_balle, paint);
        }
    }

    public void update() {
        this.vitesse_balle = this.vitesse_balle + 0.1f;
        this.x_balle = (this.x_balle + (this.directionX * vitesse_balle));
        if ((x_balle >= this.getResources().getDisplayMetrics().widthPixels) || (x_balle <= 0)) {
            //this.threadDraw.setRunning(false);
            //this.threadUpdate.setRunning(false);

        }
    }


    public void setDirection(float posX, float posY) {
        if (this.x_balle > posX) {
            directionX = 1;
            // partir à droite
        } else {
            directionX = -1;
            //partir à gauche
        }
    }

    @Override
    public void onProcessingUpdate(Bitmap imgProccessed) {
        img =  Bitmap.createScaledBitmap(imgProccessed, getWidth(), getHeight(), false);
        drawImg = true;
    }
}

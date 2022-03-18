package helloandroid.ut3.floorislava.Ball;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThreadUpdate extends Thread {
    private GameView gameView;
    private Handler handler;
    private boolean running;
    private Canvas canvas;
    private final SurfaceHolder surfaceHolder;


    public GameThreadUpdate(SurfaceHolder surfaceHolder, GameView gameView) {
        this.gameView = gameView;
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.handler = new Handler();
    }

    @Override
    public void run() {
        synchronized (surfaceHolder) {
            this.gameView.update();
        }
        if (running) {
            this.handler.postDelayed(this, 16);
        }
    }


    public void setRunning(boolean running) {
        this.running = running;
    }
}

package helloandroid.ut3.floorislava.Ball;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThreadDraw extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;
    private boolean running;
    private Canvas canvas;
    private Handler handler;


    public GameThreadDraw(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.handler = new Handler();
    }


    @Override
    public void run() {

        canvas = null;

        try {
            canvas = this.surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                this.gameView.draw(canvas);
            }
        } catch (Exception ignored) {
        } finally {
            if (canvas != null) {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (running) {
            this.handler.postDelayed(this, 5);
        }

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }


}

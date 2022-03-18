package helloandroid.ut3.floorislava.Ball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

import java.util.Date;

public class Timer {
    private Date startDate;
    String sDate = "0s";

    public Timer() {
        startDate = new Date();
    }

    public String getsDate() {
        return sDate;
    }

    public void update() {
        sDate="";
        Date currentDate = new Date();
        long diff = currentDate.getTime() - startDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days != 0) {
            sDate += days + "d";
        }
        if (hours != 0) {
            sDate += hours%24 + "h";
        }
        if (minutes != 0) {
            sDate += minutes%60 + "m";
        }
        if (seconds != 0) {
            sDate += seconds%60 + "s";
        }
    }

    public void draw(Canvas canvas, int height) {
        Paint txtPaint = new Paint();
        txtPaint.setColor(Color.BLACK);
        txtPaint.setTextSize(height * 0.05f);
        canvas.drawText(sDate, 0, txtPaint.getTextSize(), txtPaint);
    }
}

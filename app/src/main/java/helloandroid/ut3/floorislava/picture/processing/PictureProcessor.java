package helloandroid.ut3.floorislava.picture.processing;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import android.os.Handler;

public class PictureProcessor implements Runnable {

   private static final int LAVA_THRESHOLD = 300;
   private Bitmap picture;
   private Bitmap grayPicture;
   private PictureProcessorListener listener;
   private boolean running = false;
   private int delay;
   private Handler handler;
   private int nbProcess;

   public PictureProcessor(Bitmap src, Handler handler) {
      picture = src;
      grayPicture = toGrayscale(src);
      this.handler = handler;
   }

   public void setRunning(boolean running) {
      this.running = running;
   }

   public void setDelay(int delay) {
      this.delay = delay;
   }

   public int getDelay() {
      return delay;
   }

   @Override
   public void run() {
      Bitmap img = processImg();
      listener.onProcessingUpdate(img);
      if (running) {
         handler.postDelayed(this, getDelay());
      }
      System.out.println("Image Processed.");
   }

   private Bitmap toGrayscale(Bitmap bmpOriginal)
   {
      int width, height;
      height = bmpOriginal.getHeight();
      width = bmpOriginal.getWidth();

      Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, bmpOriginal.getConfig());
      Canvas c = new Canvas(bmpGrayscale);
      Paint paint = new Paint();
      ColorMatrix cm = new ColorMatrix();
      cm.setSaturation(0);
      ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
      paint.setColorFilter(f);
      c.drawBitmap(bmpOriginal, 0, 0, paint);
      return bmpGrayscale;
   }

   private Bitmap processImg() {
      for (int x = 0; x < picture.getWidth(); x++) {
         for (int y = 0; y < picture.getHeight(); y++) {
            int pixel = grayPicture.getPixel(x, y);
            int A,R,G,B;
            A = Color.alpha(pixel);
            R = Color.red(pixel);
            G = Color.green(pixel);
            B = Color.blue(pixel);
            int rgbSum = R+G+B;
            if (rgbSum < calculateLavaThreshold()) {
               picture.setPixel(x, y, Color.RED);
            }
         }
      }
      nbProcess++;
      return picture;
   }

   private int calculateLavaThreshold() {
      return LAVA_THRESHOLD + (nbProcess*10);
   }

   public void setListener(PictureProcessorListener listener) {
      this.listener = listener;
   }
}

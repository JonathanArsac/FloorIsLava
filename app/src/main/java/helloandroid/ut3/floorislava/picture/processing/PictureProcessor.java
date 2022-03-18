package helloandroid.ut3.floorislava.picture.processing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

public class PictureProcessor extends Thread {

   private static final int LAVA_THRESHOLD = 100;
   private static final int PROCESSING_DELAY = 500;
   private final Bitmap picture;
   private final Bitmap grayPicture;
   private int nbProcess;
   private boolean running = false;
   private final Handler handler = new Handler();
   private PictureProcessorListener listener;

   public PictureProcessor(Bitmap picture) {
      this.picture = picture;
      grayPicture = toGrayscale(picture);
   }

   public void setRunning(boolean running) {
      this.running = running;
   }

   public void setListener(PictureProcessorListener listener) {
      this.listener = listener;
   }

   @Override
   public void run() {
      listener.onProcessingUpdate(processImg());
      if (running) {
         handler.postDelayed(this, PROCESSING_DELAY);
      }
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
            int R,G,B;
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
      return LAVA_THRESHOLD + (nbProcess*5);
   }

}

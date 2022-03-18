package helloandroid.ut3.floorislava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {
    Animation fadeInAnim;
    TextView textView_madeBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        textView_madeBy = findViewById(R.id.textView_madeBy);
        textView_madeBy.setAnimation(fadeInAnim);

        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(2);
        anim.setDuration(700);
        final ImageView splash = (ImageView) findViewById(R.id.volcan_image);
        splash.startAnimation(anim);

        final MediaPlayer player = MediaPlayer.create(SplashActivity.this, R.raw.lava_sound);
        player.start();
        int SPLASH_SCREEN = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                player.stop();
                finish();
            }
        }, SPLASH_SCREEN);

    }
}
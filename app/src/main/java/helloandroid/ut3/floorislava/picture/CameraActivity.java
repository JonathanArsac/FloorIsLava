package helloandroid.ut3.floorislava.picture;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import helloandroid.ut3.floorislava.Ball.BallActivity;
import helloandroid.ut3.floorislava.R;

public class CameraActivity extends Activity{
    private static final int CAMERA_REQUEST = 1888;
    public static final String PHOTO_EXTRA_KEY = "picture.photo";
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView imageView;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.imageView);
        Button photoButton = findViewById(R.id.prendre_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        Button validateButton = findViewById(R.id.validate_picture);
        validateButton.setOnClickListener(this::validatePhoto);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            Log.println(Log.DEBUG,"Permission granted?",isStoragePermissionGranted()+"");
            if (isStoragePermissionGranted()) {
                SaveImage(photo);
            }
            Log.println(Log.DEBUG,"BitMap width", photo.getWidth()+", height"+photo.getHeight());
        }
    }

    public void validatePhoto(View v) {
        if (photo != null) {
            Intent intent = new Intent(this, BallActivity.class);
            intent.putExtra(PHOTO_EXTRA_KEY, photo);
            startActivity(intent);
        }
    }

    private boolean isStoragePermissionGranted() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return false;
        }
    }

    private void SaveImage(Bitmap finalBitmap) {
        Log.println(Log.DEBUG,"Valeur", Environment.getExternalStorageDirectory().toString());
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
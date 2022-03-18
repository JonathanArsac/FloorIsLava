package helloandroid.ut3.floorislava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import helloandroid.ut3.floorislava.picture.CameraActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String description = "Bonjour jeune entrepreneur," +
                "\nCe 'jeu' se joue en prenant une photo qui deviendra la carte du jeu !" +
                "\nLe but étant de survivre le plus longtemps sans se faire toucher par la lave !" +
                "\nBonne chance :)";


        TextView descriptionTextView = findViewById(R.id.description);
        descriptionTextView.setText(description);
        descriptionTextView.setTypeface(null, Typeface.BOLD);


        Button validateButton = findViewById(R.id.playButton);
        validateButton.setOnClickListener(this::jouerButton);


    }

    private void jouerButton(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}
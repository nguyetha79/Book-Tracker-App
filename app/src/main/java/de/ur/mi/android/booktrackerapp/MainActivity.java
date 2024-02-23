package de.ur.mi.android.booktrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import de.ur.mi.android.booktrackerapp.BackgroundSoundService.BackgroundSoundService;

public class MainActivity extends AppCompatActivity {
    private Button continueBtn, playBtn, stopBtn;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContinueBtn();

        setUpBackgroundMusic();

    }



    private void initContinueBtn() {
        continueBtn = findViewById(R.id.btn_continue);
        continueBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CustomFragmentActivity.class);
            startActivity(intent);
        });
    }

    private void setUpBackgroundMusic() {
        playBtn = findViewById(R.id.btn_play);
        playBtn.setOnClickListener(view -> {
            String action = "PLAY";
            Intent myService = new Intent(MainActivity.this, BackgroundSoundService.class);
            myService.setAction(action);
            startService(myService);
        });

        stopBtn = findViewById(R.id.btn_stop);
        stopBtn.setOnClickListener(view -> {
            String action = "STOP";
            Intent myService = new Intent(MainActivity.this, BackgroundSoundService.class);
            myService.setAction(action);
            startService(myService);
        });
    }


}
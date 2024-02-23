package de.ur.mi.android.booktrackerapp.BackgroundSoundService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import de.ur.mi.android.booktrackerapp.R;

public class BackgroundSoundService extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bgr_music);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("PLAY")) {
            if(mediaPlayer.isPlaying() == false) {
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(), "Music Is Playing", Toast.LENGTH_SHORT).show();

            }
        }
        if (intent.getAction().equals("STOP")) {
            this.stopService(intent);
            Toast.makeText(getApplicationContext(), "Music Stopped",    Toast.LENGTH_SHORT).show();

        }

        return startId;
    }
    public void onStart(Intent intent, int startId) {
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public void onLowMemory() {
    }
}

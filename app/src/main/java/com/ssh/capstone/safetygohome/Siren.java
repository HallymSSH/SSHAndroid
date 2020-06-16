package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ssh.capstone.safetygohome.R;

public class Siren extends Activity {

    static MediaPlayer mediaPlayer;
    AudioManager audioManager;
    Button btn_play;
    String shared = "sirenfile";
    String sound;
    int defaltVol;
    boolean isPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.sirenpopup);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        sound = sharedPreferences.getString("sound", "");
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%(가로)
        getWindow().getAttributes().width = width;
        setting();
        setlistner();
    }

    public void setting() {
        btn_play = (Button) findViewById(R.id.btn_play);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    public void setlistner() {
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    if (sound.equals("siren1")) {
                        defaltVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren1);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();

                    } else if (sound.equals("siren2")) {
                        defaltVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren2);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();


                    } else if (sound.equals("siren3")) {
                        defaltVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren3);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();


                    } else {
                        defaltVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren3);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                        mediaPlayer.setVolume(1.0f, 1.0f);
                    }
                    btn_play.setText("중지");

                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer = null;
                        btn_play.setText("시작");
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, defaltVol, AudioManager.FLAG_PLAY_SOUND);
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

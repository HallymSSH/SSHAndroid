package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ssh.capstone.safetygohome.R;

public class Siren extends Activity {

    MediaPlayer mediaPlayer;
    Button btn_play, btn_stop;
    String sound="";
    Siren_select siren_select;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.sirenpopup);


        //sound = ((Siren_select)Siren_select.context).result;
        /*
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%(가로)

        int height = (int) (dm.heightPixels * 0.5); // Display 사이즈의 60%(세로)

        getWindow().getAttributes().width = width;

        getWindow().getAttributes().height = height;
        */

        setting();
        setlistner();
    }

    public void setting() {
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_stop = (Button) findViewById(R.id.btn_stop);
    }

    public void setlistner() {
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (sound.equals("siren1")){
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren1);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } else if(sound.equals("siren2")){
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren2);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } else if(sound.equals("siren3")){
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren2);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                */

                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren1);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                //((Siren_select)Siren_select.context).getresult();
                //siren_select.getresult();

                //Toast.makeText(getApplicationContext(), sound, Toast.LENGTH_SHORT).show();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

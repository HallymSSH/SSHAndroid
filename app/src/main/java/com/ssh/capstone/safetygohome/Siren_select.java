package com.ssh.capstone.safetygohome;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Siren_select extends AppCompatActivity {
    Button btn_back,btn_siren1,btn_siren2,btn_siren3,btn_select1,btn_select2,btn_select3;
    MediaPlayer mediaPlayer;
    String shared = "sirenfile";
    String result;
    Boolean state1,state2,state3;
    SeekBar seekBar;
    AudioManager audioManager;
    int nMax;
    int nCurrentVolumn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siren_select);
        setting();
        setlistner();
        setSeekBar();
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        Boolean select1 = sharedPreferences.getBoolean("select1", false);
        Boolean select2 = sharedPreferences.getBoolean("select2", false);
        Boolean select3 = sharedPreferences.getBoolean("select3", false);

        btn_select1.setSelected(select1);
        btn_select2.setSelected(select2);
        btn_select3.setSelected(select3);
        state1 = select1;
        state2 = select2;
        state3 = select3;

    }

    public void setting() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_siren1 = (Button) findViewById(R.id.btn_siren1);
        btn_siren2 = (Button) findViewById(R.id.btn_siren2);
        btn_siren3 = (Button) findViewById(R.id.btn_siren3);
        btn_select1 = (Button) findViewById(R.id.btn_select1);
        btn_select2 = (Button) findViewById(R.id.btn_select2);
        btn_select3 = (Button) findViewById(R.id.btn_select3);
    }

    public void setlistner() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_siren1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_siren1.setSelected(true);
                btn_siren2.setSelected(false);
                btn_siren3.setSelected(false);
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                    btn_siren1.setSelected(false);
                } else {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren1);
                    mediaPlayer.start();
                }
            }
        });

        btn_siren2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_siren1.setSelected(false);
                btn_siren2.setSelected(true);
                btn_siren3.setSelected(false);
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                    btn_siren2.setSelected(false);
                } else {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren2);
                    mediaPlayer.start();
                }
            }
        });

        btn_siren3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_siren1.setSelected(false);
                btn_siren2.setSelected(false);
                btn_siren3.setSelected(true);
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                    btn_siren3.setSelected(false);
                } else {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren3);
                    mediaPlayer.start();
                }
            }
        });

        btn_select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state1 = true;
                state2 = false;
                state3 = false;

                //Toast.makeText(getApplicationContext(), state.toString(), Toast.LENGTH_LONG).show();
                btn_select1.setSelected(true);
                btn_select2.setSelected(false);
                btn_select3.setSelected(false);
            }
        });

        btn_select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state1 = false;
                state2 = true;
                state3 = false;
                btn_select1.setSelected(false);
                btn_select2.setSelected(true);
                btn_select3.setSelected(false);
            }
        });

        btn_select3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state1 = false;
                state2 = false;
                state3 = true;
                btn_select1.setSelected(false);
                btn_select2.setSelected(false);
                btn_select3.setSelected(true);
            }
        });
    }

    public void setSeekBar() {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        nCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(nMax);
        seekBar.setProgress(nCurrentVolumn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

        if (state1.equals(true)){
            result="siren1";
        } else if(state2.equals(true)) {
            result="siren2";
        } else if(state3.equals(true)){
            result="siren3";
        } else {
            result="siren3";
        }


        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Boolean select1 = state1;
        Boolean select2 = state2;
        Boolean select3 = state3;
        String sound = result;

        editor.putString("sound",sound);
        editor.putBoolean("select1", select1);
        editor.putBoolean("select2", select2);
        editor.putBoolean("select3", select3);
        editor.commit();

    }
}

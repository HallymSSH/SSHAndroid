package com.ssh.capstone.safetygohome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PreferenceActivity extends AppCompatActivity {

    Intent To_info, To_siren;
    Button btn_info, btn_back, btn_siren;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {      // 버튼 클릭 이벤트
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_main);

        setting();
        setListener();
    }
    public void setting() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_info = (Button) findViewById(R.id.btn_info);
        btn_siren = (Button) findViewById(R.id.btn_siren);
        To_info = new Intent(getApplicationContext(),profile.class);
        To_siren = new Intent(getApplicationContext(),Siren_select.class);

    }



    public void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(To_info);
            }
        });

        btn_siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(To_siren);
            }
        });
    }

    public void OnClickHandler(View view)               // 버전 버튼 클릭시 다이얼로그
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("버전").setMessage("0.0.1");

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

}

package com.ssh.capstone.safetygohome;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {      // 버튼 클릭 이벤트
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_main);

        Button button = (Button) findViewById(R.id.btn_back);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

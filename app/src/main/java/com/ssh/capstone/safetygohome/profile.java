package com.ssh.capstone.safetygohome;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class profile extends AppCompatActivity {

    Button btn_profileback, btn_birthday;
    ImageView profile_view;
    TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);

        setting();
        setListener();


    }

    public void setting() {
        btn_profileback = (Button) findViewById(R.id.btn_profileback);
        btn_birthday = (Button) findViewById(R.id.btn_birthday);
        profile_view = (ImageView) findViewById((R.id.profile_image));
        textView_Date = (TextView)findViewById(R.id.birthday);

        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView_Date.setText(year+"년"+month+"월"+dayOfMonth+"일");

            }
        };
    }

    public void setListener() {
        btn_profileback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        profile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void onClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2020,4,28);
        dialog.show();
    }
}

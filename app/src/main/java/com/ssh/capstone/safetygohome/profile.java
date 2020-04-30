package com.ssh.capstone.safetygohome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import java.util.Calendar;


public class profile extends AppCompatActivity {

    Button btn_profileback, btn_birthday, btn_sex;
    ImageView profile_view;
    TextView textView_Date, textView_sex;
    private DatePickerDialog.OnDateSetListener listener;


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
        btn_sex = (Button) findViewById(R.id.btn_sex);
        profile_view = (ImageView) findViewById((R.id.profile_image));
        textView_Date = (TextView)findViewById(R.id.birthday);
        textView_sex = (TextView) findViewById(R.id.textView_sex);

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView_Date.setText(year+"년 "+month+"월 "+dayOfMonth+"일 ");

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
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);

                getMenuInflater().inflate(R.menu.popup, popup.getMenu());

            }
        });
        btn_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        profile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        btn_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final String [] items = {"남성","여성"};
        AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
        builder.setTitle("성별을 선택하세요");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView_sex.setText(items[which]);
                dialog.dismiss(); // 누르면 바로 닫히는 형태
            }
        });
        return builder.create();
    }
}

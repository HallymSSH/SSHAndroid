package com.ssh.capstone.safetygohome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile extends AppCompatActivity {

    Button btn_profileback, btn_birthday, btn_sex;
    CircleImageView profile_view;
    TextView textView_Date, textView_sex, text_name;
    private DatePickerDialog.OnDateSetListener listener;
    private final int GET_GALLERY_IMAGE = 200;


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
        profile_view = (CircleImageView) findViewById((R.id.profile_image));
        textView_Date = (TextView)findViewById(R.id.birthday);
        textView_sex = (TextView) findViewById(R.id.textView_sex);
        text_name = (TextView) findViewById(R.id.text_name);
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView_Date.setText(year+"년 "+month+"월 "+dayOfMonth+"일 ");

            }
        };

    }

    public void setListener() {
        btn_profileback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        profile_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.Album:
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                                startActivityForResult(intent, GET_GALLERY_IMAGE);
                                break;
                            case R.id.basic:
                                profile_view.setImageResource(R.drawable.profile_icon);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        btn_birthday.setOnClickListener(new OnClickListener() {
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

        btn_sex.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        text_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputname();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == GET_GALLERY_IMAGE)
            {
                if(resultCode == RESULT_OK)
                {
                    try{
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        profile_view.setImageBitmap(img);


                    } catch (Exception e)
                    {

                    }
                }
            }
    }

    public void inputname() {

        FrameLayout container = new FrameLayout(this);
        final EditText editText = new EditText(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        container.addView(editText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(container);
        builder.setTitle("이름 변경");
        builder.setMessage("변경할 이름을 입력하세요");

        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text_name.setText(editText.getText().toString());
            }

        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}

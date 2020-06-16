package com.ssh.capstone.safetygohome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile extends AppCompatActivity {

    Bitmap img;
    private static final String FAIL_CODE = "no_image_found";
    Button btn_profileback, btn_birthday, btn_sex;
    CircleImageView profile_view;
    TextView textView_Date, textView_sex, text_name, daum_result, daum_result2;
    Intent daum_view;
    private DatePickerDialog.OnDateSetListener listener;
    private final int GET_GALLERY_IMAGE = 100;
    private final int GET_ADDRESS = 200;
    String shared = "file";
    Drawable temp2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);
        setting();
        setListener();

        profile_view.setImageResource(R.drawable.profile_icon);
        temp2 = getResources().getDrawable(R.drawable.profile_icon);
        img = ((BitmapDrawable) temp2).getBitmap();
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String name = sharedPreferences.getString("name", "");
        String Birthday = sharedPreferences.getString("birthday", "");
        String sex = sharedPreferences.getString("sex", "");
        String Post1 = sharedPreferences.getString("post1", "");
        String Post2 = sharedPreferences.getString("post2", "");
        String profileimg = sharedPreferences.getString("profileimg", "");

        text_name.setText(name);
        textView_Date.setText(Birthday);
        textView_sex.setText(sex);
        daum_result.setText(Post1);
        daum_result2.setText(Post2);
        profile_view.setImageBitmap(decodeBase64(profileimg));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String name = text_name.getText().toString();
        String Birthday = textView_Date.getText().toString();
        String sex = textView_sex.getText().toString();
        String Post1 = daum_result.getText().toString();
        String Post2 = daum_result2.getText().toString();
        editor.putString("post2", Post2);
        editor.putString("post1", Post1);
        editor.putString("sex", sex);
        editor.putString("birthday", Birthday);
        editor.putString("name", name);
        editor.putString("profileimg", encodeTobase64(img));
        editor.commit();
    }

    public void setting() {
        btn_profileback = (Button) findViewById(R.id.btn_profileback);
        btn_birthday = (Button) findViewById(R.id.btn_birthday);
        btn_sex = (Button) findViewById(R.id.btn_sex);
        profile_view = (CircleImageView) findViewById((R.id.profile_image));
        textView_Date = (TextView) findViewById(R.id.birthday);
        textView_sex = (TextView) findViewById(R.id.textView_sex);
        text_name = (TextView) findViewById(R.id.textView);
        daum_result = (TextView) findViewById(R.id.daum_result);
        daum_result2 = (TextView) findViewById(R.id.daum_result2);
        daum_view = new Intent(getApplicationContext(), daumView.class);
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView_Date.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 ");

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
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Album:    // 앨범에서 가져오기
                                Intent gallery = new Intent(Intent.ACTION_PICK);
                                gallery.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                gallery.setType("image/*");
                                startActivityForResult(gallery, GET_GALLERY_IMAGE);
                                break;
                            case R.id.basic:            // 기본이미지 설정
                                profile_view.setImageResource(R.drawable.profile_icon);
                                //img = ((BitmapDrawable)temp2).getBitmap();
                                temp2 = profile_view.getDrawable();
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
                        android.R.style.Theme_DeviceDefault_Light_Dialog, listener,
                        year, month, day);

                dialog.show();
            }
        });

        btn_sex.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"남성", "여성"};
                AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                builder.setTitle("성별을 선택하세요");
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView_sex.setText(items[which]);
                        dialog.dismiss(); // 누르면 바로 닫히는 형태
                    }
                });
                builder.show();
            }
        });

        text_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputname();
            }
        });

        daum_result.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(daum_view, GET_ADDRESS);
            }
        });

        daum_result2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                otheraddress();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_GALLERY_IMAGE:
                    try {

                        InputStream in = getContentResolver().openInputStream(data.getData());
                        img = BitmapFactory.decodeStream(in);           // 비트맵으로 만들어서 저장
                        in.close();
                        profile_view.setImageBitmap(img);

                    } catch (Exception e) {
                    }

                    break;
                case GET_ADDRESS:
                    daum_result.setText(data.getStringExtra("result"));
                    break;

            }
        }
    }

    public void inputname() {
        FrameLayout container = new FrameLayout(this);
        final EditText editText = new EditText(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        container.addView(editText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setView(container);
        builder.setTitle("이름 입력").setMessage("변경할 이름을 입력하세요");

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

    public void otheraddress() {

        FrameLayout container = new FrameLayout(this);
        final EditText editText = new EditText(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        container.addView(editText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setView(container);
        builder.setTitle("상세 주소 입력").setMessage("나머지 주소를 입력하세요");


        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                daum_result2.setText(editText.getText().toString());

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

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

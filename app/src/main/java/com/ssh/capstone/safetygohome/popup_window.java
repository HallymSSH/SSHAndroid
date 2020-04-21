package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class popup_window extends Activity {

    FrameLayout btn_book1, btn_epeople, btn_setting;
    LinearLayout btn_book2, btn_epeople2, btn_setting2;
    Intent Intent_ToContact, Intent_ToURL,  Intent_ToSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        setDisplaysize();
        setting();
        setlistener();
    }

    //디스플레이 사이즈를 세팅
    private void setDisplaysize() {
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.layout_popup);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%(가로)

        int height = (int) (dm.heightPixels * 0.8); // Display 사이즈의 60%(세로)

        getWindow().getAttributes().width = width;

        getWindow().getAttributes().height = height;

    }

    public void setting(){
        btn_book1 = (FrameLayout)findViewById(R.id.btn_book);
        btn_book2 = (LinearLayout) findViewById(R.id.btn_book2);
        Intent_ToContact = new Intent(com.ssh.capstone.safetygohome.popup_window.this, com.ssh.capstone.safetygohome.contactbook_window.class);
        Intent_ToSetting = new Intent(com.ssh.capstone.safetygohome.popup_window.this, com.ssh.capstone.safetygohome.PreferenceActivity.class);

                btn_epeople = (FrameLayout)findViewById(R.id.btn_epeople);
        btn_epeople2 = (LinearLayout)findViewById(R.id.btn_epeople2);
        Uri uri = Uri.parse("https://www.epeople.go.kr/index.jsp"); //이동한 웹주소
        Intent_ToURL = new Intent(Intent.ACTION_VIEW, uri);

        btn_setting = (FrameLayout)findViewById(R.id.btn_setting);
        btn_setting2 = (LinearLayout)findViewById(R.id.btn_setting2);


    }

    //button listener 세팅
    public void setlistener(){
        btn_book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent_ToContact);
            }
        });
        btn_book2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(Intent_ToContact);
            }
        });

        btn_epeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent_ToURL);
            }
        });
        btn_epeople2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(Intent_ToURL);
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent_ToSetting);
            }
        });
        btn_setting2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(Intent_ToSetting);
            }
        });
    }
}

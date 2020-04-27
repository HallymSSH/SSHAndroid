package com.ssh.capstone.safetygohome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class contactbook_window extends AppCompatActivity {


    FrameLayout btn_add, btn_search, btn_delete;
    EditText edit_search;
    Intent ToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        setContentView(R.layout.layout_contactbook);

        setting();
        setlistener();
    }

    public void setting(){
        btn_add = (FrameLayout) findViewById(R.id.btn_add);
        btn_search = (FrameLayout)findViewById(R.id.btn_search);
        btn_delete = (FrameLayout)findViewById(R.id.btn_delete);
        edit_search = (EditText)findViewById(R.id.edit_search);
        ToAdd = new Intent(com.ssh.capstone.safetygohome.contactbook_window.this,
                com.ssh.capstone.safetygohome.listview_add.class);
    }

    public void setlistener(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ToAdd);
            }
        });
    }
}

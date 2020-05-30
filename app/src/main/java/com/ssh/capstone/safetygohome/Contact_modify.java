package com.ssh.capstone.safetygohome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ssh.capstone.safetygohome.Database.DatabaseClass;

import java.util.ArrayList;

public class Contact_modify extends AppCompatActivity {

    FrameLayout btn_yes,btn_no;
    EditText edit_name, edit_num;
    String getName,getNum;          //수정할 사용자 정보
    String name, num;               //바뀐 사용자 정보
    Intent ToBook,FromBook;
    private DatabaseClass db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        setContentView(R.layout.layout_modify);
        setting();
        setlistener();
    }

    public void setting() {
        btn_yes = (FrameLayout)findViewById(R.id.btn_modify_yes);
        btn_no = (FrameLayout)findViewById(R.id.btn_modify_no);
        edit_name = (EditText)findViewById(R.id.edit_modify_name);
        edit_num = (EditText)findViewById(R.id.edit_modify_num);
        ToBook = new Intent(com.ssh.capstone.safetygohome.Contact_modify.this,
                com.ssh.capstone.safetygohome.contactbook_window.class);
        db = new DatabaseClass(this);
        FromBook = getIntent();

        getName = FromBook.getStringExtra("user_name");
        getNum  = FromBook.getStringExtra("user_num");

        edit_name.setText(getName);
        edit_num.setText(getNum);
    }

    public void setlistener() {
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modify();
                ToBook.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ToBook);
                overridePendingTransition(0, 0);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToBook.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ToBook);
                overridePendingTransition(0, 0);
            }
        });
    }

    public void Modify(){
        ArrayList<String> user_send = new ArrayList<>();
        name = edit_name.getText().toString();
        num = edit_num.getText().toString();
        user_send.add(name);        //수정할 원본
        user_send.add(num);
        user_send.add(getName);     //대체 자료
        user_send.add(getNum);

        db.ModifyUser(user_send);
    }
}

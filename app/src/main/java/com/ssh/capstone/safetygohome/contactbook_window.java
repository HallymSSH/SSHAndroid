package com.ssh.capstone.safetygohome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class contactbook_window extends AppCompatActivity {

    ArrayList<String> items = new ArrayList<String>();
    ArrayAdapter adapter;
    FrameLayout btn_add, btn_search, btn_delete;
    ListView contact_listView;
    String name = null, num = null;
    EditText edit_search;
    Intent FromAdd;
    Intent ToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        setContentView(R.layout.layout_contactbook);

        setting();
        setlistener();
        addItem();
    }

    public void setting(){
        btn_add = (FrameLayout) findViewById(R.id.btn_add);
        btn_search = (FrameLayout)findViewById(R.id.btn_search);
        btn_delete = (FrameLayout)findViewById(R.id.btn_delete);
        edit_search = (EditText)findViewById(R.id.edit_search);
        contact_listView = (ListView)findViewById(R.id.contact_listview);
        ToAdd = new Intent(com.ssh.capstone.safetygohome.contactbook_window.this,
                com.ssh.capstone.safetygohome.listview_add.class);
        FromAdd = getIntent();
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, items) ;
        //contact_listView.setAdapter(adapter);
    }

    public void setlistener(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ToAdd);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
    }

    public void addItem(){
        name = FromAdd.getStringExtra("name");
        num = FromAdd.getStringExtra("num");
        Log.i("name : "+name,"num : "+ num);
        edit_search.setText("name : "+name+", num : "+num);

        if(name != null && num != null){
            int count = adapter.getCount();

            // 아이템 추가.
            items.add("LIST" + Integer.toString(count + 1));

            // listview 갱신
            adapter.notifyDataSetChanged();
        }
    }

    public void deleteItem(){
        int count, checked ;
        count = adapter.getCount() ;

        if (count > 0) {
            // 현재 선택된 아이템의 position 획득.
            checked = contact_listView.getCheckedItemPosition();

            if (checked > -1 && checked < count) {
                // 아이템 삭제
                items.remove(checked) ;

                // listview 선택 초기화.
                contact_listView.clearChoices();

                // listview 갱신.
                adapter.notifyDataSetChanged();
            }
        }
    }
}

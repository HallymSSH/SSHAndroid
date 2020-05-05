package com.ssh.capstone.safetygohome;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;

import com.ssh.capstone.safetygohome.Database.DatabaseClass;
import static com.ssh.capstone.safetygohome.Database.PreParingDB.initDB;

public class contactbook_window extends AppCompatActivity {

    ArrayList<ContactData> items = new ArrayList<>();
    ContactAdapter adapter;
    ArrayAdapter arrayAdapter;
    FrameLayout btn_add, btn_search, btn_delete;
    ListView contact_listView;
    String name = null, num = null;
    EditText edit_search;
    Intent FromAdd;
    Intent ToAdd;

    private DatabaseClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        setContentView(R.layout.layout_contactbook);

        try {
            initDB(getResources(), false); // db 준비
        } catch (Exception e) {
            Log.w("Get DB Exception", e.getMessage());
        }

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
        adapter = new ContactAdapter();
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, items);
        contact_listView.setAdapter(adapter);
        db = new DatabaseClass(this);
    }

    public void setlistener(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToAdd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            items.add(new ContactData(name,num));

            db.SaveUser(name, num);

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

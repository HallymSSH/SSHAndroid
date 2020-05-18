package com.ssh.capstone.safetygohome;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.ssh.capstone.safetygohome.Database.DatabaseClass;
import static com.ssh.capstone.safetygohome.Database.PreParingDB.initDB;

public class contactbook_window extends AppCompatActivity {

    ArrayList<ContactData> items = new ArrayList<>();
    ArrayList<String> username = new ArrayList<>();
    ArrayList<String> usernum = new ArrayList<>();
    String name = null, num = null;

    ListView contact_listView;
    ContactAdapter adapter;

    Button btn_add, btn_search, btn_delete;
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
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        edit_search = (EditText)findViewById(R.id.edit_search);
        contact_listView = (ListView)findViewById(R.id.contact_listview);
        ToAdd = new Intent(com.ssh.capstone.safetygohome.contactbook_window.this,
                com.ssh.capstone.safetygohome.listview_add.class);
        FromAdd = getIntent();
        adapter = new ContactAdapter();
        contact_listView.setAdapter(adapter);
        db = new DatabaseClass(this);
    }

    public void setlistener(){

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem(edit_search.getText().toString());
            }
        });

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

        //editText 실시간 확인
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                searchItem(edit_search.getText().toString());
            }
        });
    }

    public void setlistview(){
        db.GetUser(username,usernum);

        for(int i=0; i<username.size();i++) {
            adapter.addItem(username.get(i), usernum.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    public void addItem(){
        name = FromAdd.getStringExtra("name");
        num = FromAdd.getStringExtra("num");

        if(name != null && num != null){
            Log.i(name,num);
            db.SaveUser(name, num);
        }
        setlistview();
    }

    public void deleteItem(){
        int count, checked ;
        count = adapter.getCount() ;

        if (count > 0) {
            // 현재 선택된 아이템의 position 획득.
            Log.i("checkitem : ",contact_listView.getCheckedItemCount()+"");
            checked = contact_listView.getCheckedItemPosition();
            Log.i("들어오긴함 : ", checked+"");

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

    public void searchItem(String name) {

        //검색을 위해 listview clear
        adapter.clear();

        //검색창이 비었을 경우 보든 데이터를 가져온다
        if(name.equals(""))
            setlistview();

            //검색창에 입력이 되면 해당 값을 검색함
        else {
            db.SearchUser(username, usernum, name);

            for (int i = 0; i < username.size(); i++) {
                adapter.addItem(username.get(i), usernum.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}

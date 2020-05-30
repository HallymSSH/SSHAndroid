package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.ssh.capstone.safetygohome.Database.DatabaseClass;
import static com.ssh.capstone.safetygohome.Database.PreParingDB.initDB;


import androidx.annotation.Nullable;

public class Emergencycall extends Activity {
    Button close,save,btn_contect;
    RadioGroup radioGroup;
    RadioButton police, rd_contact;
    TextView name, num;
    Intent intent;
    String shared = "emergency";
    boolean state = true;
    private DatabaseClass db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_emergency);
        setFinishOnTouchOutside(false);
        //initDB(getResources(), false);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = (int) (dm.widthPixels * 0.8); // Display 사이즈의 90%(가로)
        getWindow().getAttributes().width = width;

        //getWindow().getAttributes().height = height;
        setting();
        setlistner();

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String Name = sharedPreferences.getString("name","");
        String number = sharedPreferences.getString("number","");
        Boolean State = sharedPreferences.getBoolean("check",false);
        //Toast.makeText(this, Name, Toast.LENGTH_SHORT).show();

        if (State == true) {
            police.setChecked(true);
            name.setText(Name);
            num.setText(number);
        } else {
            rd_contact.setChecked(true);
            name.setText(Name);
            num.setText(number);
        }
    }

     public void setting() {
        close = (Button) findViewById(R.id.btn_close);
        save = (Button) findViewById(R.id.btn_emersave);
        btn_contect = (Button) findViewById(R.id.btn_contect);
        intent = new Intent(getApplicationContext(),PreferenceActivity.class);
        name = (TextView) findViewById(R.id.text_name);
        num = (TextView) findViewById(R.id.text_num);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        police = (RadioButton) findViewById(R.id.radioButton);
        rd_contact = (RadioButton) findViewById(R.id.radioButton2);
        db = new DatabaseClass(this);
     }

     public void setlistner() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (state == true) {
                    String Name = name.getText().toString();
                    String Number = num.getText().toString();
                    Boolean State = state;
                    editor.putString("name",Name);
                    editor.putString("number",Number);
                    editor.putBoolean("check",State);
                } else {
                    String Name = name.getText().toString();
                    String Number = num.getText().toString();
                    Boolean State = state;
                    editor.putString("name",Name);
                    editor.putString("number",Number);
                    editor.putBoolean("check",State);
                }

                editor.commit();
                DbAdd();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

        btn_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,0);

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton){
                    state = true;
                    //Toast.makeText(Emergencycall.this, "112입니다.", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == R.id.radioButton2) {
                    state = false;
                    //Toast.makeText(Emergencycall.this, "사용자 연락처입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER},null,null,null);
            cursor.moveToFirst();
            String sName = cursor.getString(0);
            String sNumber = cursor.getString(1);
            name.setText(sName);
            num.setText(sNumber);

        }
    }

    public void DbAdd() {
        db.SaveUser(name.getText().toString(),num.getText().toString());
    }
}

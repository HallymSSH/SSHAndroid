package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Emergencysms extends Activity {

    Button close, save, btn_contect;
    RadioGroup radioGroup;
    RadioButton police, rd_contact;
    EditText smsname, smsnum;
    Intent intent;
    boolean state = true;
    String shared = "emergency";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_emersms);
        setFinishOnTouchOutside(false);
        //initDB(getResources(), false);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = (int) (dm.widthPixels * 0.8); // Display 사이즈의 90%(가로)
        getWindow().getAttributes().width = width;

        setting();
        setlistner();
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String Name = sharedPreferences.getString("name", "");
        String number = sharedPreferences.getString("number", "");
        Boolean State = sharedPreferences.getBoolean("smscheck", false);

        if (State == true) {
            police.setChecked(true);
            smsname.setText(Name);
            smsnum.setText(number);
        } else {
            rd_contact.setChecked(true);
            smsname.setText(Name);
            smsnum.setText(number);
        }


    }

    public void setting() {
        close = (Button) findViewById(R.id.btn_smsclose);
        save = (Button) findViewById(R.id.btn_smssave);
        btn_contect = (Button) findViewById(R.id.btn_smscontact);
        intent = new Intent(getApplicationContext(), PreferenceActivity.class);
        smsname = (EditText) findViewById(R.id.text_smsname);
        smsnum = (EditText) findViewById(R.id.text_smsnum);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupsms);
        police = (RadioButton) findViewById(R.id.radiosms);
        rd_contact = (RadioButton) findViewById(R.id.radiosms2);
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
                    String Name = smsname.getText().toString();
                    String Number = smsnum.getText().toString();
                    Boolean State = state;
                    editor.putString("smsname", Name);
                    editor.putString("smsnumber", Number);
                    editor.putBoolean("smscheck", State);
                } else {
                    String Name = smsname.getText().toString();
                    String Number = smsnum.getText().toString();
                    Boolean State = state;
                    editor.putString("smsname", Name);
                    editor.putString("smsnumber", Number);
                    editor.putBoolean("smscheck", State);
                }

                editor.commit();
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
                startActivityForResult(intent, 0);

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiosms) {
                    state = true;
                } else if (checkedId == R.id.radiosms2) {
                    state = false;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String sName = cursor.getString(0);
            String sNumber = cursor.getString(1);
            smsname.setText(sName);
            smsnum.setText(sNumber);
        }
    }
}

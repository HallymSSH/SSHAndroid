package com.ssh.capstone.safetygohome;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class PreferenceActivity extends AppCompatActivity {

    Intent To_info, To_siren, To_emergency;
    Button btn_info, btn_back, btn_siren, btn_emergency;
    TextView timeview;
    String emergency = "time";
    LinearLayout emergencycall;
    Switch aSwitch;
    Boolean switch_state = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {      // 버튼 클릭 이벤트
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_main);

        setting();
        setListener();

        SharedPreferences sharedPreferences = getSharedPreferences(emergency, 0);
        String timeset = sharedPreferences.getString("timeset", "30");
        Boolean Switch = sharedPreferences.getBoolean("switch",false);
        aSwitch.setChecked(Switch);
        //Toast.makeText(this, timeset, Toast.LENGTH_SHORT).show();
        timeview.setText(timeset);
    }
    public void setting() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_info = (Button) findViewById(R.id.btn_info);
        btn_siren = (Button) findViewById(R.id.btn_siren);
        btn_emergency = (Button) findViewById(R.id.btn_emergency);
        To_info = new Intent(getApplicationContext(),profile.class);
        To_siren = new Intent(getApplicationContext(),Siren_select.class);
        To_emergency = new Intent(getApplicationContext(), Emergencycall.class);
        timeview = (TextView) findViewById(R.id.emergency_time);
        emergencycall = (LinearLayout) findViewById(R.id.emergencycall);
        aSwitch = (Switch) findViewById(R.id.switch_call);
    }

    public void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(To_info);
            }
        });

        btn_siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(To_siren);
            }
        });

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                inputtime();
            }
        });

        emergencycall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(To_emergency);


            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    switch_state = true;

                } else {
                    switch_state = false;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void inputtime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle("시간 입력").setMessage("변경할 시간을 입력하세요(분)");
        builder.setView(R.layout.dialog);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog f= (Dialog) dialog;
                EditText input = (EditText) f.findViewById(R.id.dialogText);
                int settime = Integer.parseInt(input.getText().toString());
                String setnumber = String.valueOf(settime);
                timeview.setText(setnumber);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences(emergency, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int timeset = Integer.parseInt(timeview.getText().toString());
        String timesetting = timeview.getText().toString();
        Boolean Switch = switch_state;
        //Toast.makeText(this, timesetting, Toast.LENGTH_SHORT).show();
        editor.putBoolean("switch",Switch);
        editor.putInt("timenumber", timeset);
        editor.putString("timeset",timesetting);
        editor.commit();
    }
}

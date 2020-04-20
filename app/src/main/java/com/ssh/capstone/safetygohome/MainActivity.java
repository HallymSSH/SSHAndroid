package com.ssh.capstone.safetygohome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(this);
        tMapView.setHttpsMode(true);

        tMapView.setSKTMapApiKey("l7xx184eefe2e110491b9ff59f533d66b17b");
        linearLayoutTmap.addView( tMapView );

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(v);
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(v);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(v);
            }
        });
    }

    protected void show(View view)
    {
        String dialogTitle;
        String dialogText;

        if (view.getId() == R.id.imageButton1) {
            dialogTitle = "사이렌";
            dialogText = "사이렌 ㄱㄱ?";
        }
        else if (view.getId() == R.id.imageButton2) {
            dialogTitle = "긴급상황";
            dialogText = "긴급상황 ㄱㄱ?";
        }
        else {
            dialogTitle = "경찰서 연락";
            dialogText = "경찰서 ㄱㄱ?";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogText);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"예",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

}

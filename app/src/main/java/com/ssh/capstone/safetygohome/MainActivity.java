package com.ssh.capstone.safetygohome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;

    View view;
    TMapView tMapView = null;
    TMapGpsManager gps = null;

    FloatingActionButton btn_ToPopUp;
    ImageButton imageButton5;
    Intent Intent_ToPopUp, Intent_DestList, Intent_siren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 위치 권한부분
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        */

        // sms 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }
        }

        // 전화 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        // tmap 그리기
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);

        tMapView = new TMapView(this);
        gps = new TMapGpsManager(this);

        tMapView.setHttpsMode(true);

        // tmap api key
        tMapView.setSKTMapApiKey("l7xx184eefe2e110491b9ff59f533d66b17b");
        linearLayoutTmap.addView(tMapView);

        //현재위치 표시될 아이콘
        tMapView.setIconVisibility(true);

        // setGps(); 현재위치 바로 받아올때 이거 쓰기

        // 사이렌, 긴급상황, 경찰서, 메뉴버튼
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        // 황찬우
        setting();
        setlistener();
        // 황찬우

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show(v);
                startActivity(Intent_siren);
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show(v);

                // 기능 확인시 주석풀고 ㄱㄱ
                //sendSMS("821042008558","테스트입니다.");          // 문자보내기
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show(v);

                // 기능확인시 주석 풀고 ㄱㄱ
               // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01042008558"));    // 전화걸기
               // startActivity(intent);

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGps();
                Toast.makeText(getApplicationContext(), "현재위치", Toast.LENGTH_LONG).show();
            }
        });


    }
    // 문자보내기
    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener); // 휴대폰으로 옮길 때 활성화 하기
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);

    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Log.d("TmapTest",""+longitude+","+latitude);
                tMapView.setLocationPoint(longitude, latitude); // 현재위치로 표시될 좌표의 위도, 경도를 설정합니다.
                //tMapView.setIconVisibility(true);
                //tMapView.setCenterPoint(longitude, latitude,true); // 현재 위치로 이동
                tMapView.setCenterPoint(longitude, latitude, true); // 현재 위치로 이동
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    protected void show(View view) {
        String dialogTitle;
        String dialogText;

        if (view.getId() == R.id.imageButton1) {
            dialogTitle = "사이렌";
            dialogText = "사이렌 ㄱㄱ?";
        } else if (view.getId() == R.id.imageButton2) {
            dialogTitle = "긴급상황";
            dialogText = "긴급상황 ㄱㄱ?";
        } else {
            dialogTitle = "경찰서 연락";
            dialogText = "경찰서 ㄱㄱ?";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogText);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "예", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "아니오", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }



    // 황찬우
    //Button과 Intent 세팅
    public void setting() {
        //FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        btn_ToPopUp = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        Intent_ToPopUp = new Intent(MainActivity.this, com.ssh.capstone.safetygohome.popup_window.class);

        Intent_siren = new Intent(MainActivity.this,Siren.class);
        // 목적지 목록 인텐트
        Intent_DestList = new Intent(MainActivity.this, com.ssh.capstone.safetygohome.DestinationList.class);


    }

    //button listener 세팅
    public void setlistener() {
        btn_ToPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent_ToPopUp);
            }
        });

        // 심창현 목적지 추가
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent_DestList);
            }
        });


    }

    // 지도 중심점 가져오기
    public double getCenterPointLat() {
        return tMapView.getCenterPoint().getLatitude();
    }

    public double getCenterPointLon() {
        return tMapView.getCenterPoint().getLongitude();
    }

}

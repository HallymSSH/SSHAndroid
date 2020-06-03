package com.ssh.capstone.safetygohome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static Context mContext;

    Location location;
    View view;
    TMapView tMapView = null;
    TMapGpsManager gps = null;
    String emergency = "time";
    String shared = "emergency";
    String address;
    int timeset, flag;
    double latitude, longitude;
    Boolean state, aswitch_state, bswitch_state;
    FloatingActionButton btn_ToPopUp;
    ImageButton imageButton5;
    Intent Intent_ToPopUp, Intent_DestList, Intent_siren;
    TextView mTextViewCountDown;
    CountDownTimer mCountDownTimer;
    ColorStateList textColorDefault;
    boolean mTimerRunning;
    long mStartTimeInMillis;
    long mTimeLeftInMillis;
    long mEndTime;
    Geocoder g;
    List<Address> addresslocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mTextViewCountDown = (TextView) findViewById(R.id.timer);
        textColorDefault = mTextViewCountDown.getTextColors();
        SharedPreferences sharedPreferences = getSharedPreferences(emergency, 0);
        timeset = sharedPreferences.getInt("timenumber", 0);
        aswitch_state = sharedPreferences.getBoolean("aswitch", false);
        bswitch_state = sharedPreferences.getBoolean("bswitch", false);
        //Toast.makeText(getApplicationContext(), String.valueOf(timeset), Toast.LENGTH_SHORT).show();
        flag = timeset;
        long millisInput = timeset * 60000;
        setTime(millisInput);


        // 권한 받아오기 TedPermission 라이브러리 사용
        PermissionListener permissionListener = new PermissionListener() {
            public void onPermissionGranted() {
                //Toast.makeText(MainActivity.this, "권한 허용", Toast.LENGTH_SHORT).show();
            }

            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("가족이나 지인에게 연락을 하기 위해 접근권한이 필요합니다.")
                .setDeniedMessage("설정 메뉴에서 언제든지 권한을 변경할 수 있습니다.")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE)
                .check();

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("사용자의 위치를 가져오기 위해 접근권한이 필요합니다.")
                .setDeniedMessage("설정 메뉴에서 언제든지 권한을 변경할 수 있습니다.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        /*
        // 위치 권한부분
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }

        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        // sms 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }
        }

        // 전화 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        // 연락처 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1);
        }
        */

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        g = new Geocoder(this);

        try {
            addresslocation = g.getFromLocation(latitude
                    , longitude, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresslocation != null) {
            if (addresslocation.size() == 0) {
                address = "주소찾기 오류";
            } else {
                address = addresslocation.get(0).getAddressLine(0);
            }
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
                Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();
                if (bswitch_state == true) {
                    if (mTimerRunning) {
                        resetTimer();
                        mCountDownTimer.cancel();
                    } else {
                        SharedPreferences emergency = getSharedPreferences(shared, 0);
                        String number = emergency.getString("number", "");
                        if (number == "") {
                            Toast.makeText(MainActivity.this, "연락처를 설정해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            startTimer();
                        }

                    }
                } else {
                    Toast.makeText(MainActivity.this, "설정에서 스위치를 켜세요", Toast.LENGTH_SHORT).show();
                }

                // 기능 확인시 주석풀고 ㄱㄱ
                // 문자보내기
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show(v);

                // 기능확인시 주석 풀고 ㄱㄱ

                if (aswitch_state == true) {
                    SharedPreferences emergency = getSharedPreferences(shared, 0);
                    state = emergency.getBoolean("callcheck", false);
                    String number = emergency.getString("number", "");
                    if (state == true) {            // 경찰
                        Toast.makeText(getApplicationContext(), "비상연락", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01092086833"));    // 전화걸기
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    } else {                        // 사용자 지정 연락
                        if (number == "") {
                            Toast.makeText(MainActivity.this, "연락처를 설정해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));    // 전화걸기
                            startActivity(intent);
                        }

                    }
                } else {
                    Toast.makeText(MainActivity.this, "설정에서 스위치를 켜세요", Toast.LENGTH_SHORT).show();
                }
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


    public void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        updateCountDownText();
        resetTimer();
    }

    public void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

                SharedPreferences emergency = getSharedPreferences(shared,0);
                state = emergency.getBoolean("smscheck",false);
                String number = emergency.getString("number", "");
                if (state == true) {                // 경찰
                    if (number =="") {
                        Toast.makeText(MainActivity.this, "연락처를 설정해주세요", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "문자를 보냈습니다.", Toast.LENGTH_LONG).show();
                    sendSMS("821092086833",address);      // 문자보내기
                } else {                            // 사용자 지정 연락처
                    if (number =="") {
                        Toast.makeText(MainActivity.this, "연락처를 설정해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "문자를 보냈습니다.", Toast.LENGTH_LONG).show();
                        sendSMS(number,address+ " 주소 테스트 입니다.");
                    }
                }
                resetTimer();
            }
        }.start();
        mTimerRunning = true;
    }

    public void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        mTimerRunning = false;
    }


    @SuppressLint("DefaultLocale")
    public void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        }


        if(mTimeLeftInMillis < 60000) {
            mTextViewCountDown.setTextColor(Color.parseColor("#FF0000"));
        } else {
            mTextViewCountDown.setTextColor(textColorDefault);
        }
        //Toast.makeText(this, String.valueOf(minutes), Toast.LENGTH_SHORT).show();
        mTextViewCountDown.setText(timeLeftFormatted);

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
                latitude = location.getLatitude();           // 위도
                longitude = location.getLongitude();         // 경도
                // Log.d("TmapTest",""+longitude+","+latitude);
                tMapView.setLocationPoint(longitude, latitude); // 현재위치로 표시될 좌표의 위도, 경도를 설정합니다.
                //tMapView.setIconVisibility(true);
                //tMapView.setCenterPoint(longitude, latitude,true); // 현재 위치로 이동
                tMapView.setCenterPoint(longitude, latitude, true); // 현재 위치로 이동
            }

            try {
                addresslocation = g.getFromLocation(latitude,longitude,10);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addresslocation!=null) {
                if(addresslocation.size() == 0) {
                    address = "주소찾기 오류";
                }else {
                    address = addresslocation.get(0).getAddressLine(0);
                }
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

        Intent_siren = new Intent(getApplicationContext(),Siren.class);
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

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(emergency, 0);
        aswitch_state = sharedPreferences.getBoolean("aswitch", false);
        bswitch_state = sharedPreferences.getBoolean("bswitch", false);
        timeset = sharedPreferences.getInt("timenumber",0);
        long millisInput = timeset*60000;
        if (mTimerRunning==true && timeset!=flag ){
            mCountDownTimer.cancel();
            setTime(millisInput);
        } else if (timeset!=flag) {
            setTime(millisInput);
        }
    }

    // 현재 위치 가져오기
    // 함수 이름 바꿔야함
    public double getCenterPointLat() {
        return location.getLatitude();
    }

    public double getCenterPointLon() {
        return location.getLongitude();
    }

}

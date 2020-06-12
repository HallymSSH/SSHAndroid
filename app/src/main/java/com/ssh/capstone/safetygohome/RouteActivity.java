package com.ssh.capstone.safetygohome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.ssh.capstone.safetygohome.Database.DatabaseClass;

import java.util.ArrayList;

import static com.ssh.capstone.safetygohome.Database.PreParingDB.initDB;

public class RouteActivity extends Activity {
    TMapView tMapView = null;
    Intent intent;
    MainActivity mainActivity;
    private DatabaseClass db;
    // 목적지 위경도
    private double destLat;
    private double destLon;
    double latitude, longitude;
    LocationManager lm = null;
    boolean temp = false;

    // CCTV arraylist
    ArrayList<TMapPoint> cctvList = new ArrayList<>();
    ArrayList<Double> tmp1 = new ArrayList<Double>();
    ArrayList<Double> tmp2 = new ArrayList<Double>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_route);

        // db 준비
        try {
            initDB(getResources(), false);
        } catch (Exception e) {
            Log.w("Get DB Exception", e.getMessage());
        }

        // 버튼, 레이아웃
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1r);
        imageButton1.setImageResource(R.drawable.ic_notification_important_black_24dp);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2r);
        imageButton2.setImageResource(R.drawable.ic_notifications_active_24px);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3r);
        imageButton3.setImageResource(R.drawable.ic_security_black_24dp);

        final ToggleButton tbTracking = (ToggleButton) this.findViewById(R.id.toggleButton);
        LinearLayout routeLayoutTmap = (LinearLayout) findViewById(R.id.routeLayoutTmap);
        tMapView = new TMapView(this);
        db = new DatabaseClass(this);

        // 지도 그리기
        routeLayoutTmap.addView(tMapView);

        // 현재 위치 원형아이콘으로 표시
        tMapView.setIconVisibility(true);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) MainActivity.mContext).imagebutton1_click();
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) MainActivity.mContext).imagebutton2_click();
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) MainActivity.mContext).imagebutton3_click();
            }
        });

        // DestinationList 클래스에서 선택한 목적지 위도 경도 가져오기
        intent = getIntent();
        destLat = intent.getDoubleExtra("getLat", 0);
        destLon = intent.getDoubleExtra("getLon", 0);

        // 출발지 목적지 경유지 같은아이콘으로 임시설정
        TMapData tmapdata = new TMapData();
        Bitmap start = BitmapFactory.decodeResource(getResources(), R.drawable.poi_star);
        Bitmap end = BitmapFactory.decodeResource(getResources(), R.drawable.poi_star);
        Bitmap pass = BitmapFactory.decodeResource(getResources(), R.drawable.poi_star);
        tMapView.setTMapPathIcon(start, end, pass);

        // 목적지 위도 경도 팝업 테스트용
        Toast.makeText(getApplicationContext(), destLat + " / " + destLon, Toast.LENGTH_SHORT).show();

        // 목적지까지 라인 그리기
        // drawPedestrianPath(); 여기 주석처리여기 주석처리여기 주석처리

        // 현재 위치로 표시
        setGps();

        // 트래킹모드 활성화 여부. 버튼으로 동작
        tbTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbTracking.isChecked())
                    setTracking(true);
                else
                    setTracking(false);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tMapView.setCenterPoint(longitude, latitude);
                drawPedestrianPath();
            }
        }, 3000);

    }

    // 보행자 경로 그리기
    public void drawPedestrianPath() {
        TMapPoint point1 = tMapView.getCenterPoint(); // 현재위치 출발점
        TMapPoint point2 = new TMapPoint(destLat, destLon); // 목적지 좌표

        // CCTV 위치 찾기. ArrayList, 출발 위경도, 도착 위경도
        db.searchCCTV(cctvList, point1.getLatitude(), point1.getLongitude(), point2.getLatitude(), point2.getLongitude());

        for (int i = 0; i < cctvList.size(); i++) {
            Log.i(cctvList.get(i) + " / ", "경로경로경로경로");
        }

        if (cctvList.size() == 2) {

            double distance1 = distanceTo(point1.getLatitude(), point1.getLongitude(), cctvList.get(0).getLatitude(), cctvList.get(0).getLongitude());
            double distance2 = distanceTo(point1.getLatitude(), point1.getLongitude(), cctvList.get(1).getLatitude(), cctvList.get(1).getLongitude());

            Log.i(distance1 + "distance1", "distance1");
            Log.i(distance2 + "distance2", "distance2");
            if (distance1 > distance2) {
                TMapPoint temp1 = cctvList.get(0);
                TMapPoint temp2 = cctvList.get(1);

                cctvList.clear();
                cctvList.add(temp2);
                cctvList.add(temp1);
                Log.i(cctvList.get(0) + "0번째임", "0번째임");
                Log.i(cctvList.get(1) + "1번째임", "1번째임");
            }
        }

        TMapData tmapdata = new TMapData();

        // 경유지 5개까지라해서 일단 조건문 설정해둠. 5개 초과하면 그냥 일반라인 그려주는걸로. db에서는 3개만 받도록 해둠
        if (cctvList.size() > 0 && cctvList.size() < 5) {
            tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, cctvList, 10, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine polyLine) {
                    polyLine.setLineColor(Color.RED);
                    tMapView.addTMapPath(polyLine);
                }
            });
        } else {
            tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine polyLine) {
                    polyLine.setLineColor(Color.BLUE);
                    tMapView.addTMapPath(polyLine);
                }
            });
        }


    }

    public void setGps() {
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener); // 휴대폰으로 옮길 때 활성화 하기
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener); // 이거 네트워크 위치 잡는거

    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                latitude = location.getLatitude();           // 위도
                longitude = location.getLongitude();         // 경도
                Log.d("여긴가여긴가", latitude + " / " + longitude + "");
                tMapView.setLocationPoint(longitude, latitude);
            }


        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    // 토글버튼 클릭 시 트래킹모드 설정여부
    public void setTracking(boolean toggle) {
        if (toggle == true) { // 트래킹 모드 실행되면 현재위치로 중심점 옮김
            setGps();
            tMapView.setCenterPoint(longitude, latitude);
            Toast.makeText(getApplicationContext(), "트래킹모드 활성화", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "트래킹모드 비활성화", Toast.LENGTH_SHORT).show();
        }

        tMapView.setCompassMode(toggle);
        tMapView.setSightVisible(toggle); // 시야 표출 여부
        tMapView.setTrackingMode(toggle); // --> 트래킹모드 실행. gps 수신될때마다 변경, True일때 실행임

    }

    public double distanceTo(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        Location startPos = new Location("Point A");
        Location endPos = new Location("Point B");

        startPos.setLatitude(startLatitude);
        startPos.setLongitude(startLongitude);
        endPos.setLatitude(endLatitude);
        endPos.setLongitude(endLongitude);

        double distance = startPos.distanceTo(endPos);

        return distance;
    }

}

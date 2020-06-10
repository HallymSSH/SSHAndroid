package com.ssh.capstone.safetygohome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    // 현재 위경도
    private double nowLat;
    private double nowLon;

    Location location;

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
        final ToggleButton tbTracking = (ToggleButton) this.findViewById(R.id.toggleButton);
        LinearLayout routeLayoutTmap = (LinearLayout) findViewById(R.id.routeLayoutTmap);
        tMapView = new TMapView(this);
        db = new DatabaseClass(this);

        // 지도 그리기
        routeLayoutTmap.addView(tMapView);

        // 현재 위치 원형아이콘으로 표시
        tMapView.setIconVisibility(true);

        // 현재 위치로 표시
        setNowLocation();

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
        drawPedestrianPath();

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

    }

    // 현재 위치 메인에서 가져온 후 지도 중심점으로 설정.
    public void setNowLocation() {
        double nowPointLat = location.getLatitude();
        double nowPointLon = location.getLongitude();
        tMapView.setLocationPoint(nowPointLat, nowPointLon); // 현재위치로 표시될 좌표의 위도, 경도를 설정
        tMapView.setCenterPoint(nowPointLat, nowPointLon, true); // 현재 위치로 센터포인트 지정
    }


    // 보행자 경로 그리기
    public void drawPedestrianPath() {
        TMapPoint point1 = tMapView.getLocationPoint(); // 현재위치 출발점
        TMapPoint point2 = new TMapPoint(destLat, destLon); // 목적지 좌표
        TMapPoint wayPoint;

        // CCTV 위치 찾기. ArrayList, 출발 위경도, 도착 위경도
        db.searchCCTV(cctvList, point1.getLatitude(), point1.getLongitude(), point2.getLatitude(), point2.getLongitude());

        for (int i = 0; i < cctvList.size(); i++) {
            Log.i(cctvList.get(i) + " / ", "경로경로경로경로");
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
    /*
    // 현재 위치찾기
    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener); // gps로 하기
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                nowLat = location.getLatitude();
                nowLon = location.getLongitude();
                tMapView.setLocationPoint(nowLat, nowLon); // 현재위치로 표시될 좌표의 위도, 경도를 설정합니다.
                tMapView.setCenterPoint(nowLat, nowLon, true); // 현재 위치로 이동
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }; // 현재 위치찾기 끝
     */

    // 토글버튼 클릭 시 트래킹모드 설정여부
    public void setTracking(boolean toggle) {
        if (toggle == true) { // 트래킹 모드 실행되면 현재위치로 중심점 옮김
            setNowLocation();
            Toast.makeText(getApplicationContext(), "트래킹모드 true", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "트래킹모드 false", Toast.LENGTH_SHORT).show();
        }

        tMapView.setSightVisible(toggle); // 시야 표출 여부
        tMapView.setTrackingMode(toggle); // --> 트래킹모드 실행. gps 수신될때마다 변경, True일때 실행임

    }

}

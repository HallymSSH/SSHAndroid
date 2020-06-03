package com.ssh.capstone.safetygohome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    private double destLat;
    private double destLon;
    private double nowLat;
    private double nowLon;
    ArrayList<Double[]> cctvList = new ArrayList<Double[]>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_route);

        try {
            initDB(getResources(), false); // db 준비
        } catch (Exception e) {
            Log.w("Get DB Exception", e.getMessage());
        }

        final ToggleButton tbTracking = (ToggleButton) this.findViewById(R.id.toggleButton);
        LinearLayout routeLayoutTmap = (LinearLayout) findViewById(R.id.routeLayoutTmap);
        tMapView = new TMapView(this);

        routeLayoutTmap.addView(tMapView);

        tMapView.setIconVisibility(true);

        // 목적지 위도 경도 가져오기
        intent = getIntent();
        destLat = intent.getDoubleExtra("getLat", 0);
        destLon = intent.getDoubleExtra("getLon", 0);

        Toast.makeText(getApplicationContext(), destLat + " / " + destLon, Toast.LENGTH_LONG).show();

        drawPedestrianPath();

        // 트래킹모드 활성화 여부
        tbTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tbTracking.isChecked())
                    setTracking(true);
                else
                    setTracking(false);
            }
        });

    }

    public void setNowLocation() {
        double temp1 = ((MainActivity)MainActivity.mContext).getCenterPointLat();
        double temp2 = ((MainActivity)MainActivity.mContext).getCenterPointLon();
        tMapView.setLocationPoint(temp1, temp2); // 현재위치로 표시될 좌표의 위도, 경도를 설정
        tMapView.setCenterPoint(temp1, temp2, false); // 현재 위치로 센터포인트 지정
    }


    // 보행자 경로 그리기
    public void drawPedestrianPath() {

        TMapPoint point1 = tMapView.getLocationPoint(); // 현재위치 출발점
        TMapPoint point2 = new TMapPoint(destLat, destLon);

        // CCTV 위치 찾기
        db.searchCCTV(cctvList, point1.getLatitude(), point1.getLongitude(), point2.getLatitude(), point2.getLongitude());
        System.out.println(cctvList);

        TMapData tmapdata = new TMapData();

        /*
        경유지 -> <ArrayList> passlist / 변수 0 -> 최적경로
        tmapdata.findPathDataWithType(TMapPathType.CAR_PATH, point1, point2, passList, 0
	new FindPathDataListenerCallback() {
		@Override
		public void onFindPathData(TMapPolyLine polyLine) {
			mMapView.addTMapPath(polyLine);
		}
	});

         */

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                tMapView.addTMapPath(polyLine);
            }
        });
    }

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
    };


    // A, B 위, 경도 위치 반환
    // distanceTo(현재 위,경도 / 도착 위,경도);
    /*
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

     */

    public void setTracking(boolean toggle) {
        if (toggle == true) // 트래킹 모드 실행되면 현재위치로 중심점 옮김
            setNowLocation();

        tMapView.setTrackingMode(toggle); // --> 트래킹모드 실행. gps 수신될때마다 변경, True일때 실행임
        Toast.makeText(getApplicationContext(), "트래킹모드 on off", Toast.LENGTH_LONG).show();
    }

}
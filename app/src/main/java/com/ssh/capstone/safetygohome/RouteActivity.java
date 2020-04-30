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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

public class RouteActivity extends Activity {
    TMapView tMapView = null;
    Intent intent;
    MainActivity mainActivity;
    private double destLat;
    private double destLon;
    private double nowLat;
    private double nowLon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_route);

        LinearLayout routelayoutTmap = new LinearLayout(this);
        tMapView = new TMapView(this);
        routelayoutTmap.addView(tMapView);
        setContentView(routelayoutTmap);
        tMapView.setIconVisibility(true);
        setNowLocation();



        //LinearLayout routelayoutTmap = (LinearLayout) findViewById(R.id.routeLayoutTmap);
        //routelayoutTmap.addView(tMapView);

        intent = getIntent();
        destLat = intent.getDoubleExtra("getLat", 0);
        destLon = intent.getDoubleExtra("getLon", 0);

        Toast.makeText(getApplicationContext(), destLat + " / " + destLon, Toast.LENGTH_LONG).show();

        drawPedestrianPath();


    }

    public void setNowLocation() {
        TMapPoint point = mainActivity.getCenterPoint();
        tMapView.setLocationPoint(point.getLatitude(), point.getLongitude()); // 현재위치로 표시될 좌표의 위도, 경도를 설정합니다.
        tMapView.setCenterPoint(point.getLatitude(), point.getLongitude(), false); // 현재 위치로 이동
    }


    public void drawPedestrianPath() {

        TMapPoint point1 = tMapView.getLocationPoint(); // 출발점
        // TMapPoint point1 = new TMapPoint(37.56420451, 126.98113196);
        TMapPoint point2 = new TMapPoint(destLat, destLon);

        TMapData tmapdata = new TMapData();

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
}
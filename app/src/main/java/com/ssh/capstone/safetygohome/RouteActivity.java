package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

public class RouteActivity extends Activity {
    TMapView tMapView;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_route);


    }


    public void drawPedestrianPath() {
        intent = getIntent();
        intent.getStringExtra("destpoint");

        tMapView = new TMapView(this);
        TMapPoint point1 = tMapView.getLocationPoint(); // 출발점
        TMapPoint point2 = tMapView.getLocationPoint();
        //TMapPoint point2 = randomTMapPoint(); // 도착점

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                tMapView.addTMapPath(polyLine);
            }
        });
    }
}
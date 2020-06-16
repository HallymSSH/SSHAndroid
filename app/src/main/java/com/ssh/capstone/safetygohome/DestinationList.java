package com.ssh.capstone.safetygohome;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DestinationList extends AppCompatActivity {
    private EditText destination;
    private ListView listView;
    private AddressListViewAdapter adapter;
    private String address;
    private Button search_button;
    Location location;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_destlist);

        adapter = new AddressListViewAdapter();

        destination = (EditText) findViewById(R.id.destSearchText);
        listView = (ListView) findViewById(R.id.addressList);
        listView.setAdapter(adapter);

        search_button = (Button) findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                address = destination.getText().toString();
                if (address.getBytes().length <= 0) {
                    Toast.makeText(getApplicationContext(), "값 없음", Toast.LENGTH_SHORT).show();
                } else {
                    findAllPoi(v, address);
                }
            }
        });

        // 리스트뷰 아이템 선택했을 떄 30km 이상이면 길찾기 미제공
        listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                double nowlat = ((MainActivity) MainActivity.mContext).getNowPointLat();
                double nowlon = ((MainActivity) MainActivity.mContext).getNowPointLon();

                if (distanceTo(nowlat, nowlon, adapter.getItem(position).getPoint().getLatitude(), adapter.getItem(position).getPoint().getLongitude()) > 30000) {
                    Log.i("여기여기여기 " + nowlat + " / " + nowlon + " / " + " / " + adapter.getItem(position).getPoint().getLatitude() + " / " + adapter.getItem(position).getPoint().getLongitude() + " / " + distanceTo(nowlat, nowlon, adapter.getItem(position).getPoint().getLatitude(), adapter.getItem(position).getPoint().getLongitude()), "Here");
                    // Toast.makeText(getApplicationContext(), " / " + nowlat + " / " + nowlon + " / " + distanceTo(nowlat, nowlon, adapter.getItem(position).getPoint().getLatitude(), adapter.getItem(position).getPoint().getLongitude()), Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(DestinationList.this)
                            .setMessage("직선 거리가 30km 이내인 경우에만 도보 길찾기를 제공합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                } else {
                    Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                    intent.putExtra("getLat", adapter.getItem(position).getPoint().getLatitude());
                    intent.putExtra("getLon", adapter.getItem(position).getPoint().getLongitude());
                    startActivity(intent);
                }
            }
        }));

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

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };

    public void findAllPoi(View view, final String address) {
        TMapData tmapdata = new TMapData();

        tmapdata.findAllPOI(address, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {

                for (int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem item = poiItem.get(i);
                    // Log.d("ABABABAB", "" + item.name);
                    adapter.addItem(item);
                    Message msg = myHandler.obtainMessage();
                    myHandler.sendMessage(msg);
                }
            }
        });
    }

}
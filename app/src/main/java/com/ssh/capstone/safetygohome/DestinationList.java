package com.ssh.capstone.safetygohome;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
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

        listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                intent.putExtra("destpoint", adapter.getItem(position).getPoint().toString());
                startActivity(intent);
            }
        }));
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
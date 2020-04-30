package com.ssh.capstone.safetygohome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;

public class AddressListViewAdapter extends BaseAdapter {
    Intent gotoLoute;
    private String destPoint;

    private TextView address_name;

    private ArrayList<AddressListViewItem> addressListViewItemList = new ArrayList<AddressListViewItem>();

    public int getCount() {
        return addressListViewItemList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.address_info, parent, false);
        }

        address_name = (TextView) convertView.findViewById(R.id.list_address);
        AddressListViewItem addressListViewItem = getItem(position);
        // AddressListViewItem addressListViewItem = addressListViewItemList.get(position);

        address_name.setText(addressListViewItem.getName());

        /*
        // 클릭 이벤트
        address_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoute.putExtra("dest", addressListViewItem.getPoint());
                Toast.makeText(context, addressListViewItem.getPoint().toString(), Toast.LENGTH_SHORT).show(); // // final 붙음
                gotoLoute = new Intent(com.ssh.capstone.safetygohome.DestinationList.this, com.ssh.capstone.safetygohome.RouteActivity.class);
            }
        });

         */

        return convertView;
    }


    public long getItemId(int position) {
        return position;
    }

    public AddressListViewItem getItem(int position) {
        return addressListViewItemList.get(position);
    }

    // 이름 좌표 추가
    public void addItem(TMapPOIItem poiItem) {
        AddressListViewItem item = new AddressListViewItem();

        item.setName(poiItem.name);
        item.setPoint(poiItem.getPOIPoint());

        addressListViewItemList.add(item);
    }

    public void clear() {
        addressListViewItemList.clear();
    }
}

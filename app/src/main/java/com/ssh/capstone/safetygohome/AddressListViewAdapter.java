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

import java.util.ArrayList;

public class AddressListViewAdapter extends BaseAdapter {
    Intent gotoLoute;

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

        final AddressListViewItem addressListViewItem = addressListViewItemList.get(position); // final 붙음

        address_name.setText(addressListViewItem.getName());

        // 클릭 이벤트
        address_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoute.putExtra("dest", addressListViewItem.getName());
                Toast.makeText(context, addressListViewItem.getName(), Toast.LENGTH_SHORT).show(); // // final 붙음
            }
        });

        return convertView;
    }



    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return addressListViewItemList.get(position);
    }

    public void addItem(String name) {
        AddressListViewItem item = new AddressListViewItem();

        item.setName(name);

        addressListViewItemList.add(item);
    }

    public void clear() {
        addressListViewItemList.clear();
    }
}

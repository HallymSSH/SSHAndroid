package com.ssh.capstone.safetygohome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddressListViewAdapter extends BaseAdapter {
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

        AddressListViewItem addressListViewItem = addressListViewItemList.get(position);

        address_name.setText(addressListViewItem.getName());

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

package com.ssh.capstone.safetygohome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ContactData> sample;

    public ContactAdapter(Context context, ArrayList<ContactData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ContactData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.layout_listview, null);

        TextView name = (TextView)view.findViewById(R.id.textView_name);
        TextView num = (TextView)view.findViewById(R.id.textView_num);

        name.setText(sample.get(position).getName());
        num.setText(sample.get(position).getNum());

        return view;
    }
}

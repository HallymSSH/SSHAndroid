package com.ssh.capstone.safetygohome;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ContactData> listViewItemList = new ArrayList<ContactData>() ;
    ArrayList<Boolean> tag_position = new ArrayList<>();

    // ListViewAdapter의 생성자
    public ContactAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name) ;
        TextView txt_num = (TextView) convertView.findViewById(R.id.txt_num) ;
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.CheckBox);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ContactData listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        txt_name.setText(listViewItem.getName());
        txt_num.setText(listViewItem.getNum());
        checkBox.setChecked(listViewItemList.get(position).getChecked());
        checkBox.setTag(position);
        tag_position.add(false);


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Tag! : ", v.getTag()+"");
                if(tag_position.get((Integer) v.getTag()) == false)
                    tag_position.set((Integer) v.getTag(), true);
                else if(tag_position.get((Integer) v.getTag()) == true)
                    tag_position.set((Integer) v.getTag(), false);
            }
        });
        checkBox.setChecked(isChecked(position));

        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Contact_modify.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("user_name",listViewItemList.get(position).getName());
                intent.putExtra("user_num",listViewItemList.get(position).getNum());
                context.startActivity(intent);
            }
        });

        txt_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Contact_modify.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("user_name",listViewItemList.get(position).getName());
                intent.putExtra("user_num",listViewItemList.get(position).getNum());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public boolean isChecked(int position){
        return listViewItemList.get(position).getChecked();
    }

    public ArrayList<Boolean> getItems(){
        return tag_position;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String name, String num) {
        ContactData item = new ContactData();

        item.setName(name);
        item.setNum(num);

        listViewItemList.add(item);
    }

    public void clear(){
        listViewItemList.clear();
    }
}
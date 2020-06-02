package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

public class listview_add extends Activity {

    FrameLayout btn_yes,btn_no, btn_tobook;
    EditText edit_name, edit_num;
    String name, num;
    Intent ToBook;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        setDisplaysize();
        setting();
        setlistener();
    }

    //디스플레이 사이즈를 세팅
    private void setDisplaysize() {
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.layout_add);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%(가로)

        int height = (int) (dm.heightPixels * 0.6); // Display 사이즈의 60%(세로)

        getWindow().getAttributes().width = width;

        getWindow().getAttributes().height = height;

    }

    public void setting() {
        btn_yes = (FrameLayout)findViewById(R.id.btn_yes);
        btn_no = (FrameLayout)findViewById(R.id.btn_no);
        btn_tobook = (FrameLayout)findViewById(R.id.btn_tobook);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_num = (EditText)findViewById(R.id.edit_num);
        ToBook = new Intent(com.ssh.capstone.safetygohome.listview_add.this,
                com.ssh.capstone.safetygohome.contactbook_window.class);
    }

    public void setlistener() {
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edit_name.getText().toString();
                num = edit_num.getText().toString();

                ToBook.putExtra("name", name);
                ToBook.putExtra("num", num);
                ToBook.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ToBook);
                overridePendingTransition(0, 0);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToBook.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ToBook);
                overridePendingTransition(0, 0);
            }
        });

        btn_tobook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER},null,null,null);
            cursor.moveToFirst();
            String sName = cursor.getString(0);
            String sNumber = cursor.getString(1);
            edit_name.setText(sName);
            edit_num.setText(sNumber);
        }
    }
}

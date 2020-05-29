package com.ssh.capstone.safetygohome;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class ContactData extends Activity {
    private String num, name;
    private boolean checked;

    public ContactData(){
    }

    public ContactData(String name, String num){
        this.name = name;
        this.num = num;
    }

    public void setNum(String user_num) {num = user_num;}
    public void setName(String user_name) { name = user_name; }
    public void setChecked(Boolean user_checked) { checked = user_checked; }

    public String getNum() { return this.num;}
    public String getName() { return this.name;}
    public Boolean getChecked() { return this.checked;}

}

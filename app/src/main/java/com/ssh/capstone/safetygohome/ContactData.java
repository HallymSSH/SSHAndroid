package com.ssh.capstone.safetygohome;

public class ContactData {
    private String num, name;

    /*public ContactData(String name, String num){
        this.name = name;
        this.num = num;
    }*/

    public void setNum(String user_num) {num = user_num;}
    public void setName(String user_name) { name = user_name; }

    public String getNum() { return this.num;}
    public String getName() { return this.name;}
    
}

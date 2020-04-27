package com.ssh.capstone.safetygohome;

public class ContactData {
    private String num, name;

    public ContactData(String num, String name){
        this.name = name;
        this.num = num;
    }

    public String getNum() { return this.num;}
    public String getName() { return this.name;}
    
}

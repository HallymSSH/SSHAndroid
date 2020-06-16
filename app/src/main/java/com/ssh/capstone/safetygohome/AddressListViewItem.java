package com.ssh.capstone.safetygohome;

import com.skt.Tmap.TMapPoint;

public class AddressListViewItem {

    private String nameStr;
    private TMapPoint pointStr;

    public void setName(String name) {
        nameStr = name;
    }

    public String getName() {
        return this.nameStr;
    }

    public TMapPoint setPoint(TMapPoint point) {
        return pointStr = point;
    }

    public TMapPoint getPoint() {
        return pointStr;
    }

}
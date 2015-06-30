package com.example.roberto.sticket;

/**
 * Created by Roberto on 5/19/15.
 */
public class navizonDevice{
    private String mac;
    private String name;
    private String desc;
    private int lrrt;
    private navizonLocation loc;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLrrt() {
        return lrrt;
    }

    public void setLrrt(int lrrt) {
        this.lrrt = lrrt;
    }

    public navizonLocation getLoc() {
        return loc;
    }

    public void setLoc(navizonLocation loc) {
        this.loc = loc;
    }
}



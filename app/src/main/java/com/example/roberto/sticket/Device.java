package com.example.roberto.sticket;

import java.io.Serializable;

/**
 * Created by Roberto on 5/20/15.
 */
public class Device implements Serializable {
    private int sticket_id;
    private String mac_address;
    private int ticket_id;
    private int id;
    private String ip_address;
    private int power_on;
    private int ticket_status;
    private indoorlocation indoorlocation;

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Device(){

    }

    public int getPower_on() {
        return power_on;
    }

    public indoorlocation getIndoorlocation() {
        return indoorlocation;
    }

    public void setIndoorlocation(indoorlocation indoorlocation) {
        this.indoorlocation = indoorlocation;
    }

    public void setPower_on(int power_on) {
        this.power_on = power_on;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticketID){
        this.ticket_id = ticketID;
    }

    public int getSticket_id() { return this.sticket_id;  }

    public void setSticket_id(int sticket_id){ this.sticket_id = sticket_id;}

    public int getTicket_status(){
        return this.ticket_status;
    }

    public void setTicket_status(int ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getMac_address(){
        return this.mac_address;
    }

    public void setMac_address(String mac_address){
        this.mac_address = mac_address;
    }

    public String outputString(){
        String output = Integer.toString(this.sticket_id);
        output = output+" "+Integer.toString(this.ticket_id)+" "+Integer.toString(ticket_status)+" "+this.mac_address;
        return output;
    }
}

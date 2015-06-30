package com.example.roberto.sticket;

/**
 * Created by Roberto on 5/15/15.
 */
public class Sticket {
    private int sticket_id;
    private String mac_address;
    private int ticket_id;
    private int power_on;
    private int ticket_status;
    private navizonDevice device;

    public Sticket(){

    }

    public navizonDevice getDevice() {
        return device;
    }

    public void setDevice(navizonDevice device) {
        this.device = device;
    }

    public int getPower_on() {
        return power_on;
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

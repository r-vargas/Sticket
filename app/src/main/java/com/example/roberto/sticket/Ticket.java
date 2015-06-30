package com.example.roberto.sticket;

import android.media.Image;

/**
 * Created by Roberto on 5/14/15.
 */
public class Ticket {
    // Ticket fields
    private int id;
    private int status;
    private int activation_date;
    private int category;
    private int people_involved;
    private String notes;
    private String attachment_name;
    private String attachment_url;

    // Fields for POST method
    private int sticket_id;
    private int user_id;
    private String user_name;

    public int getSticket_id() {
        return sticket_id;
    }

    public void setSticket_id(int sticket_id) {
        this.sticket_id = sticket_id;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }



    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public int getPeople_involved() {
        return people_involved;
    }

    public void setPeople_involved(int people_involved) {
        this.people_involved = people_involved;
    }

    public int getActivation_date() {
        return activation_date;
    }

    public void setActivation_date(int activation_date) {
        this.activation_date = activation_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    //private String repairType;
    //private String ticketCreator;
    //private String repairImagePath;
    //private int prob/lemSeverity;
    //private String problemDescription;
    //private int problemScope;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ticket(){
    }

    public int getCategory(){
        return this.category;
    }

    public void setCategory(int category){
        this.category= category;
    }

    public int getUser_id(){
     return this.user_id;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    //public String getRepairImagePath(){
    //    return this.repairImagePath;
    //}

    //public void setRepairImagePath(String repairImagePath){
    //    this.repairImagePath = repairImagePath;
    //}

    public String getNotes(){
        return this.notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    //public int getProblemSeverity(){
    //    return this.problemSeverity;
    //}

    //public void setProblemSeverity(int problemSeverity){
    //    this.problemSeverity = problemSeverity;
    //}


}

package com.example.roberto.sticket;

/**
 * Created by Roberto on 5/23/15.
 */
public class ticketResponse {
    private Ticket ticket;
    // private Message message;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ticketResponse(){

    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}



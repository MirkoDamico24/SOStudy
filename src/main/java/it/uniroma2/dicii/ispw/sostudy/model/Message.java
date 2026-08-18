package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String message;
    private User sender;
    private User recipient;

    public Message(String message, User sender, User recipient) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Message(String message, User recipient) {
        this.message = message;
        this.sender = null;;
        this.recipient = recipient;
    }


    public String getMessage() { return this.message; }
    public User getSender() { return this.sender; }
    public User getRecipient() { return this.recipient; }
    public void setMessage(String message) { this.message = message; }
    public void setSender(User sender) { this.sender = sender; }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}

package it.uniroma2.dicii.ispw.sostudy.model;


public class Message {
    private String content;
    private User sender;
    private User recipient;

    public Message(String message, User sender, User recipient) {
        this.content = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Message(String message, User recipient) {
        this.content = message;
        this.sender = null;
        this.recipient = recipient;
    }


    public String getMessage() { return this.content; }
    public User getSender() { return this.sender; }
    public User getRecipient() { return this.recipient; }
    public void setMessage(String message) { this.content = message; }
    public void setSender(User sender) { this.sender = sender; }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}

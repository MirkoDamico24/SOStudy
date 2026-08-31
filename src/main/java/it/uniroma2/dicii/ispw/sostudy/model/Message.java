package it.uniroma2.dicii.ispw.sostudy.model;


public class Message {
    private String content;
    private User sender;
    private User recipient;
    private MessageType type;
    private boolean read;

    public Message(String message, User sender, User recipient,  MessageType type) {
        this.content = message;
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
    }

    public Message(String message, User recipient, MessageType type) {
        this.content = message;
        this.sender = null;
        this.recipient = recipient;
        this.type = type;
    }


    public String getMessage() { return this.content; }
    public User getSender() { return this.sender; }
    public User getRecipient() { return this.recipient; }
    public void setMessage(String message) { this.content = message; }
    public void setSender(User sender) { this.sender = sender; }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }

    public void setRead(boolean read) { this.read = read; }
    public boolean isRead() { return this.read; }
}

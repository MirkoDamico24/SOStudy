package it.uniroma2.dicii.ispw.sostudy.bean;

import it.uniroma2.dicii.ispw.sostudy.model.MessageType;

public class MessageBean {
    private String message;
    private String recipient;
    private String sender;
    private MessageType type;
    private boolean read;

    public MessageBean(String message, String recipient, String sender, MessageType type, boolean read) {
        this.message = message;
        this.recipient = recipient;
        this.sender = sender;
        this.type = type;
        this.read = read;
    }

    public MessageBean(String message, String recipient, MessageType type, boolean read) {
        this.message = message;
        this.recipient = recipient;
        this.sender = null;
        this.type = type;
        this.read = read;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }

}

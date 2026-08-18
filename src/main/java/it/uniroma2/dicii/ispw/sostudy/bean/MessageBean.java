package it.uniroma2.dicii.ispw.sostudy.bean;

public class MessageBean {
    private String message;
    private String recipient;
    private String sender;

    public MessageBean(String message, String recipient, String sender) {
        this.message = message;
        this.recipient = recipient;
        this.sender = sender;
    }

    public MessageBean(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
        this.sender = null;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
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

}

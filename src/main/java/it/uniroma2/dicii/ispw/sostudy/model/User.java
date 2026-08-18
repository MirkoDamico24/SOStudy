package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.eng.observer.MessageSubject;

import java.util.ArrayList;
import java.util.List;

public class User extends MessageSubject {
    private String name;
    private String surname;
    private String email;

    private List<VirtualClass> classes;
    private List<Message> messages;

    protected User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public void addClass(VirtualClass cls) {
        if(this.classes == null) {
            this.classes = new ArrayList<>();
        }
        this.classes.add(cls);
    }

    public void addClass(List<VirtualClass> cls) {
        if(this.classes == null) {
            this.classes = cls;
        }
    }

    public void addMessage(Message msg) {
        if(this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(msg);
        this.notifyObservers();
    }

    public void addMessage(List<Message> msg) {
        this.messages = msg;
        this.notifyObservers();
    }

    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public List<VirtualClass> getClasses() { return this.classes; }

}

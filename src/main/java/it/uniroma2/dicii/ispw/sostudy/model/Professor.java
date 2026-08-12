package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class Professor {
    private String name;
    private String surname;
    private String email;

    private List<VirtualClass> classes;

    public Professor(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Professor(String name, String surname, String email, List<VirtualClass> classes) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.classes = classes;
    }

    public void addClass(VirtualClass cls) {
        if(this.classes == null) {
            this.classes = new ArrayList<>();
        }
        this.classes.add(cls);
    }

    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public List<VirtualClass> getClasses() { return this.classes; }
}

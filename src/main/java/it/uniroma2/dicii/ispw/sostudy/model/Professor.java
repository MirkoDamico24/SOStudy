package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class Professor {
    private int id;
    private String name;
    private String surname;
    private String email;

    private List<Class> classes;

    public Professor(int id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public void addClass(Class cls) {
        if(this.classes == null) {
            this.classes = new ArrayList<>();
        }
        this.classes.add(cls);
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public List<Class> getClasses() { return this.classes; }
}

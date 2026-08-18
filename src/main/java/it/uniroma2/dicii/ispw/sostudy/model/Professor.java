package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.List;

public class Professor extends User{
    public Professor(String name, String surname, String email) {
        super(name, surname, email);
    }

    public Professor(String name, String surname, String email, List<VirtualClass> classes) {
        super(name, surname, email);
        this.addClass(classes);
    }

}

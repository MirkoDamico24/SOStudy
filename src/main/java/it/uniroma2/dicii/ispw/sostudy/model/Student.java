package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String surname;
    private String email;

    private List<TestAttempt> takenTests;
    private List<VirtualClass>  classes;

    public Student(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public void takeTest(TestAttempt t){
        if(this.takenTests == null) {
            this.takenTests = new ArrayList<>();
        }
        this.takenTests.add(t);
    }

    public void joinClass(VirtualClass cls) {
        if(this.classes == null) {
            this.classes = new ArrayList<>();
        }
        this.classes.add(cls);
    }



    public List<TestAttempt> getTakenTests() { return this.takenTests; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public List<VirtualClass> getClasses() { return this.classes; }
}

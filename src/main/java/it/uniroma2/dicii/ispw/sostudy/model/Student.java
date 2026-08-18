package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<TestAttempt> takenTests;

    public Student(String name, String surname, String email) {
        super(name, surname, email);
    }

    public void takeTest(TestAttempt t){
        if(this.takenTests == null) {
            this.takenTests = new ArrayList<>();
        }
        this.takenTests.add(t);
    }
    public List<TestAttempt> getTakenTests() { return this.takenTests; }
}

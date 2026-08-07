package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private String name;

    private Professor prof;
    private List<Student> students;
    private List<Test> assignedTests;

    public Class(String name, Professor prof, Student student) {
        this.name = name;
        this.prof = prof;
        this.students = new ArrayList<>();
        this.students.add(student);
    }

    public Class(String name, Professor prof, List<Student> students) {
        this.name = name;
        this.prof = prof;
        this.students = students;
    }

    public void addTest(Test test) {
        if(this.assignedTests == null) {
            this.assignedTests = new ArrayList<>();
        }
        this.assignedTests.add(test);
    }

    public List<Test> getAvailableTests() { return this.assignedTests; }

    public void deleteTest(Test test) { this.assignedTests.remove(test); }

    public void addStudent(Student student) { this.students.add(student); }

    public void removeStudent(Student student) { this.students.remove(student); }

    public Professor getProf() { return this.prof; }

}

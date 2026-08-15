package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class VirtualClass {
    private String name;
    private int classId;
    private Professor prof;
    private List<Student> students;
    private List<Test> assignedTests;

    public VirtualClass(String name,int classId, Professor prof, Student student) {
        this.name = name;
        this.classId = classId;
        this.prof = prof;
        this.students = new ArrayList<>();
        this.students.add(student);
    }

    public VirtualClass(String name,int classId, Professor prof) {
        this.name = name;
        this.classId = classId;
        this.prof = prof;
    }

    public VirtualClass(String name, int classId, Professor prof, List<Student> students) {
        this.name = name;
        this.classId = classId;
        this.prof = prof;
        this.students = students;
    }

    public VirtualClass(String name,int classId, Professor prof, List<Student> students, List<Test> assignedTests) {
        this.name = name;
        this.classId = classId;
        this.prof = prof;
        this.students = students;
        this.assignedTests = assignedTests;
    }

    public void addTest(Test test) {
        if(this.assignedTests == null) {
            this.assignedTests = new ArrayList<>();
        }
        this.assignedTests.add(test);
    }

    public void setAssignedTests(List<Test> assignedTests) {
        this.assignedTests = assignedTests;
    }

    public List<Test> getAvailableTests() { return this.assignedTests; }

    public void deleteTest(Test test) { this.assignedTests.remove(test); }

    public void addStudent(Student student) {
        if(this.students == null) {
            this.students = new ArrayList<>();
        }
        this.students.add(student);
    }

    public void setStudent(List<Student> student) {
        this.students = student;
    }

    public List<Student> getStudents() { return this.students; }

    public void removeStudent(Student student) { this.students.remove(student); }

    public Professor getProf() { return this.prof; }

    public String getName() { return this.name; }

    public int getClassId() { return this.classId; }

    public void setClassId(int classId) { this.classId = classId; }
}

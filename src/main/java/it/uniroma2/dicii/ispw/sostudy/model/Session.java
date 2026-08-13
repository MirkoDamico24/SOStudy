package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;

public class Session {
    private Student currentStudent;
    private Professor currentProfessor;
    private UserRole role;
    private VirtualClass currentClass;
    private int sessionID;

    public Session(int sessionID, Student currentStudent) {
        this.sessionID = sessionID;
        this.currentStudent = currentStudent;
        this.role = UserRole.STUDENT;
    }

    public Session(int sessionID, Professor currentProfessor) {
        this.sessionID = sessionID;
        this.currentProfessor = currentProfessor;
        this.role = UserRole.PROFESSOR;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public Professor getCurrentProfessor() {
        return currentProfessor;
    }

    public void setCurrentProfessor(Professor currentProfessor) {
        this.currentProfessor = currentProfessor;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setCurrentClass(VirtualClass currentClass) {
        this.currentClass = currentClass;
    }

    public VirtualClass getCurrentClass() {
        return currentClass;
    }
}

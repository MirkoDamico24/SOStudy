package it.uniroma2.dicii.ispw.sostudy.bean;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.TestTimerService;

public class SessionBean {
    private StudentBean student;
    private ProfessorBean professor;
    private final int sessionID;
    private UserRole currentRole;
    private VirtualClassBean currentClass;
    private TestBean test;
    private TestTimerService timer;

    public SessionBean(StudentBean student, int id) {
        this.student = student;
        this.sessionID = id;
        this.currentRole = UserRole.STUDENT;
    }

    public SessionBean(ProfessorBean professor, int id) {
        this.professor = professor;
        this.sessionID = id;
        this.currentRole = UserRole.PROFESSOR;
    }

    public StudentBean getStudent() {
        return student;
    }

    public ProfessorBean getProfessor() {
        return professor;
    }

    public int getSessionID() {
        return sessionID;
    }

    public UserRole getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(UserRole currentRole) {
        this.currentRole = currentRole;
    }

    public VirtualClassBean getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(VirtualClassBean currentClass) {
        this.currentClass = currentClass;
    }

    public TestBean getTest() {
        return test;
    }

    public void setTest(TestBean test) {
        this.test = test;
    }

    public TestTimerService getTimer() {
        return timer;
    }

    public void setTimer(TestTimerService timer) {
        this.timer = timer;
    }
}

package it.uniroma2.dicii.ispw.sostudy.bean;


import java.util.List;

public class VirtualClassBean {
    private String className;
    private ProfessorBean professor;
    private List<StudentBean> students;
    private List<TestBean> test;

    public VirtualClassBean(String className, ProfessorBean professor, List<StudentBean> students) {
        this.className = className;
        this.professor = professor;
        this.students = students;
    }

    public VirtualClassBean(String className) {
        this.className = className;
    }

    public VirtualClassBean(String className, ProfessorBean professor) {
        this.className = className;
        this.professor = professor;
    }

    public VirtualClassBean(String className, List<StudentBean> student) {
        this.className = className;
        this.students = student;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ProfessorBean getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorBean professor) {
        this.professor = professor;
    }

    public List<StudentBean> getStudents() {
        return students;
    }

    public void setStudents(List<StudentBean> students) {
        this.students = students;
    }

    public void setTest(List<TestBean> test) {
        this.test = test;
    }

    public List<TestBean> getTest() {
        return test;
    }
}

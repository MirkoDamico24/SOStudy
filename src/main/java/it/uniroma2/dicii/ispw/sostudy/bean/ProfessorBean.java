package it.uniroma2.dicii.ispw.sostudy.bean;

public class ProfessorBean {
    private String name;
    private String surname;
    private String email;

    public ProfessorBean(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }
}

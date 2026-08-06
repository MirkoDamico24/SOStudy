package it.uniroma2.dicii.ispw.sostudy.model;

public class Student {
    private int id;
    private String name;
    private String surname;
    private String email;

    public Student(int id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public int getId() { return this.id;}
    public String getName() { return this.name;}
    public String getSurname() { return this.surname;}
    public String getEmail() { return this.email;}


}

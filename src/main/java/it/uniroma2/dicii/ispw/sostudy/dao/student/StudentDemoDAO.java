package it.uniroma2.dicii.ispw.sostudy.dao.student;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDemoDAO extends StudentDAO {
    private List<Student> students = new ArrayList<>();

    private void populateStudents(){
        students.add(new Student("Giuseppe", "Bianchi", "giuseppe.bianchi@gmail.com"));
        this.addToCache("giuseppe.bianchi@gmail.com", students.getFirst());
    }

    @Override
    public Student getStudentByEmail(String email) throws DAOException {
        if(students.isEmpty()) populateStudents();

        if(containsKey(email)) {
            return getFromCache(email);
        }

        return null;
    }
}

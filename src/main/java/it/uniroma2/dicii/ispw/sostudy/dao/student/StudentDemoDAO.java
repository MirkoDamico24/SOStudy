package it.uniroma2.dicii.ispw.sostudy.dao.student;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;

public class StudentDemoDAO implements  StudentDAO {
    private Student student = new Student(1234, "Giuseppe", "Bianchi", "giuseppe.bianchi@gmail.com");

    @Override
    public Student getStudentByEmail(String email) throws DAOException {
        if(student.getEmail().equals(email)) return this.student;
        else throw new DAOException("Student not found");
    }
}

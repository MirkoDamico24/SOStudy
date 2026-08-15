package it.uniroma2.dicii.ispw.sostudy.dao.student;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;

public class StudentDemoDAO extends StudentDAO {
    @Override
    public Student getStudentByEmail(String email) throws DAOException {
        if(containsKey(email)) {
            return getFromCache(email);
        }
        return new Student ("Giuseppe", "Bianchi", "giuseppe.bianchi@gmail.com");
    }
}

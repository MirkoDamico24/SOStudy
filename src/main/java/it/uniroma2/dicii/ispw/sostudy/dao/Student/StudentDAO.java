package it.uniroma2.dicii.ispw.sostudy.dao.Student;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;

public interface StudentDAO {
    Student getStudentByEmail(String email) throws DAOException;
}

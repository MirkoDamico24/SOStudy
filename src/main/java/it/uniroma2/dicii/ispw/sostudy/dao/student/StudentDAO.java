package it.uniroma2.dicii.ispw.sostudy.dao.student;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;

public abstract class StudentDAO extends CacheDAO<String, Student> {
    public abstract Student getStudentByEmail(String email) throws DAOException;
}

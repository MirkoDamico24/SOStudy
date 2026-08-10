package it.uniroma2.dicii.ispw.sostudy.dao.professor;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;

public interface ProfessorDao {
    Professor getProfessorByEmail(String email) throws DAOException;
}

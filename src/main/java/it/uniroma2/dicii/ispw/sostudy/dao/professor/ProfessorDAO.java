package it.uniroma2.dicii.ispw.sostudy.dao.professor;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;

public abstract class ProfessorDAO extends CacheDAO<String, Professor> {
    public abstract Professor getProfessorByEmail(String email) throws DAOException;
}

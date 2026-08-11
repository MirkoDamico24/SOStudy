package it.uniroma2.dicii.ispw.sostudy.dao.professor;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;

public class ProfessorDemoDAO extends ProfessorDAO {
    @Override
    public Professor getProfessorByEmail(String email) throws DAOException {
        if(containsKey(email)) {
            return getFromCache(email);
        }
        return new Professor("Mario", "Rossi", "mario.rossi@gmail.com");
    }
}

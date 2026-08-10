package it.uniroma2.dicii.ispw.sostudy.dao.Professor;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;

public class ProfessorDemoDAO implements ProfessorDao {
    private Professor prof = new Professor("Mario", "Rossi", "mario.rossi@gmail.com");

    @Override
    public Professor getProfessorByEmail(String email) throws DAOException {
        if(prof.getEmail().equals(email))
            return prof;
        else throw new DAOException("Provided email does not exist");
    }
}

package it.uniroma2.dicii.ispw.sostudy.dao.professor;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;


import java.util.ArrayList;
import java.util.List;

public class ProfessorDemoDAO extends ProfessorDAO {
    private List<Professor> professors = new ArrayList<>();

    private void populateProfessors() {
        professors.add(new Professor("Mario", "Rossi", "mario.rossi@gmail.com"));
        this.addToCache("mario.rossi@gmail.com", professors.getFirst());
    }

    @Override
    public Professor getProfessorByEmail(String email) throws DAOException {
        if (professors.isEmpty()) populateProfessors();

        if (containsKey(email)) {
            return getFromCache(email);
        }

        return null;
    }
}

package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;

import java.util.List;

public class ChoiceFSDAO extends ChoiceDAO {
    @Override
    public Choice getChoiceById(int choiceID) throws DAOException{
        return null;
    }

    @Override
    public List<Choice> getChoicesByQuestionId(int questionID) throws DAOException{
        return null;
    }

    @Override
    public Choice getQuestionSolution(int questionID) throws DAOException{
        return null;
    }
}

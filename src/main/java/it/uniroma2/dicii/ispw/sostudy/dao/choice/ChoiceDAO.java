package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;

import java.util.List;

public abstract class ChoiceDAO extends CacheDAO<Integer, Choice> {
    public abstract Choice getChoiceById(int choiceID) throws DAOException;
    public abstract List<Choice> getChoicesByQuestionId(int questionID) throws DAOException;
    public abstract Choice getQuestionSolution(int questionID) throws DAOException;
}

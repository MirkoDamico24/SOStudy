package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.eng.functional.ChoiceDTO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;

import java.util.List;

public abstract class ChoiceDAO{
    public abstract Choice getChoiceById(int choiceID) throws DAOException;
    public abstract ChoiceDTO getChoicesByQuestionId(int questionID) throws DAOException;
    public abstract void saveChoices(List<ChoiceDTO> choices) throws DAOException;
    public abstract int getChoiceId(Choice choice, int questionId) throws DAOException;
}

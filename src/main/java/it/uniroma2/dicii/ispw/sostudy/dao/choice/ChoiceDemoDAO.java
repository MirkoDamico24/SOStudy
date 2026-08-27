package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.ChoiceDTO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionDTO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionMapper;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;
import it.uniroma2.dicii.ispw.sostudy.model.Question;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;

import java.util.Collections;
import java.util.List;

public class ChoiceDemoDAO extends ChoiceDAO{
    @Override
    public Choice getChoiceById(int choiceID) throws DAOException {
        if(this.containsKey(choiceID)) {
            return getFromCache(choiceID);
        }
        return null;
    }

    @Override
    public ChoiceDTO getChoicesByQuestionId(int questionID) throws DAOException{
        Question question = DAOFactory.getInstance().getQuestionDAO().getQuestionById(questionID);
        QuestionDTO questionDTO = QuestionMapper.questionToDTO(question);
        if(questionDTO.type() == QuestionType.CLOSEQUESTION) {
            ChoiceDTO dto = new ChoiceDTO(questionDTO.options(), questionDTO.solution(), questionID);
            return dto;
        }
        throw new DAOException("Open Answers have no choices associated.");
    }

    @Override
    public void saveChoices(List<ChoiceDTO> choices) throws DAOException{
        int id = Collections.max(this.getKeys()) + 1;
        for(ChoiceDTO choiceDTO : choices){
            for(Choice choice: choiceDTO.options()) addToCache(id, choice);
        }
    }

    @Override
    public int getChoiceId(Choice choice, int questionId) throws DAOException{
        //use case 'evaluate knowledge' not implemented in demo version
        return 0;
    }
}

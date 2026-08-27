package it.uniroma2.dicii.ispw.sostudy.dao.question;


import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Question;

import java.util.*;

public class QuestionDemoDAO extends QuestionDAO {
    @Override
    public Question getQuestionById(int questionID){
        return null;
    }

    @Override
    public List<Question> getQuestionsByTestId(int testID){
        return new ArrayList<>();
    }

    @Override
    public void saveTestQuestion(int testID, List<Question> question){
        int questionID = Collections.max(this.getKeys()) + 1;
        for(Question q : question){
            addToCache(questionID, q);
            questionID++;
        }
    }

    @Override
    public Map<Integer, List<Question>> getQuestionsByTestIds(List<Integer> testIDs){
        //TODO: implement
        return new HashMap<>();
    }

    @Override
    public Integer getQuestionId(Question question, int testID) throws DAOException{
        //use case 'evaluate knowledge' not implemented in demo version
        return null;
    }
}

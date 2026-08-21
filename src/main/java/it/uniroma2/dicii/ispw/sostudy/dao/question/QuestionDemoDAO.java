package it.uniroma2.dicii.ispw.sostudy.dao.question;


import it.uniroma2.dicii.ispw.sostudy.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //yet to implement
    }

    @Override
    public Map<Integer, List<Question>> getQuestionsByTestIds(List<Integer> testIDs){
        //TODO: implement
        return new HashMap<>();
    }

    @Override
    public Integer getQuestionId(Question question, int testID){
        //TODO: implement
        return 0;
    }
}

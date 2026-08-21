package it.uniroma2.dicii.ispw.sostudy.dao.question;

import it.uniroma2.dicii.ispw.sostudy.model.Question;

import java.util.List;
import java.util.Map;

public class QuestionFSDAO extends QuestionDAO {
    @Override
    public Question getQuestionById(int questionID){
        return null;
    }

    @Override
    public List<Question> getQuestionsByTestId(int testID){
        return null;
    }

    @Override
    public void saveTestQuestion(int testID, List<Question> question){

    }

    @Override
    public Map<Integer, List<Question>> getQuestionsByTestIds(List<Integer> testIDs){
        //TODO: implement
        return null;
    }

    @Override
    public int getQuestionId(Question question, int testID){
        //TODO: implement
        return 0;
    }
}

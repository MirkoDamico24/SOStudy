package it.uniroma2.dicii.ispw.sostudy.dao.question;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.model.Question;

import java.util.List;
import java.util.Map;

public abstract class QuestionDAO extends CacheDAO<Integer, Question> {
    public abstract Question getQuestionById(int questionID);
    public abstract List<Question> getQuestionsByTestId(int testID);
    public abstract void saveTestQuestion(int testID, List<Question> question);
    public abstract Map<Integer, List<Question>> getQuestionsByTestIds(List<Integer> testIDs);
    public abstract Integer getQuestionId(Question question, int testID);
}

package it.uniroma2.dicii.ispw.sostudy.dao.question;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.model.Question;

import java.util.List;

public abstract class QuestionDAO extends CacheDAO<Integer, Question> {
    public abstract Question getQuestionById(int questionID);
    public abstract List<Question> getQuestionsByTestId(int testID);
}

package it.uniroma2.dicii.ispw.sostudy.dao.question;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Question;

import java.util.*;

public class QuestionDemoDAO extends QuestionDAO {

    /**
     * The following map is used to associate each question
     * to the corresponding test. It is needed because we
     * decided to not add a reference to a Test object into
     * the Question class.
     */
    private final Map<Integer, List<Integer>> questionsByTest = new HashMap<>();

    @Override
    public Question getQuestionById(int questionID){
        if(this.containsKey(questionID)){
            return this.getFromCache(questionID);
        }
        return null;
    }

    @Override
    public List<Question> getQuestionsByTestId(int testID){
        List<Question> questions = new ArrayList<>();

        List<Integer> questionIds = questionsByTest.get(testID);
        if(questionIds == null){
            return questions;
        }

        for(Integer questionId : questionIds){
            if(this.containsKey(questionId)){
                questions.add(this.getFromCache(questionId));
            }
        }
        return questions;
    }

    @Override
    public void saveTestQuestion(int testID, List<Question> question){
        int questionID = 1;
        if(!this.getKeys().isEmpty()) questionID = Collections.max(this.getKeys()) + 1;

        List<Integer> assignedIds = questionsByTest.computeIfAbsent(testID, k -> new ArrayList<>());

        for(Question q : question){
            addToCache(questionID, q);
            assignedIds.add(questionID);
            questionID++;
        }
    }

    @Override
    public Map<Integer, List<Question>> getQuestionsByTestIds(List<Integer> testIDs){
        Map<Integer, List<Question>> result = new HashMap<>();

        if(testIDs == null || testIDs.isEmpty()){
            return result;
        }

        for(Integer testID : testIDs){
            result.put(testID, getQuestionsByTestId(testID));
        }

        return result;
    }

    @Override
    public Integer getQuestionId(Question question, int testID) throws DAOException{
        if(!questionsByTest.containsKey(testID)) throw new DAOException("The provided test id does not exist");

        for(Integer questionId : questionsByTest.get(testID)){
            Question tmp = getQuestionById(questionId);
            if(tmp.getHeader() == question.getHeader()) return questionId;
        }

        return null;
    }
}
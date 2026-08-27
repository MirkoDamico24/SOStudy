package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TestDemoDAO extends TestDAO {
    private DAOFactory factory = DAOFactory.getInstance();
    private QuestionDAO questionDAO = factory.getQuestionDAO();

    private List<Test> populateTest(VirtualClass virtualClass){
        List<Question> questions = new ArrayList<>();

        CloseQuestion closeQuestion = new CloseQuestion("Qual è il pattern GOF che si applica quando si vogliono aggiungere dinamicamente responsabilità ad una classe?", 10);
        Choice c1 = new Choice("Observer");
        Choice c2 = new Choice("Adapter");
        Choice c3 = new Choice("Decorator");
        closeQuestion.addChoice(List.of(c1, c2, c3));
        try {
            closeQuestion.addSolution(closeQuestion.getChoices().get(2));
        } catch (ModelException e) {
            throw new DAOException("Error occurred while populating demo question data. " + e.getMessage());
        }
        questions.add(closeQuestion);

        questions.add(new OpenQuestion("Descrivere gli interl steps di un caso d'uso di un sistema di gestione della carriera universitaria.", 10));

        Test demoTest = new Test("ISPWdemoTest", LocalDate.now().plusDays(7), LocalTime.of(23, 59),
                Duration.ofMinutes(30), questions, virtualClass);

        int testId = 1;
        if(!this.getKeys().isEmpty()) testId = Collections.max(this.getKeys()) + 1;

        this.addToCache(testId, demoTest);

        questionDAO.saveTestQuestion(testId, demoTest.getQuestions());

        List<Test> tests = new ArrayList<>();
        tests.add(demoTest);
        return tests;
    }

    @Override
    public Test getTestById(int id){
        if(containsKey(id)){
            return getFromCache(id);
        }

        return null;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        int id = 1;
        if(!this.getKeys().isEmpty()) id = Collections.max(this.getKeys()) + 1;
        addToCache(id, test);

        questionDAO.saveTestQuestion(id, test.getQuestions());
    }


    @Override
    public List<Test> getTestByClassId(int classId, VirtualClass virtualClass) throws DAOException{
        Set<Integer> ids = this.getKeys();
        List<Test> allTests = new ArrayList<>();
        for(Integer id : ids){
            if(getFromCache(id).getVirtualClass().equals(virtualClass)){
                allTests.add(getFromCache(id));
            }
        }

        if(allTests.isEmpty()) allTests = populateTest(virtualClass);

        return allTests;
    }

    @Override
    public List<TestAttempt> getTestAttempt(Test test) throws DAOException{
        //use case 'evaluate knowledge' not implemented in demo version
        return new ArrayList<>();
    }

}
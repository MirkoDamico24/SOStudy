package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.exception.ExsistingTestExcpetion;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.util.ArrayList;
import java.util.List;

public class CreateTestController {


    public Test createTest(TestBean test, List<QuestionBean> questions) throws ExsistingTestExcpetion, ControllerException{
        TestDAO td = DAOFactory.getInstance().getTestDAO();
        if(td.testExists(test.getName())) throw new ExsistingTestExcpetion("Un testo con questo nome è già stato creato");

        VirtualClassDAO virtualClassDAO = DAOFactory.getInstance().getVirtualClassDAO();
        VirtualClass cls = virtualClassDAO.getVirtualClassByName(test.getVirtualClass());

        List<Question> questionList = getQuestions(questions);

        Test newTest = new Test(test.getName(), test.getDueDate(), test.getDueTime(), test.getDuration(), questionList, cls);
        try {
            td.saveTest(newTest);
        }
        catch (DAOException e) {
            throw new ControllerException("Errore durante il salvataggio del test", e);
        }
        cls.addTest(newTest);

        return newTest;
    }

    private List<Question> getQuestions(List<QuestionBean> questions) {
        List<Question> questionList = new ArrayList<>();
        Question tempQuestion;
        for(QuestionBean question : questions){
            if(question.getOptions() == null || question.getOptions().isEmpty()){
                tempQuestion = new OpenQuestion(question.getHeader(), question.getMaxScore());
            }
            else{
                List<Choice> choices = new ArrayList<>();
                for(String option : question.getOptions()) {
                    Choice choice = new Choice(option);
                    choices.add(choice);
                }
                Choice solution = choices.get(question.getSolution());
                tempQuestion = new CloseQuestion(question.getHeader(), question.getMaxScore(), choices, solution);

            }
            questionList.add(tempQuestion);
        }
        return questionList;
    }

}

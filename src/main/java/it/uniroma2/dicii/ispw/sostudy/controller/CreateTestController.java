package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.util.ArrayList;
import java.util.List;

public class CreateTestController {


    public Test createTest(TestBean test, List<QuestionBean> questions){

        VirtualClassDAO virtualClassDAO = DAOFactory.getInstance().getVirtualClassDAO();
        VirtualClass cls = virtualClassDAO.getVirtualClassByName(test.getName());

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

        Test newTest = new Test(test.getName(), test.getDueDate(), test.getDueTime(), test.getDuration(), questionList, cls);
        //TODO: implement DB logic to make the test persistent
        cls.addTest(newTest);

        return newTest;
    }
    
}

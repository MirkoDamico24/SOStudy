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

    private VirtualClass getClass(int sessionID, String virtualClass) throws ControllerException {
        List<VirtualClass> vcls = null;

        VirtualClassDAO classDAO = DAOFactory.getInstance().getVirtualClassDAO();
        Session s = SessionManager.getInstance().getSession(sessionID);
        try {
            vcls = classDAO.getClassesByProfessor(s.getCurrentProfessor().getEmail());
        }
        catch (DAOException e) {
            throw new ControllerException(e.getMessage());
        }

        for(VirtualClass vClass : vcls){
            if(vClass.getName().equals(virtualClass)){
                return vClass;
            }
        }
        return null;
    }

    public void createTest(int sessionID, TestBean test, List<QuestionBean> questions) throws ControllerException{
        TestDAO td = DAOFactory.getInstance().getTestDAO();

        VirtualClass cls = getClass(sessionID, test.getVirtualClass());
        if(cls == null){
            throw new ControllerException("Class not found");
        }

        List<Question> questionList = getQuestions(questions);

        Test newTest = new Test(test.getName(), test.getDueDate(), test.getDueTime(), test.getDuration(), questionList, cls);
        try {
            td.saveTest(newTest);
        }
        catch (DAOException e) {
            throw new ControllerException("Errore durante il salvataggio del test", e);
        }
        cls.addTest(newTest);
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

package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.*;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionMapper;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.TestTimerService;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeEvaluationController {
    private DAOFactory factory = DAOFactory.getInstance();
    private VirtualClassDAO classDAO = factory.getVirtualClassDAO();
    private TestAttemptDAO testAttemptDAO = factory.getTestAttemptDAO();

    public List<VirtualClassBean> getUserClasses(SessionBean sessionBean){
        List<VirtualClass> vcls = null;
        List<VirtualClassBean> beans = new ArrayList<>();
        try {
            switch(sessionBean.getCurrentRole()){
                case PROFESSOR -> vcls = classDAO.getClassesByProfessor(sessionBean.getProfessor().getEmail());
                case STUDENT -> vcls = classDAO.getClassesByStudent(sessionBean.getStudent().getEmail());
                default -> throw new ControllerException("Invalid session role");
            }
        }
        catch(DAOException e){
            throw new ControllerException("Errore durante la ricerca della classe");
        }

        for(VirtualClass vcl : vcls){
            VirtualClassBean tmp = new VirtualClassBean(vcl.getName(),
                    new ProfessorBean(vcl.getProf().getName(), vcl.getProf().getSurname(), vcl.getProf().getEmail()));
            List<TestBean> tmpBean = testToBean(vcl.getAvailableTests());
            tmp.setTest(tmpBean);
            beans.add(tmp);
        }

        return beans;
    }

    private List<TestBean> testToBean(List<Test> toConvert){
        List<TestBean> beans = new ArrayList<>();
        for(Test test : toConvert){
            beans.add(new TestBean(test.getName(), test.getDueDate(), test.getDueTime(), test.getDuration(), test.getVirtualClass().getName()));
        }
        return beans;
    }

    public List<QuestionBean> loadRequiredTest(SessionBean session, TestBean testBean) {
        List<VirtualClass> vcls = null;
        VirtualClass selectedClass = null;
        Test toTake = null;

        Session currentSession = SessionManager.getInstance().getSession(session.getSessionID());

        try {
            switch(currentSession.getRole()){
                case PROFESSOR -> vcls = classDAO.getClassesByProfessor(currentSession.getCurrentProfessor().getEmail());
                case STUDENT -> vcls = classDAO.getClassesByStudent(currentSession.getCurrentStudent().getEmail());
                default -> throw new ControllerException("Invalid session role");
            }
        }
        catch(DAOException e){
            throw new ControllerException("Errore durante la ricerca della classe");
        }

        for(VirtualClass virtualClass : vcls){
            if(virtualClass.getName().equals(testBean.getVirtualClass())){
                selectedClass = virtualClass;
                break;
            }
        }

        //extract test from class' test list
        for(Test test : selectedClass.getAvailableTests()){
            if(test.getName().equals(testBean.getName())){
                toTake = test;
                break;
            }
        }

        LocalDateTime toCheck = LocalDateTime.of(toTake.getDueDate(), toTake.getDueTime());
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        if(!toCheck.isAfter(now)){
            throw new  ControllerException("Termini di consegna del test scaduti.");
        }

        //add test to session
        currentSession.setCurrentTest(toTake);

        if(currentSession.getRole() == UserRole.STUDENT){
            TestAttempt attempt = new TestAttempt(toTake, currentSession.getCurrentStudent(), LocalDate.now());
            currentSession.setCurrentAttempt(attempt);
            TestTimerService timer = new TestTimerService(LocalDateTime.now(), toTake.getDuration());
            session.setTimer(timer);
            timer.start();
        }

        return questionToBean(toTake.getQuestions());
    }

    private List<QuestionBean> questionToBean(List<Question> questions){
        int index = 0;
        List<QuestionBean> questionBeans = new ArrayList<>();
        for(Question question : questions){
            QuestionBean tmp = QuestionMapper.questionToBean(question);
            tmp.setPositionInTest(++index);
            questionBeans.add(tmp);
        }
        return questionBeans;
    }

    public void registerAnswer(SessionBean sessionBean, QuestionBean question, AnswerBean answer){
        Session currentSession = SessionManager.getInstance().getSession(sessionBean.getSessionID());
        Question current = currentSession.getCurrentTest().getQuestions().get(question.getPositionInTest() - 1);

        //instantiate answer and link with current
        Answer currentAnswer = current.createAnswer(answer.getTextualContent(), answer.getChosenOption());

        currentSession.getCurrentAttempt().addAnswer(currentAnswer);
    }

    public void submitAttempt(SessionBean sessionBean){
        Session currentSession = SessionManager.getInstance().getSession(sessionBean.getSessionID());

        //grading auto-valuable questions
        TestAttempt attempt =  currentSession.getCurrentAttempt();
        attempt.setHandInTime(LocalTime.now());
        Test test = attempt.getTest();
        test.addTestAttempt(attempt);

        try{
            test.gradeTest(attempt);
        }
        catch(ModelException e){
            throw new ControllerException("Errore durante la valutazione del test. " + e.getMessage());
        }

        try {
            testAttemptDAO.saveTestAttempt(currentSession.getCurrentAttempt());
        }
        catch(DAOException e){
            throw new ControllerException("Errore durante il salvataggio del tentativo. " + e.getMessage());
        }
    }


}

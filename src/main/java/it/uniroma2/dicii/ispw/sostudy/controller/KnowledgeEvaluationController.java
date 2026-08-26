package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.*;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.AttemptMapper;
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
    private TestDAO testDAO = factory.getTestDAO();

    private List<VirtualClass> obtainClasses(SessionBean sessionBean) throws ControllerException {
        List<VirtualClass> vcls = null;
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

        return vcls;
    }

    public List<VirtualClassBean> getUserClasses(SessionBean sessionBean){
        List<VirtualClassBean> beans = new ArrayList<>();

        List<VirtualClass> vcls = obtainClasses(sessionBean);

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

    private VirtualClass getSelectedClass(List<VirtualClass> classes, TestBean testBean, Session session){
        VirtualClass selectedClass = null;
        for(VirtualClass virtualClass : classes){
            if(virtualClass.getName().equals(testBean.getVirtualClass())){
                selectedClass = virtualClass;
                session.setCurrentClass(selectedClass);
                break;
            }
        }
        return selectedClass;
    }

    public List<QuestionBean> loadRequiredTest(SessionBean session, TestBean testBean) throws ControllerException{
        Test toTake = null;

        Session currentSession = SessionManager.getInstance().getSession(session.getSessionID());

        List<VirtualClass> vcls = obtainClasses(session);

        VirtualClass selectedClass = getSelectedClass(vcls, testBean, currentSession);

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
            TestAttempt attempt = new TestAttempt(toTake, currentSession.getCurrentStudent(), LocalDate.now(ZoneId.systemDefault()));
            currentSession.setCurrentAttempt(attempt);
            TestTimerService timer = new TestTimerService(LocalDateTime.now(ZoneId.systemDefault()), toTake.getDuration());
            session.setTimer(timer);
            timer.start();
        }

        return questionToBean(toTake.getQuestions());
    }

    public List<AttemptBean> loadTestAttempts(SessionBean session, TestBean testBean) throws ControllerException{
        List<VirtualClass> vcls = obtainClasses(session);

        Session currentSession = SessionManager.getInstance().getSession(session.getSessionID());

        VirtualClass selectedClass = getSelectedClass(vcls, testBean, currentSession);

        Test toEvaluate = null;
        for(Test test : selectedClass.getAvailableTests()){
            if(test.getName().equals(testBean.getName())){
                toEvaluate = test;
                currentSession.setCurrentTest(test);
                break;
            }
        }

        List<TestAttempt> availableAttempt = null;
        if(toEvaluate.getTests() == null){
            try{
                availableAttempt = testDAO.getTestAttempt(toEvaluate);
                if(availableAttempt.isEmpty()) return null;
            }
            catch(DAOException e){
                throw new ControllerException("Errore durante il carciamento dei test svolti dagli studenti.");
            }
        }
        else availableAttempt = toEvaluate.getTests();


        List<AttemptBean> beans = new ArrayList<>();
        for(TestAttempt attempt : availableAttempt){
            if(attempt.getTestGradingStatus() != TestGradingStatus.INCOMPLETE) break;

            beans.add(AttemptMapper.toBean(attempt));
        }

        return beans;
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

    public void submitAttempt(SessionBean sessionBean) throws ControllerException{
        Session currentSession = SessionManager.getInstance().getSession(sessionBean.getSessionID());

        //grading auto-valuable questions
        TestAttempt attempt =  currentSession.getCurrentAttempt();
        attempt.setHandInTime(LocalTime.now(ZoneId.systemDefault()));
        Test test = attempt.getTest();

        try{
            test.gradeTest(attempt);
        }
        catch(ModelException e){
            throw new ControllerException("Errore durante la valutazione del test. " + e.getMessage());
        }

        test.addTestAttempt(attempt);

        try {
            testAttemptDAO.saveTestAttempt(attempt);
        }
        catch(DAOException e){
            throw new ControllerException("Errore durante il salvataggio del tentativo. " + e.getMessage());
        }
    }

    public void registerEvaluation(SessionBean session, AttemptBean attempt) throws ControllerException{
        Session currentSession = SessionManager.getInstance().getSession(session.getSessionID());

        Test currentTest = currentSession.getCurrentTest();
        if(currentTest == null) throw new ControllerException("Nessun test associato alla sessione.");

        TestAttempt toUpdate = null;
        for(TestAttempt testAttempt : currentTest.getTests()){
            if(testAttempt.getStudent().getEmail().equals(attempt.getStudent().getEmail())){
                toUpdate = testAttempt;
                break;
            }
        }

        if(toUpdate == null) throw new ControllerException("Il test selezionato non contiene il tentativo che si sta valutando.");

        for(AnswerBean answer : attempt.getAnswers()){
            Answer a = toUpdate.getAnswer(answer.getTextualContent());
            if(a == null) throw new ControllerException("La risposta fornita non è stata trovata nel tentativo. Test compromesso");
            a.setScore(answer.getAssignedScore());
        }

        currentTest.gradeTest(toUpdate);
        toUpdate.setTestGradingStatus(TestGradingStatus.FULLYGRADED);

        try{
            testAttemptDAO.updateTestAttempt(toUpdate);
        }
        catch(DAOException e){
            throw new ControllerException("Errore durante l'aggiornamento della valutazione.");
        }
    }

}

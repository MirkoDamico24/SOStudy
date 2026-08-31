package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.*;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.AttemptMapper;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

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

    private Test getSelectedTest(TestBean testBean, SessionBean session){
        List<VirtualClass> vcls = obtainClasses(session);

        Session currentSession = SessionManager.getInstance().getSession(session.getSessionID());

        VirtualClass selectedClass = getSelectedClass(vcls, testBean, currentSession);

        Test selected = null;
        for(Test test : selectedClass.getAvailableTests()){
            if(test.getName().equals(testBean.getName())){
                selected = test;
                currentSession.setCurrentTest(test);
                break;
            }
        }

        return selected;
    }

    private List<TestAttempt> retireTestAttempts(Test test){
        List<TestAttempt> availableAttempt = null;
        if(test.getTests() == null){
            try{
                availableAttempt = testDAO.getTestAttempt(test);
                if(availableAttempt.isEmpty()) return null;
            }
            catch(DAOException e){
                throw new ControllerException("Errore durante il carciamento dei test svolti dagli studenti.");
            }
        }
        else availableAttempt = test.getTests();

        return availableAttempt;
    }

    public List<AttemptBean> loadTestAttempts(SessionBean session, TestBean testBean) throws ControllerException{
        Test toEvaluate = getSelectedTest(testBean, session);

        List<TestAttempt> availableAttempt = retireTestAttempts(toEvaluate);
        if(availableAttempt == null) return new ArrayList<>();

        List<AttemptBean> beans = new ArrayList<>();
        for(TestAttempt attempt : availableAttempt){
            if(attempt.getTestGradingStatus() != TestGradingStatus.INCOMPLETE) break;

            beans.add(AttemptMapper.toBean(attempt));
        }

        return beans;
    }

    private void requestAttemptUpdate(TestAttempt attempt){
        try{
            testAttemptDAO.updateTestAttempt(attempt);
        }
        catch(DAOException e){
            throw new ControllerException("Errore durante l'aggiornamento della valutazione.");
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
        toUpdate.setTestGradingStatus(TestGradingStatus.PENDING);

        //notification to student
        NotificationController msgctrl = new NotificationController();
        msgctrl.sendNewEvaluationNotification(currentTest, toUpdate);

        requestAttemptUpdate(toUpdate);
    }

    public Integer checkGradeToAccept(SessionBean session, TestBean testBean) throws ControllerException{
        Test toEvaluate = getSelectedTest(testBean, session);
        List<TestAttempt> availableAttempt = retireTestAttempts(toEvaluate);

        Session currentSession = SessionManager.getInstance().getSession(session.getSessionID());

        for(TestAttempt attempt : availableAttempt){
            if(attempt.getStudent().getEmail().equals(session.getStudent().getEmail()) && attempt.getTestGradingStatus() == TestGradingStatus.PENDING){
                currentSession.setCurrentAttempt(attempt);
                return attempt.getGrade();
            }
        }
        return null;
    }

    public void acceptGrade(SessionBean session, boolean accepted) throws ControllerException{
        Session  currentSession = SessionManager.getInstance().getSession(session.getSessionID());
        TestAttempt attempt = currentSession.getCurrentAttempt();
        if(accepted) attempt.setTestGradingStatus(TestGradingStatus.FULLYGRADED);
        else {
            attempt.setTestGradingStatus(TestGradingStatus.REVISIONING);
            Test test = attempt.getTest();
            VirtualClass vcls = test.getVirtualClass();
            Professor prof = vcls.getProf();
            NotificationController msgctrl = new NotificationController();
            msgctrl.sendRevisionNotification(prof, attempt);
        }

        requestAttemptUpdate(attempt);
    }
}

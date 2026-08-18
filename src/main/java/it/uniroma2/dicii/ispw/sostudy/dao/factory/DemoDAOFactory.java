package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDemoDAO;

public class DemoDAOFactory extends DAOFactory {

    AuthenticationDemoDAO authenticationDAO = null;
    ProfessorDemoDAO professorDAO = null;
    MessageDemoDAO messageDAO = null;
    TestDemoDAO testDAO = null;
    TestAttemptDemoDAO testAttemptDAO = null;
    StudentDemoDAO studentDAO = null;
    VirtualClassDemoDAO virtualClassDAO = null;
    ChoiceDemoDAO choiceDAO = null;
    QuestionDemoDAO questionDAO = null;


    @Override
    public AuthenticationDAO getAuthenticationDAO(){
        if(authenticationDAO == null) authenticationDAO = new AuthenticationDemoDAO();
        return authenticationDAO;
    }

    @Override
    public ProfessorDAO getProfessorDAO(){
        if(professorDAO == null) professorDAO = new ProfessorDemoDAO();
        return professorDAO;
    }

    @Override
    public StudentDAO getStudentDAO(){
        if(studentDAO == null) studentDAO = new StudentDemoDAO();
        return studentDAO;
    }

    @Override
    public VirtualClassDAO getVirtualClassDAO(){
        if(virtualClassDAO == null) virtualClassDAO = new VirtualClassDemoDAO();
        return virtualClassDAO;
    }

    @Override
    public TestDAO getTestDAO() {
        if(testDAO == null) testDAO = new TestDemoDAO();
        return testDAO;
    }

    @Override
    public TestAttemptDAO getTestAttemptDAO() {
        if(testAttemptDAO == null) testAttemptDAO = new TestAttemptDemoDAO();
        return testAttemptDAO;
    }

    @Override
    public QuestionDAO getQuestionDAO() {
        if(questionDAO == null) questionDAO = new QuestionDemoDAO();
        return questionDAO;
    }

    @Override
    public ChoiceDAO getChoiceDAO() {
        if(choiceDAO == null) choiceDAO = new ChoiceDemoDAO();
        return choiceDAO;
    }

    @Override
    public MessageDAO getMessageDAO() {
        if(messageDAO == null) messageDAO = new MessageDemoDAO();
        return messageDAO;
    }

}

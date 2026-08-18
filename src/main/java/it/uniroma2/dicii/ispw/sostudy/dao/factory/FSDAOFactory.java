package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassFSDAO;

public class FSDAOFactory extends DAOFactory {
    AuthenticationFSDAO authenticationDAO = null;
    ProfessorFSDAO professorDAO = null;
    MessageFSDAO messageDAO = null;
    TestFSDAO testDAO = null;
    TestAttemptFSDAO testAttemptDAO = null;
    StudentFSDAO studentDAO = null;
    VirtualClassFSDAO virtualClassDAO = null;
    ChoiceFSDAO choiceDAO = null;
    QuestionFSDAO questionDAO = null;


    @Override
    public AuthenticationDAO getAuthenticationDAO(){
        if(authenticationDAO == null) authenticationDAO = new AuthenticationFSDAO();
        return authenticationDAO;
    }

    @Override
    public ProfessorDAO getProfessorDAO(){
        if(professorDAO == null) professorDAO = new ProfessorFSDAO();
        return professorDAO;
    }

    @Override
    public StudentDAO getStudentDAO(){
        if(studentDAO == null) studentDAO = new StudentFSDAO();
        return studentDAO;
    }

    @Override
    public VirtualClassDAO getVirtualClassDAO(){
        if(virtualClassDAO == null) virtualClassDAO = new VirtualClassFSDAO();
        return virtualClassDAO;
    }

    @Override
    public TestDAO getTestDAO() {
        if(testDAO == null) testDAO = new TestFSDAO();
        return testDAO;
    }

    @Override
    public TestAttemptDAO getTestAttemptDAO() {
        if(testAttemptDAO == null) testAttemptDAO = new TestAttemptFSDAO();
        return testAttemptDAO;
    }

    @Override
    public QuestionDAO getQuestionDAO() {
        if(questionDAO == null) questionDAO = new QuestionFSDAO();
        return questionDAO;
    }

    @Override
    public ChoiceDAO getChoiceDAO() {
        if(choiceDAO == null) choiceDAO = new ChoiceFSDAO();
        return choiceDAO;
    }

    @Override
    public MessageDAO getMessageDAO() {
        if(messageDAO == null) messageDAO = new MessageFSDAO();
        return messageDAO;
    }

}

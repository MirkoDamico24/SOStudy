package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDBDAO;

public class DBDAOFactory extends DAOFactory {
    AuthenticationDBDAO authenticationDBDAO = null;
    ProfessorDBDAO professorDBDAO = null;
    MessageDBDAO messageDBDAO = null;
    TestDBDAO testDBDAO = null;
    TestAttemptDBDAO testAttemptDBDAO = null;
    StudentDBDAO studentDBDAO = null;
    VirtualClassDBDAO virtualClassDBDAO = null;
    ChoiceDBDAO choiceDBDAO = null;
    QuestionDBDAO questionDBDAO = null;


    @Override
    public AuthenticationDBDAO getAuthenticationDAO(){
        if(authenticationDBDAO == null) authenticationDBDAO = new AuthenticationDBDAO();
        return authenticationDBDAO;
    }

    @Override
    public ProfessorDBDAO getProfessorDAO(){
        if(professorDBDAO == null) professorDBDAO = new ProfessorDBDAO();
        return professorDBDAO;
    }

    @Override
    public StudentDBDAO getStudentDAO(){
        if(studentDBDAO == null) studentDBDAO = new StudentDBDAO();
        return studentDBDAO;
    }

    @Override
    public VirtualClassDAO getVirtualClassDAO(){
        if(virtualClassDBDAO == null) virtualClassDBDAO = new VirtualClassDBDAO();
        return virtualClassDBDAO;
    }

    @Override
    public TestDAO getTestDAO() {
        if(testDBDAO == null) testDBDAO = new TestDBDAO();
        return testDBDAO;
    }

    @Override
    public TestAttemptDAO getTestAttemptDAO() {
        if(testAttemptDBDAO == null) testAttemptDBDAO = new TestAttemptDBDAO();
        return testAttemptDBDAO;
    }

    @Override
    public QuestionDAO getQuestionDAO() {
        if(questionDBDAO == null) questionDBDAO = new QuestionDBDAO();
        return questionDBDAO;
    }

    @Override
    public ChoiceDAO getChoiceDAO() {
        if(choiceDBDAO == null) choiceDBDAO = new ChoiceDBDAO();
        return choiceDBDAO;
    }

    @Override
    public MessageDAO getMessageDAO() {
        if(messageDBDAO == null) messageDBDAO = new MessageDBDAO();
        return messageDBDAO;
    }

}

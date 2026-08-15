package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassFSDAO;

public class FSDAOFactory extends DAOFactory {

    @Override
    public AuthenticationDAO getAuthenticationDAO(){
        return new AuthenticationFSDAO();
    }

    @Override
    public ProfessorFSDAO getProfessorDAO(){
        return new ProfessorFSDAO();
    }

    @Override
    public StudentFSDAO getStudentDAO(){
        return new StudentFSDAO();
    }

    @Override
    public VirtualClassDAO getVirtualClassDAO(){
        return new VirtualClassFSDAO();
    }

    @Override
    public TestDAO getTestDAO() {return new TestFSDAO(); }

    @Override
    public TestAttemptDAO getTestAttemptDAO() {return new TestAttemptDBDAO(); }

    @Override
    public QuestionDAO getQuestionDAO() {return new QuestionFSDAO(); }

    @Override
    public ChoiceDAO getChoiceDAO() {return new ChoiceFSDAO(); }
}

package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDemoDAO;

public class DemoDAOFactory extends DAOFactory {
    @Override
    public AuthenticationDAO getAuthenticationDAO(){
        return new AuthenticationDemoDAO();
    }

    @Override
    public ProfessorDemoDAO getProfessorDAO(){
        return new ProfessorDemoDAO();
    }

    @Override
    public StudentDemoDAO getStudentDAO(){
        return new StudentDemoDAO();
    }

    @Override
    public VirtualClassDAO getVirtualClassDAO(){
        return new VirtualClassDemoDAO();
    }

    @Override
    public TestDAO getTestDAO() {return new TestDemoDAO();}

    @Override
    public TestAttemptDAO getTestAttemptDAO() {return new TestAttemptDemoDAO();}

    @Override
    public QuestionDAO getQuestionDAO() {return new QuestionDemoDAO();}

    @Override
    public ChoiceDAO getChoiceDAO() {return new ChoiceDemoDAO();}
}

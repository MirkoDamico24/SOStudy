package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDBDAO;

public class DBDAOFactory extends DAOFactory {
    @Override
    public AuthenticationDBDAO getAuthenticationDAO(){
        return new AuthenticationDBDAO();
    }

    @Override
    public ProfessorDBDAO getProfessorDAO(){
        return new ProfessorDBDAO();
    }

    @Override
    public StudentDBDAO getStudentDAO(){
        return new StudentDBDAO();
    }

    @Override
    public VirtualClassDAO getVirtualClassDAO(){
        return new VirtualClassDBDAO();
    }

    @Override
    public TestDAO getTestDAO() {return new TestDBDAO(); }

    @Override
    public TestAttemptDAO getTestAttemptDAO() {return new TestAttemptDBDAO(); }
}

package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDBDAO;
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
}

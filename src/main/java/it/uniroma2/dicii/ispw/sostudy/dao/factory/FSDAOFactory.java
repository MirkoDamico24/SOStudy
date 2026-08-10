package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.Authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Authentication.AuthenticationFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Professor.ProfessorFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Student.StudentFSDAO;

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
}

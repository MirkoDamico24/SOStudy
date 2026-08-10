package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.Authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Authentication.AuthenticationDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Professor.ProfessorDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Student.StudentDemoDAO;

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
}

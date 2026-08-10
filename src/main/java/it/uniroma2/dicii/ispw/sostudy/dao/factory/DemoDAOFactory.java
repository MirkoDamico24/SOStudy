package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDemoDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDemoDAO;

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

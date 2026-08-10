package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.Authentication.AuthenticationDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Professor.ProfessorDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.Student.StudentDBDAO;

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
}

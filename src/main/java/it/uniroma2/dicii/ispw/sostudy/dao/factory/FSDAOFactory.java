package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorFSDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentFSDAO;

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

package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDBDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDBDAO;
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
}

package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.application.PasswdHelper;
import it.uniroma2.dicii.ispw.sostudy.bean.ProfessorBean;
import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.StudentBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.InvalidCredentialException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Session;
import it.uniroma2.dicii.ispw.sostudy.model.SessionManager;
import it.uniroma2.dicii.ispw.sostudy.model.Student;

public class LoginController {
    public SessionBean authenticate(UserBean ub) throws InvalidCredentialException {
        String email = ub.getEmail();
        String password = PasswdHelper.hashPassword(ub.getPassword());
        AuthenticationDAO dao = DAOFactory.getInstance().getAuthenticationDAO();

        if(PasswdHelper.verifyPassword(ub.getPassword(), dao.getCredentials(email))) {
            if(dao.getUserRole(email) == UserRole.PROFESSOR) {
                ProfessorDAO profDAO = DAOFactory.getInstance().getProfessorDAO();
                Professor prof = profDAO.getProfessorByEmail(email);
                Session currentSession = SessionManager.getInstance().createSession(prof);
                ProfessorBean pb = new ProfessorBean(prof.getName(), prof.getSurname(), prof.getEmail());
                return new SessionBean(pb, currentSession.getSessionID());
            }
            else if(dao.getUserRole(email) == UserRole.STUDENT) {
                StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
                Student student = studentDAO.getStudentByEmail(email);
                Session currentSession = SessionManager.getInstance().createSession(student);
                StudentBean sb = new StudentBean(student.getName(), student.getSurname(), student.getEmail());
                return new SessionBean(sb, currentSession.getSessionID());
            }
        }
        else throw new InvalidCredentialException("Invalid credential");
        return null;
    }
    
}

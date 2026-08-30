package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.controller.LoginController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {
    @Test
    public void testStudentAuthentication() {
        UserBean user = new UserBean("giuseppe.bianchi@gmail.com", "giuseppe.bianchi");

        LoginController loginController = new LoginController();
        SessionBean session = loginController.authenticate(user);
        assertEquals("STUDENT",  session.getCurrentRole().toString());
        assertEquals("giuseppe.bianchi@gmail.com", session.getStudent().getEmail());
        assertEquals("Giuseppe", session.getStudent().getName());
        assertEquals("Bianchi", session.getStudent().getSurname());
        assertNull(session.getProfessor());
    }

    @Test
    public void testProfessorAuthentication(){
        UserBean user = new UserBean("mario.rossi@gmail.com", "mario.rossi");

        LoginController loginController = new LoginController();
        SessionBean session = loginController.authenticate(user);
        assertEquals("PROFESSOR",  session.getCurrentRole().toString());
        assertEquals("mario.rossi@gmail.com", session.getProfessor().getEmail());
        assertEquals("Mario", session.getProfessor().getName());
        assertEquals("Rossi", session.getProfessor().getSurname());
        assertNull(session.getStudent());
    }
}

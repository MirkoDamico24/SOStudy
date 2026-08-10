package it.uniroma2.dicii.ispw.sostudy.dao.Authentication;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

public class AuthenticationDemoDAO implements AuthenticationDAO {
    private final String emailProf = "mario.rossi@gmail.com";
    private final String passwordProf = "mario.rossi";
    private final String emailStud = "giuseppe.bianchi@gmail.com";
    private final String passwordStud = "giuseppe.bianchi";

    @Override
    public String getCredentials(String email) throws DAOException {
        return switch(email){
            case emailProf -> passwordProf;
            case emailStud -> passwordStud;
            default -> throw new DAOException("Invalid email address");
        };
    }

     @Override
    public UserRole getUserRole(String email) throws DAOException {
        System.out.println(email);
        return switch(email){
            case emailProf -> UserRole.PROFESSOR;
            case emailStud -> UserRole.STUDENT;
            default -> throw new DAOException("Invalid email address");
        };
     }
}

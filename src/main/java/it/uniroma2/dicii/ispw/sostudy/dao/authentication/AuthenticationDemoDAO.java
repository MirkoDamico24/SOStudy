package it.uniroma2.dicii.ispw.sostudy.dao.authentication;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

public class AuthenticationDemoDAO implements AuthenticationDAO {
    private static final String EMAILPROF = "mario.rossi@gmail.com";
    private static final String PASSWORDPROF = "mario.rossi";
    private static final String EMAILSTUD = "giuseppe.bianchi@gmail.com";
    private static final String PASSWORDSTUD = "giuseppe.bianchi";

    @Override
    public String getCredentials(String email) throws DAOException {
        return switch(email){
            case EMAILPROF -> PASSWORDPROF;
            case EMAILSTUD -> PASSWORDSTUD;
            default -> throw new DAOException("Invalid email address");
        };
    }

     @Override
    public UserRole getUserRole(String email) throws DAOException {
        return switch(email){
            case EMAILPROF -> UserRole.PROFESSOR;
            case EMAILSTUD -> UserRole.STUDENT;
            default -> throw new DAOException("Invalid email address");
        };
     }
}

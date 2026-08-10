package it.uniroma2.dicii.ispw.sostudy.dao.Authentication;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

public interface AuthenticationDAO {
    String getCredentials(String email) throws DAOException;
    UserRole getUserRole(String email) throws DAOException;
}

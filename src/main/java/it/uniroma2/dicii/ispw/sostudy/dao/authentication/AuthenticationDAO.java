package it.uniroma2.dicii.ispw.sostudy.dao.authentication;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

public abstract class AuthenticationDAO {
    public abstract String getCredentials(String email) throws DAOException;
    public abstract UserRole getUserRole(String email) throws DAOException;
}

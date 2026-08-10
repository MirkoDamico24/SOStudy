package it.uniroma2.dicii.ispw.sostudy.dao.Authentication;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

public class AuthenticationFSDAO implements AuthenticationDAO{

    @Override
    public String getCredentials(String username) throws DAOException {
        return null;
    }

    @Override
    public UserRole getUserRole (String email) throws DAOException{
        return null;
    }
}

package it.uniroma2.dicii.ispw.sostudy.dao.authentication;

import it.uniroma2.dicii.ispw.sostudy.application.PasswdHelper;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthenticationDemoDAO extends AuthenticationDAO {

    @Override
    public String getCredentials(String email) throws DAOException {
        try(InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            String emailProf = prop.getProperty("profemail");
            String emailStud = prop.getProperty("studentemail");

            if(email.equals(emailProf)) {
                return PasswdHelper.hashPassword(prop.getProperty("profpasswd"));
            }
            else if(email.equals(emailStud)) {return PasswdHelper.hashPassword(prop.getProperty("studentpasswd"));}

            throw new DAOException("Invalid email address");
        }
        catch(IOException e)
        {
            throw new DAOException("Error occurred while opening the config.properties file");
        }
    }

     @Override
    public UserRole getUserRole(String email) throws DAOException {
         try(InputStream input = new FileInputStream("src/main/resources/config.properties")) {
             Properties prop = new Properties();
             prop.load(input);

             String emailProf = prop.getProperty("profemail");
             String emailStud = prop.getProperty("studentemail");

             if(email.equals(emailProf)) {
                 return UserRole.PROFESSOR;
             }
             else if(email.equals(emailStud)) {return UserRole.STUDENT;}

             throw new DAOException("Invalid email address");
         }
         catch(IOException e)
         {
             throw new DAOException("Error occurred while opening the config.properties file");
         }
     }
}

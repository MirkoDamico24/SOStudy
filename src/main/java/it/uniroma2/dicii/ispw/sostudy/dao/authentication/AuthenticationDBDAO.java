package it.uniroma2.dicii.ispw.sostudy.dao.authentication;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

import java.sql.*;

public class AuthenticationDBDAO extends AuthenticationDAO {

    private Connection getDBConnection(){
        return DBConnectionFactory.getConnection();
    }

    @Override
    public String getCredentials(String email) throws DAOException{
        String SQLquery = "SELECT Password FROM Utenti WHERE email = ?";
        try(PreparedStatement cs = getDBConnection().prepareStatement(SQLquery)){
            cs.setString(1, email);
            try(ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Password");
                }
            }
            catch (SQLException e){
                throw new DAOException("The provided email does not exist.");
            }
        }
        catch(SQLException e){
            throw new DAOException("Connection error while getting credentials.");
        }
        return null;
    }

    @Override
    public UserRole getUserRole (String email) throws DAOException{
        String SQLquery = "SELECT Ruolo FROM Utenti WHERE email = ?";
        try(PreparedStatement cs = getDBConnection().prepareStatement(SQLquery)){
            cs.setString(1, email);
            try(ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return UserRole.valueOf(rs.getString("Ruolo"));
                }
            }
            catch (SQLException e){
                throw new DAOException("The provided email does not exist.");
            }
        }
        catch(SQLException e){
            throw new DAOException("Connection error while getting credentials.");
        }
        return null;
    }
}

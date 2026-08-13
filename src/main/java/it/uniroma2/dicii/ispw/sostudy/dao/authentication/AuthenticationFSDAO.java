package it.uniroma2.dicii.ispw.sostudy.dao.authentication;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.application.PasswdHelper;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class AuthenticationFSDAO extends AuthenticationDAO {

    private static final String FILE_PATH = "data/Authentication.JSON";

    @Override
    public String getCredentials(String username) throws DAOException {
        try {
            JSONObject userObj = findUserByEmail(username);
            return (userObj != null) ? PasswdHelper.hashPassword(userObj.getString("password")) : null;
        } catch (Exception e) {
            throw new DAOException("Error reading credentials");
        }
    }

    @Override
    public UserRole getUserRole(String email) throws DAOException {
        try {
            JSONObject userObj = findUserByEmail(email);
            if (userObj != null) {
                return UserRole.valueOf(userObj.getString("role").toUpperCase());
            }
        } catch (Exception e) {
            throw new DAOException("Error reading user role");
        }
        return null;
    }

    private JSONObject findUserByEmail(String email) throws DAOException {
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("email").equals(email)) {
                    return jsonObject;
                }
            }
        }
        catch (IOException e){
            throw new DAOException("Error reading user");
        }
        return null;
    }
}
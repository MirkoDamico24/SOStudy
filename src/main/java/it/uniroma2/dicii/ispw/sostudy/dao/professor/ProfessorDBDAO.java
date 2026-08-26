package it.uniroma2.dicii.ispw.sostudy.dao.professor;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDBDAO extends ProfessorDAO {
    @Override
    public Professor getProfessorByEmail(String email) throws DAOException {
        if(this.containsKey(email)){
            return this.getFromCache(email);
        }

        String sqlQuery = "SELECT name, surname, email FROM Professor WHERE email = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Professor prof = new Professor(rs.getString("name"), rs.getString("surname"), rs.getString("email"));
                this.addToCache(rs.getString("email"), prof);
                return prof;
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while taking data from the database");
        }
        return null;
    }
}

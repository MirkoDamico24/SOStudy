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

        String SQLQuery = "SELECT name, surname, email FROM Professor WHERE email = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(SQLQuery)){
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return new Professor(rs.getString("name"), rs.getString("surname"), rs.getString("email"));
            }
            catch(SQLException e){
                throw new DAOException("Provided email does not exist");
            }

        }
        catch(SQLException e){
            throw new DAOException("Error occurred while connecting to the database");
        }
        return null;
    }
}

package it.uniroma2.dicii.ispw.sostudy.dao.student;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDBDAO extends StudentDAO {

    @Override
    public Student getStudentByEmail(String email){
        if(this.containsKey(email)){
            return this.getFromCache(email);
        }

        String sqlQuery = "SELECT name, surname, email FROM Student WHERE email = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Student stud = new Student(rs.getString("name"), rs.getString("surname"), rs.getString("email"));
                this.addToCache(rs.getString("email"), stud);
                return stud;
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while taking data from the database");
        }
        return null;
    }
}

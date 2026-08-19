package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TestDBDAO extends TestDAO {
    @Override
    public Test getTestById(int testId) throws DAOException {
        if(this.containsKey(testId)){
            return this.getFromCache(testId);
        }
        String sqlQuery = """
                            SELECT Test.name, dueDate, dueTime, duration, class, header, maxScore, type, Domanda.code 
                            FROM Test join Domanda on `Domanda`.`test` = `Test`.`code` 
                                join Class on Test.class = Class.code 
                            WHERE `Test`.`code` = ?""";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, testId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                LocalDate dueDate = rs.getDate("dueDate").toLocalDate();
                LocalTime dueTime = rs.getTime("dueTime").toLocalTime();
                Duration duration = Duration.parse(rs.getString("duration"));
                VirtualClass virtualClass = DAOFactory.getInstance().getVirtualClassDAO().getVirtualClassById(rs.getInt("class"));
                List<Question> questions = DAOFactory.getInstance().getQuestionDAO().getQuestionsByTestId(testId);
                return new Test(name, dueDate, dueTime, duration, questions, virtualClass);
            }
        }
        catch(SQLException | DAOException e){
            throw new DAOException("Error occurred while taking data from the database. " + e.getMessage());
        }
        return null;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        String sqlQuery = "INSERT INTO Test (name, dueDate, dueTime, duration, class) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, test.getName());
            ps.setDate(2, Date.valueOf(test.getDueDate()));
            ps.setTime(3, Time.valueOf(test.getDueTime()));
            ps.setString(4, test.getDuration().toString());
            ps.setInt(5, test.getVirtualClass().getClassId());
            ps.executeUpdate();

            int testId;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    testId = rs.getInt(1);
                } else {
                    throw new DAOException("Query execution failed!!!");
                }
            }

            this.addToCache(testId, test);

            DAOFactory.getInstance().getQuestionDAO().saveTestQuestion(testId, test.getQuestions());
        }
        catch (SQLException | DAOException e) {
            throw new DAOException("Error while connecting to database. " + e.getMessage());
        }
    }
}
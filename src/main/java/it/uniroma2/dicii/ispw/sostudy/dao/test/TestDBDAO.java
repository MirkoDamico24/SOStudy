package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                Test test = new Test(name, dueDate, dueTime, duration, questions, virtualClass);
                this.addToCache(testId, test);
                return test;
            }
        }
        catch(SQLException | DAOException e){
            throw new DAOException("Error occurred while taking data from the database. " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Test> getTestByClassId(int classId, VirtualClass virtualClass) throws DAOException {
        record TestRawData(int code, String name, LocalDate dueDate, LocalTime dueTime, Duration duration) {}

        List<TestRawData> rawDataList = new ArrayList<>();
        List<Integer> testIds = new ArrayList<>();

        String sqlQuery = "SELECT code, name, dueDate, dueTime, duration FROM Test WHERE class = ?";

        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int code = rs.getInt("code");
                    rawDataList.add(new TestRawData(
                            code,
                            rs.getString("name"),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getTime("dueTime").toLocalTime(),
                            Duration.parse(rs.getString("duration"))
                    ));
                    testIds.add(code);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Database error. " + e.getMessage());
        }

        if (testIds.isEmpty()) return new ArrayList<>();

        Map<Integer, List<Question>> questionsMap = DAOFactory.getInstance().getQuestionDAO().getQuestionsByTestIds(testIds);

        List<Test> finalTests = new ArrayList<>();
        for (TestRawData data : rawDataList) {
            if(this.containsKey(data.code)){
                finalTests.add(this.getFromCache(data.code));
                continue;
            }
            List<Question> testQuestions = questionsMap.getOrDefault(data.code(), new ArrayList<>());

            Test test = new Test(
                    data.name(),
                    data.dueDate(),
                    data.dueTime(),
                    data.duration(),
                    testQuestions,
                    virtualClass
            );
            this.addToCache(data.code, test);
            finalTests.add(test);
        }

        return finalTests;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        String sqlQuery = "INSERT INTO Test (name, dueDate, dueTime, duration, class) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, test.getName());
            ps.setDate(2, Date.valueOf(test.getDueDate()));
            ps.setTime(3, Time.valueOf(test.getDueTime()));
            ps.setString(4, test.getDuration().toString());
            int classID = DAOFactory.getInstance().getVirtualClassDAO().getClassID(test.getVirtualClass().getName(), test.getVirtualClass().getProf().getEmail());
            ps.setInt(5, classID);
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

    @Override
    public List<TestAttempt> getTestAttempt(Test test) throws DAOException{
        int testId = 0;
        List<TestAttempt> attempts = null;

        try{
            testId = this.getTestId(test.getName(), test.getVirtualClass().getName(), test.getVirtualClass().getProf().getEmail());
            attempts = DAOFactory.getInstance().getTestAttemptDAO().getTestAttempt(testId);
        }
        catch(DAOException e){
            throw new DAOException("Error while fetching attempts. " + e.getMessage());
        }

        return attempts;
    }
}
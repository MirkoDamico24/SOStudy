package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestDBDAO extends TestDAO {
    private List<Choice> getQuestionChoices(int questionCode) throws SQLException {
        List<Choice> choices = new ArrayList<>();
        Choice solution = null;
        String sqlQuery = "SELECT code, content, isSolution FROM OpzioniDomande join Domanda on question = `Domanda`.code WHERE Domanda.code = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, questionCode);
            ResultSet rs2 = ps.executeQuery();
            while(rs2.next()){
                Choice c = new Choice(rs2.getInt("code"), rs2.getString("content"));
                choices.add(c);
                if(rs2.getBoolean("isSolution")){solution = c;}
            }
        }
        catch(SQLException e){
            throw new SQLException(e.getMessage(), e.getCause());
        }
        choices.add(solution); //solution always in last position
        return choices;
    }

    private List<Question> getTestQuestions(ResultSet rs) throws SQLException {
        List<Question> questions = new ArrayList<>();
        while (rs.isFirst() || rs.next()) { //because of short-circuit evaluation, rs.next() executed only if !rs.isFirst()
            QuestionType qt = QuestionType.valueOf(rs.getString("type"));
            if(qt == QuestionType.OPENQUESTION) {
                OpenQuestion oq = new OpenQuestion(rs.getString("header"), rs.getInt("maxScore"));
                questions.add(oq);
            }
            else if(qt == QuestionType.CLOSEQUESTION) {
                List<Choice> choices = getQuestionChoices(rs.getInt("code"));
                Choice solution = choices.getLast();
                choices.remove(choices.getLast());      //remove solution from option list, otherwise duplicates
                questions.add(new CloseQuestion(rs.getString("header"), rs.getInt("maxScore"), choices, solution));
            }
        }
        return questions;
    }

    @Override
    public Test getTestById(int testId) throws DAOException {
        if(this.containsKey(testId)){
            return this.getFromCache(testId);
        }
        String sqlQuery = "SELECT Test.name, dueDate, dueTime, duration, class, header, maxScore, type, Domanda.code FROM Test join Domanda on `Domanda`.`test` = `Test`.`code` join Class on Test.class = Class.code WHERE `Test`.`code` = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, testId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                LocalDate dueDate = rs.getDate("dueDate").toLocalDate();
                LocalTime dueTime = rs.getTime("dueTime").toLocalTime();
                Duration duration = Duration.parse(rs.getString("duration"));
                VirtualClass virtualClass = DAOFactory.getInstance().getVirtualClassDAO().getVirtualClassById(rs.getInt("class"));
                List<Question> questions = getTestQuestions(rs);
                return new Test(name, dueDate, dueTime, duration, questions, virtualClass);
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while taking data from the database");
        }
        return null;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        String sqlQuery = "INSERT INTO Test (name, dueDate, dueTime, duration, class) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, test.getName());
            ps.setDate(2, java.sql.Date.valueOf(test.getDueDate()));
            ps.setTime(3, java.sql.Time.valueOf(test.getDueTime()));
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

            if (test.getQuestions() != null) {
                for (Question q : test.getQuestions()) {
                    saveQuestion(q, testId);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while connecting to database!!!");
        }
    }

    private void saveQuestion(Question q, int testId) throws SQLException {
        String sqlQuery = "INSERT INTO Domanda (header, maxScore, type, test) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, q.getHeader());
            ps.setInt(2, q.getMaxScore());
            String type = (q instanceof OpenQuestion) ? QuestionType.OPENQUESTION.name() : QuestionType.CLOSEQUESTION.name();
            ps.setString(3, type);
            ps.setInt(4, testId);
            ps.executeUpdate();

            int questionId;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                questionId = rs.getInt(1);
            } else {
                throw new SQLException("Query execution failed!!!");
            }

            if (q instanceof CloseQuestion cq) {
                saveChoices(cq.getChoices(), cq.getSolution(), questionId);
            }
        }
    }

    private void saveChoices(List<Choice> choices, Choice solution, int questionId) throws SQLException {
        String sqlQuery = "INSERT INTO OpzioniDomande (content, isSolution, question) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)) {
            if (choices != null) {
                ps.setInt(3, questionId);
                for (Choice c : choices) {
                    ps.setString(1, c.getContent());
                    ps.setBoolean(2, c == solution);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }
}
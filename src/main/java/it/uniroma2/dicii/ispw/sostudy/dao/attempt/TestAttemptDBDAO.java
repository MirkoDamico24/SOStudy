package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;
import it.uniroma2.dicii.ispw.sostudy.model.answerfactory.AnswerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestAttemptDBDAO extends TestAttemptDAO{
    private List<TestAttemptAnswer> getAttemptAnswers(ResultSet rs) throws SQLException {
        List<TestAttemptAnswer> answers = new ArrayList<>();
        do{
            Choice choice = null;
            String textualContent = rs.getString("textualContent");
            int integerContent = rs.getInt("integerContent");
            if(!rs.wasNull()) choice =  DAOFactory.getInstance().getChoiceDAO().getChoiceById(integerContent);
            int score = rs.getInt("score");
            int questionID = rs.getInt("question");
            Question question = DAOFactory.getInstance().getQuestionDAO().getQuestionById(questionID);
            TestAttemptAnswer answer = AnswerFactory.createAnswer(score, textualContent, choice, question);
            answers.add(answer);
        }while(rs.next());
        return answers;
    }

    @Override
    public TestAttempt getTestAttemptById(int id) throws DAOException {
        if(this.containsKey(id)){
            return this.getFromCache(id);
        }

        TestAttempt attempt = null;
        String sqlQuery = "SELECT textualContent, integerContent, score, testID, question, grade, gradingStatus, handInTime, handInDate, test, student FROM Risposte join Tentativo on attempt = testID WHERE testID = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int attemptId = rs.getInt("testID");
                int grade = rs.getInt("grade");
                TestGradingStatus gradingStatus = TestGradingStatus.valueOf(rs.getString("gradingStatus"));
                LocalTime handInTime = LocalTime.parse(rs.getString("handInTime"));
                LocalDate handInDate = LocalDate.parse(rs.getString("handInDate"));
                Test test = DAOFactory.getInstance().getTestDAO().getTestById(rs.getInt("test"));

                DAOFactory.getInstance().getQuestionDAO().getQuestionsByTestId(rs.getInt("test"));   //Question cache prefill

                Student student = DAOFactory.getInstance().getStudentDAO().getStudentByEmail(rs.getString("student"));
                List<TestAttemptAnswer> answers = getAttemptAnswers(rs);
                attempt = new TestAttempt(test, answers, student, attemptId, grade, gradingStatus, handInTime, handInDate);
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching attempt data from database.");
        }
        this.addToCache(id, attempt);
        return attempt;
    }

    private void saveAnswers(int attemptID, List<TestAttemptAnswer> answers){
        /* TODO: implement saveAnswer eliminating the id from model
        String sqlQuery = "INSERT INTO Risposte VALUES (?, ?, ?, ?)";
        for(TestAttemptAnswer answer: answers){
            try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery))
            {
                    if(answer instanceof CloseAnswer closeAnswer){
                        ps.setInt(1, closeAnswer.getContent().getChoiceID());
                    }
                    else{
                        OpenAnswer op = (OpenAnswer) answer;
                        ps.setString(1, op.getContent());
                    }
                    ps.setInt(3, answer.getScore());
                    ps.setInt(4, attemptID);
                    ps.setInt(5, answer.getQuestion().getId());
            }
            catch(SQLException e){
                throw new DAOException("Error occurred while saving attempt answers to database.");
            }
        }*/
    }

    @Override
    public void saveTestAttempt(TestAttempt testAttempt) {
        /*TODO: implement this method removing id from model
        String sqlQuery = "INSERT INTO Tentativo VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, testAttempt.getGrade());
            ps.setString(2, testAttempt.getTestGradingStatus().toString());
            ps.setTime(3, Time.valueOf(testAttempt.getHandInTime()));
            ps.setDate(4, Date.valueOf(testAttempt.getHandInDate()));
            ps.setInt(5, testAttempt.getTest().getId());
            ps.setString(6, testAttempt.getStudent().getEmail());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int attemptId = 0;
            if(rs.next()){
                attemptId =  rs.getInt(1);
            }
            saveAnswers(attemptId, testAttempt.getAnswers());
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while saving attempt data to database.");
        }
        */
    }

    @Override
    public void addAnswerToAttempt(TestAttemptAnswer answer, int testID){
       /* TODO: implement this method removing id from model
        if(!this.containsKey(testID)) throw new DAOException("Attempt with id " + testID + " cannot be updated, since it is not been loaded from database.");

        List<TestAttemptAnswer> tmpList = new ArrayList<>();
        tmpList.add(answer);
        saveAnswers(testID, tmpList);
        */
    }
}

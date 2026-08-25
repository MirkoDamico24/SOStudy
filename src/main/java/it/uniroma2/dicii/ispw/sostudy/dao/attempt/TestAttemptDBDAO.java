package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestAttemptDBDAO extends TestAttemptDAO{
   private DAOFactory factory = DAOFactory.getInstance();
   private QuestionDAO questionDAO = factory.getQuestionDAO();
   private ChoiceDAO choiceDAO = factory.getChoiceDAO();
   private TestDAO testDAO = factory.getTestDAO();

   private record ToUpdate(Answer answer, Integer questionID){}

    private Answer<?> buildAnswer(ResultSet rs) throws SQLException {
        int score = rs.getInt("score");
        Question question = questionDAO.getQuestionById(rs.getInt("question"));
        int integerContent = rs.getInt("integerContent");

        if (!rs.wasNull()) {
            Choice choice = choiceDAO.getChoiceById(integerContent);
            return new Answer<>(score, choice, question);
        } else {
            return new Answer<>(score, rs.getString("textualContent"), question);
        }
    }


    @Override
    public List<TestAttempt> getTestAttempt(int testId) throws DAOException {
        List<TestAttempt> attempts = new ArrayList<>();
        if(this.loadedAttempts.contains(testId)){
            /*
                if the id is in the Set, then the test has already tried to load its attempts,
                but it hasn't any. We're sure about that, cause the request to DAO is executed
                only if test.getTests() = null
             */
            return attempts;
        }

        String sqlQuery = """
                            SELECT testID, textualContent, integerContent, score, testID, question, grade, gradingStatus, handInTime, handInDate, test, student 
                            FROM Risposte join Tentativo on attempt = testID 
                            WHERE test = ?
                            ORDER BY testID""";

        Test test = testDAO.getTestById(testId);

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, testId);

            ResultSet rs = ps.executeQuery();
            int currentAttemptId = -1;
            List<Answer<?>> currentAnswers = null;
            Student currentStudent = null;
            int grade = 0;
            TestGradingStatus gradingStatus = null;
            LocalTime handInTime = null;
            LocalDate handInDate = null;

            while (rs.next()) {
                int attemptId = rs.getInt("testID");
                if (attemptId != currentAttemptId) {
                    if (currentAttemptId != -1) {
                        attempts.add(new TestAttempt(test, currentAnswers, currentStudent,
                                 grade, gradingStatus, handInTime, handInDate));
                    }
                    currentAttemptId = attemptId;
                    currentAnswers = new ArrayList<>();
                    currentStudent = factory.getStudentDAO().getStudentByEmail(rs.getString("student"));
                    grade = rs.getInt("grade");
                    gradingStatus = TestGradingStatus.valueOf(rs.getString("gradingStatus"));
                    handInTime = LocalTime.parse(rs.getString("handInTime"));
                    handInDate = LocalDate.parse(rs.getString("handInDate"));
                }
                currentAnswers.add(buildAnswer(rs));
            }
            if (currentAttemptId != -1) {
                attempts.add(new TestAttempt(test, currentAnswers, currentStudent,
                                             grade, gradingStatus, handInTime, handInDate));
            }
        }
        catch (SQLException e) {
        throw new DAOException("Error occurred while fetching test attempts. " + e.getMessage());
        }

        test.setTests(attempts);
        loadedAttempts.add(testId);
        return attempts;
    }


    private void saveAnswers(int attemptID, List<Answer<?>> answers, Test test){
        int questionID = 0;
        String sqlQuery = "INSERT INTO Risposte (`textualContent`, `integerContent`, `score`, `attempt`, `question`) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery))
        {
            int classId = factory.getVirtualClassDAO().getClassID(test.getVirtualClass().getName(), test.getVirtualClass().getProf().getEmail());

            Object content = null;
            ps.setInt(4, attemptID);
            for(Answer<?> answer : answers){
                questionID = questionDAO.getQuestionId(answer.getQuestion(),
                        testDAO.getTestId(test.getName(), classId));

                content = answer.getContent();

                String textToInsert = null;
                Integer choiceToInsert = null;

                if (content instanceof Choice selected) {
                    choiceToInsert = choiceDAO.getChoiceId(selected, questionID);
                } else if (content instanceof String stringContent) {
                    textToInsert = stringContent;
                }

                ps.setString(1, textToInsert);
                ps.setObject(2, choiceToInsert, java.sql.Types.INTEGER);
                ps.setInt(3, answer.getScore());
                ps.setInt(5, questionID);

                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch(SQLException | DAOException e){
            throw new DAOException("Error occurred while saving attempt answers to database. " + e.getMessage());
        }
    }

    @Override
    public void saveTestAttempt(TestAttempt testAttempt) {
        String sqlQuery = "INSERT INTO Tentativo (`grade`, `gradingStatus`, `handInTime`, `handInDate`, `test`, `student`) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, testAttempt.getGrade());
            ps.setString(2, testAttempt.getTestGradingStatus().toString());
            ps.setTime(3, Time.valueOf(testAttempt.getHandInTime()));
            ps.setDate(4, Date.valueOf(testAttempt.getHandInDate()));

            int classId = factory.getVirtualClassDAO().getClassID(testAttempt.getTest().getVirtualClass().getName(), testAttempt.getTest().getVirtualClass().getProf().getEmail());

            int testId = testDAO.getTestId(testAttempt.getTest().getName(), classId);
            System.out.println("Test id: " + testId);
            ps.setInt(5, testId);

            ps.setString(6, testAttempt.getStudent().getEmail());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int attemptId = 0;
            if(rs.next()){
                attemptId =  rs.getInt(1);
            }
            saveAnswers(attemptId, testAttempt.getAnswers(), testAttempt.getTest());
        }
        catch(SQLException | DAOException e){
            throw new DAOException("Error occurred while saving attempt data to database. " + e.getMessage());
        }
    }

    @Override
    public void updateTestAttempt(TestAttempt testAttempt) throws DAOException{
        String sqlQuery = "UPDATE Tentativo SET gradingStatus = ? WHERE test = ? AND student = ? RETURNING testID";
        int testId;
        int classId;
        int attemptId = 0;

        try{
            classId = factory.getVirtualClassDAO().getClassID(testAttempt.getTest().getVirtualClass().getName(),
                    testAttempt.getTest().getVirtualClass().getProf().getEmail());

            testId = testDAO.getTestId(testAttempt.getTest().getName(), classId);
        }
        catch(DAOException e){
            throw new DAOException("Error occurred while updating attempt data to database. " + e.getMessage());
        }

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setString(1, TestGradingStatus.FULLYGRADED.name());
            ps.setInt(2, testId);
            ps.setString(3, testAttempt.getStudent().getEmail());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                attemptId = rs.getInt(1);
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while updating attempt data to database. " + e.getMessage());
        }

        updateAnswers(testId, attemptId, testAttempt);
    }

    private void updateAnswers(int testId, int attemptId, TestAttempt testAttempt){
        String sqlQuery = "UPDATE Risposte SET score = ? WHERE attempt = ? AND question = ?";

        List<ToUpdate> toUpdate = buildQueryElement(testId, testAttempt);

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(2, attemptId);
            for(ToUpdate element : toUpdate){
                ps.setInt(1, element.answer.getScore());
                ps.setInt(3, element.questionID);
                ps.addBatch();
            }

            ps.executeBatch();
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while updating attempt data to database. " + e.getMessage());
        }
    }

    private List<ToUpdate> buildQueryElement(int testId, TestAttempt testAttempt){
        List<ToUpdate> toUpdateList = new ArrayList<>();
        Integer question = null;

        List<Question> questions = new ArrayList<>();
        for(Answer a : testAttempt.getAnswers()){
            try{
                question = questionDAO.getQuestionId(a.getQuestion(), testId);
            }
            catch(DAOException e){
                throw new DAOException("Error occurred while updating attempt data to database. " + e.getMessage());
            }

            if(question != null){
                toUpdateList.add(new ToUpdate(a, question));
            }
        }

        return toUpdateList;
    }
}

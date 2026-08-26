package it.uniroma2.dicii.ispw.sostudy.dao.question;


import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.ChoiceDTO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionDTO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionMapper;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class QuestionDBDAO extends QuestionDAO {
    private record CloseIndexes (QuestionDTO q, int index) {}
    private static final String HEADER = "header";
    private static final String SCORE = "maxScore";

    private Question buildQuestion(int id, String header, int maxScore, QuestionType type)
    {
        QuestionDTO question = null;
        switch (type) {
            case QuestionType.OPENQUESTION:
                question = new QuestionDTO(header, maxScore, QuestionType.OPENQUESTION, null, null);
                break;

            case QuestionType.CLOSEQUESTION:
                ChoiceDAO choiceDAO = DAOFactory.getInstance().getChoiceDAO();
                ChoiceDTO choices = choiceDAO.getChoicesByQuestionId(id);
                question = new QuestionDTO(header, maxScore, QuestionType.CLOSEQUESTION, choices.options(), choices.solution());
                break;

            default: throw new DAOException("Invalid question type!!!");
        }
        return QuestionMapper.dtoToQuestion(question);
    }

    @Override
    public Question getQuestionById(int id) throws DAOException {
        if(this.containsKey(id)){
            return this.getFromCache(id);
        }

        Question toRet = null;
        String sqlQuery = "SELECT header, maxScore, type FROM Domanda WHERE code ? ";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery))
        {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                QuestionType type = QuestionType.valueOf(rs.getString("type"));
                toRet = buildQuestion(id, rs.getString(HEADER), rs.getInt(SCORE), type);
            }
        }
        catch(SQLException | DAOException e){
            throw new DAOException("Error occurred while fetching question data from database. " + e.getMessage());
        }

        this.addToCache(id, toRet);
        return toRet;
    }

    @Override
    public List<Question> getQuestionsByTestId(int testID) {
        List<Question> questions = new ArrayList<>();

        String sqlQuery = "SELECT code, header, maxScore, type FROM Domanda WHERE test = ? ";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, testID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int questionID = rs.getInt("code");
                if(this.containsKey(questionID)){
                    questions.add(this.getFromCache(questionID));
                    continue;
                }
                Question question = buildQuestion(questionID, rs.getString(HEADER), rs.getInt(SCORE), QuestionType.valueOf(rs.getString("type")));
                this.addToCache(questionID, question);
                questions.add(question);

            }
        }
        catch(SQLException | DAOException e){
            throw new DAOException("Error occurred while fetching question data by test id from database. " + e.getMessage());
        }
        return questions;
    }

    @Override
    public void saveTestQuestion(int testID, List<Question> questions) throws  DAOException {
        List<CloseIndexes> completeSaving = new ArrayList<>();

        String sqlQuery = "INSERT INTO Domanda (header, maxScore, type, test) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(4, testID);
            for(Question q : questions){
                QuestionDTO dto = QuestionMapper.questionToDTO(q);

                ps.setString(1, dto.header());
                ps.setInt(2, dto.score());

                ps.setString(3, dto.type().name());
                if(dto.type() == QuestionType.CLOSEQUESTION){ completeSaving.add(new CloseIndexes(dto, questions.indexOf(q)));}

                ps.addBatch();
            }

            ps.executeBatch();

            List<Integer> questionId = new ArrayList<>();
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                questionId.add(rs.getInt(1));
            }

            for (int i = 0; i < questions.size() && i < questionId.size(); i++) {
                this.addToCache(questionId.get(i), questions.get(i));
            }

            finalizeQuestionSaving(completeSaving, questionId);
        }
        catch (SQLException e) {
            throw new DAOException("Error occurred while saving question data to database.");
        }
    }

    private void finalizeQuestionSaving(List<CloseIndexes> questions, List<Integer> questionIDs){
        List<ChoiceDTO> choices = new ArrayList<>();
        for(CloseIndexes closeIndexes : questions){
            choices.add(new ChoiceDTO(closeIndexes.q.options(), closeIndexes.q.solution(),
                        questionIDs.get(closeIndexes.index)
            ));
        }

        try{
            DAOFactory.getInstance().getChoiceDAO().saveChoices(choices);
        }
        catch(DAOException e){
            throw new DAOException("Error occurred while saving question data to database.");
        }
    }

    @Override
    public Map<Integer, List<Question>> getQuestionsByTestIds(List<Integer> testIDs) {
        Map<Integer, List<Question>> questionsByTest = new HashMap<>();

        if (testIDs == null || testIDs.isEmpty()) {
            return questionsByTest;
        }

        String placeholders = String.join(",", Collections.nCopies(testIDs.size(), "?"));

        String sqlQuery = "SELECT code, header, maxScore, type, test FROM Domanda WHERE test IN (" + placeholders + ")";

        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)) {
            for (int i = 0; i < testIDs.size(); i++) {
                ps.setInt(i + 1, testIDs.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int questionID = rs.getInt("code");
                int testID = rs.getInt("test");

                Question question;
                if (this.containsKey(questionID)) {
                    question = this.getFromCache(questionID);
                } else {
                    QuestionType type = QuestionType.valueOf(rs.getString("type"));
                    question = buildQuestion(questionID, rs.getString(HEADER), rs.getInt(SCORE), type);
                    this.addToCache(questionID, question);
                }

                questionsByTest.computeIfAbsent(testID, k -> new ArrayList<>()).add(question);
            }
        } catch (SQLException | DAOException e) {
            throw new DAOException("Error occurred while fetching questions for multiple tests. " + e.getMessage());
        }

        return questionsByTest;
    }

    @Override
    public Integer getQuestionId(Question question, int testID){
        Integer questionID = null;
        String sqlQuery = "SELECT code FROM Domanda WHERE header = ? and test = ?";

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setString(1, question.getHeader());
            ps.setInt(2, testID);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            if(rs.next()){
                questionID = rs.getInt("code");
            }
        }
        catch(SQLException | DAOException e){
            throw new DAOException("Error occurred while getting question id from database. " + e.getMessage());
        }

        return questionID;
    }

}

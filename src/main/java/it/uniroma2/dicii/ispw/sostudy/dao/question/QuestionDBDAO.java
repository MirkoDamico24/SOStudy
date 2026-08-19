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
import java.util.ArrayList;
import java.util.List;

public class QuestionDBDAO extends QuestionDAO {
    private record CloseIndexes (QuestionDTO q, int index) {}

    private Question buildQuestion(int id, String header, int maxScore, QuestionType type)
    {
        QuestionDTO question = null;
        if(type == QuestionType.OPENQUESTION){
            question = new QuestionDTO(header, maxScore, QuestionType.OPENQUESTION, null, null);
        }
        else if(type == QuestionType.CLOSEQUESTION){
            ChoiceDAO choiceDAO = DAOFactory.getInstance().getChoiceDAO();
            ChoiceDTO choices = choiceDAO.getChoicesByQuestionId(id);
            question = new QuestionDTO(header, maxScore, QuestionType.CLOSEQUESTION, choices.options(), choices.solution());
        }
        else throw new DAOException("Invalid question type!!!");
        return QuestionMapper.DTOToQuestion(question);
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
                toRet = buildQuestion(id, rs.getString("header"), rs.getInt("maxScore"), type);
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
                Question question = buildQuestion(questionID, rs.getString("header"), rs.getInt("maxScore"), QuestionType.valueOf(rs.getString("type")));
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
            for(Question q : questions){
                QuestionDTO dto = QuestionMapper.questionToDTO(q);

                ps.setString(1, dto.header());
                ps.setInt(2, dto.score());

                ps.setString(3, dto.type().name());
                if(dto.type() == QuestionType.CLOSEQUESTION){ completeSaving.add(new CloseIndexes(dto, questions.indexOf(q)));}

                ps.setInt(4, testID);
                ps.addBatch();
            }

            ps.executeBatch();

            List<Integer> questionId = new ArrayList<>();
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                questionId.add(rs.getInt(1));
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
}

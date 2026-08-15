package it.uniroma2.dicii.ispw.sostudy.dao.question;


import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDBDAO extends QuestionDAO {
    private Question buildQuestion(int id, String header, int maxScore, QuestionType type)
    {
        Question toRet = null;
        if(type == QuestionType.OPENQUESTION){
            toRet = new OpenQuestion(header, maxScore);
        }
        else{
            ChoiceDAO choiceDAO = DAOFactory.getInstance().getChoiceDAO();
            List<Choice> choices = choiceDAO.getChoicesByQuestionId(id);
            Choice solution = choiceDAO.getQuestionSolution(id);
            toRet = new CloseQuestion(header, maxScore, choices, solution);
        }
        return toRet;
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
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching question data from database.");
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
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching question data by test id from database.");
        }
        return questions;
    }
}

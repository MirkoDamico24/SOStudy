package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChoiceDBDAO extends ChoiceDAO {
    @Override
    public Choice getChoiceById(int id) throws DAOException{
        if(this.containsKey(id)){
            return this.getFromCache(id);
        }

        Choice choice = null;
        String sqlQuery = "SELECT content FROM OpzioniDomande WHERE code = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery))
        {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                choice = new Choice(id, rs.getString("content"));
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching choice from database.");
        }
        this.addToCache(id, choice);
        return choice;
    }

    @Override
    public List<Choice> getChoicesByQuestionId(int questionID) throws DAOException{
        List<Choice> choices = new ArrayList<>();
        String sqlQuery = "SELECT code, content FROM OpzioniDomande WHERE question = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery))
        {
            ps.setInt(1, questionID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int choiceID = rs.getInt("code");
                if(this.containsKey(choiceID)){
                    choices.add(this.getFromCache(choiceID));
                    continue;
                }
                Choice choice = new Choice(choiceID, rs.getString("content"));
                this.addToCache(choiceID, choice);
                choices.add(choice);
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching choices from database.");
        }
        return choices;
    }

    @Override
    public Choice getQuestionSolution(int questionID) throws DAOException{
        String sqlQuery = "SELECT code, content FROM OpzioniDomande WHERE question = ? and isSolution = true";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery))
        {
            ps.setInt(1, questionID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int choiceID = rs.getInt("code");
                if(this.containsKey(choiceID)) return this.getFromCache(choiceID);
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching solution from database.");
        }
        return null;        //if choice is not in cache, then the solution is not for an already loaded question
    }
}

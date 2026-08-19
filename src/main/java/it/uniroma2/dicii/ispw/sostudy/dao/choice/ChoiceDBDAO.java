package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.ChoiceDTO;
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
    public ChoiceDTO getChoicesByQuestionId(int questionID) throws DAOException{
        ChoiceDTO choices = null;
        List<Choice> choicesList = new ArrayList<>();
        Choice solution = null;
        String sqlQuery = "SELECT code, content, isSolution FROM OpzioniDomande WHERE question = ?";

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery))
        {
            ps.setInt(1, questionID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int choiceID = rs.getInt("code");
                Choice choice = new Choice(choiceID, rs.getString("content"));
                choicesList.add(choice);
                if(rs.getBoolean("isSolution")) solution = choice;
            }
            choices = new ChoiceDTO(choicesList, solution, questionID);
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching choices from database.");
        }
        return choices;
    }

    @Override
    public void saveChoices(List<ChoiceDTO> choices){
        String sqlQuery = "INSERT INTO OpzioniDomande(content, isSolution, question) VALUES (?, ?, ?)";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            for(ChoiceDTO dto : choices){
                for(Choice c : dto.options()){
                    ps.setString(1, c.getContent());
                    if(c == dto.solution()) ps.setBoolean(2, true);
                    else ps.setBoolean(2, false);
                    ps.setInt(3, dto.questionID());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while saving choices on database.");
        }
    }
}

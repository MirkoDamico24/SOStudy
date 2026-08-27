package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.ChoiceDTO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                choice = new Choice(rs.getString("content"));
                this.addToCache(id, choice);
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching choice from database.");
        }
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
                int choiceId = rs.getInt("code");

                Choice choice;
                if(this.containsKey(choiceId)){
                    choice = this.getFromCache(choiceId);
                }
                else{
                    choice = new Choice(rs.getString("content"));
                    this.addToCache(choiceId, choice);
                }

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
    public void saveChoices(List<ChoiceDTO> choices) {
        String sqlQuery = "INSERT INTO OpzioniDomande(content, isSolution, question) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            for (ChoiceDTO dto : choices) {
                ps.setInt(3, dto.questionID());
                for (Choice c : dto.options()) {
                    ps.setString(1, c.getContent());
                    ps.setBoolean(2, c.equals(dto.solution()));
                    ps.addBatch();
                }
            }
            ps.executeBatch();

            ResultSet rs = ps.getGeneratedKeys();
            List<Integer> id = new ArrayList<>();
            while (rs.next()) {
                id.add(rs.getInt(1));
            }

            int index = 0;
            for (ChoiceDTO dto : choices) {
                for (Choice c : dto.options()) {
                    if (index < id.size()) {
                        Integer generatedId = id.get(index);
                        this.addToCache(generatedId, c);
                        index++;
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error occurred while saving choices on database.");
        }
    }

    @Override
    /**
     * The choice's id is not searched in cache, because the choice cannot be
     * uniquely identified by itself. It requires also the question to which it is attached,
     * but that would require the presence of a reference to a Question object that is not
     * required by the domain. We chose to leave the model completly independt from
     * persistency needs and pay a little overhead for the query.
     */
    public int getChoiceId(Choice choice, int questionId) throws DAOException{
        int choiceId = 0;
        String sqlQuery = "SELECT code FROM OpzioniDomande WHERE content = ? AND question = ?";

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setString(1, choice.getContent());
            ps.setInt(2, questionId);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            if(rs.next()){
                choiceId = rs.getInt("code");
            }
        }
        catch(SQLException e){
            throw new DAOException("Error occurred while fetching choice from database.");
        }

        return choiceId;
    }
}
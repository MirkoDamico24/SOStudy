package it.uniroma2.dicii.ispw.sostudy.dao.message;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Message;
import it.uniroma2.dicii.ispw.sostudy.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDBDAO extends MessageDAO {
    @Override
    public void save(List<Message> message) throws DAOException {
        String sqlQuery = "INSERT INTO Messaggi (message, sender, recipient) VALUES (?, ?, ?)";

        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            for (Message m : message) {
                ps.setString(1, m.getMessage());
                if(m.getSender() != null) ps.setString(2, m.getSender().getEmail());
                else ps.setString(2, null);
                ps.setString(3, m.getRecipient().getEmail());
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Message> getUserMessages(String userEmail) throws ControllerException{
        List<Message> messages = null;



        String sqlQuery = """
                        SELECT message, sender
                        FROM Messaggi 
                        WHERE recipient = ?""";

        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            messages = buildMessages(rs, userEmail);
        }
        catch(SQLException e){
            throw new ControllerException("Errore nell'inserimento dei destinatari: " + e.getMessage());
        }

        return messages;
    }

    private List<Message> buildMessages(ResultSet rs, String userEmail) throws SQLException {
        User recipient = getUser(userEmail);
        List<Message> messages = new ArrayList<>();
        Message msg;

        while (rs.next()) {
            String message = rs.getString("message");
            String senderEmail = rs.getString("sender");
            if(senderEmail != null){
                User sender = getUser(userEmail);
                msg = new Message(message, sender, recipient);
            }
            else msg = new Message(message, recipient);
            messages.add(msg);
        }
        return messages;
    }

    private User getUser(String userEmail) throws ControllerException{
        User user = null;
        user = DAOFactory.getInstance().getStudentDAO().getStudentByEmail(userEmail);
        if(user == null){
            user = DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail(userEmail);
        }
        return user;
    }
}

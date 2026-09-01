package it.uniroma2.dicii.ispw.sostudy.dao.message;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnection;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Message;
import it.uniroma2.dicii.ispw.sostudy.model.MessageType;
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
        String sqlQuery = "INSERT INTO Messaggi (message, sender, recipient, type, viewd) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            for (Message m : message) {
                ps.setString(1, m.getMessage());
                if(m.getSender() != null) ps.setString(2, m.getSender().getEmail());
                else ps.setString(2, null);
                ps.setString(3, m.getRecipient().getEmail());
                ps.setString(4, m.getType().toString());
                ps.setBoolean(5, m.isRead());
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Message> getUserMessages(String userEmail) throws DAOException{
        List<Message> messages = null;
        String sqlQuery = """
                        SELECT message, sender, type, viewd
                        FROM Messaggi 
                        WHERE recipient = ? AND viewd = false
                        ORDER BY messageid desc""";

        try(PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQuery)){
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            messages = buildMessages(rs, userEmail);
        }
        catch(SQLException e){
            throw new DAOException("Errore nell'inserimento dei destinatari: " + e.getMessage());
        }

        return messages;
    }

    private List<Message> buildMessages(ResultSet rs, String userEmail) throws SQLException {
        User recipient = getUser(userEmail);
        List<Message> messages = new ArrayList<>();
        Message msg;

        while (rs.next()) {
            boolean read = rs.getBoolean("viewd");
            if(read) continue;
            String message = rs.getString("message");
            String senderEmail = rs.getString("sender");
            MessageType type = MessageType.valueOf(rs.getString("type"));
            if(senderEmail != null){
                User sender = getUser(senderEmail);
                msg = new Message(message, sender, recipient, type);
            }
            else msg = new Message(message, recipient, type);
            msg.setRead(false);
            messages.add(msg);
        }

        recipient.setMessages(messages);
        return messages;
    }

    private User getUser(String userEmail){
        User user = null;
        user = DAOFactory.getInstance().getStudentDAO().getStudentByEmail(userEmail);
        if(user == null){
            user = DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail(userEmail);
        }
        return user;
    }
}

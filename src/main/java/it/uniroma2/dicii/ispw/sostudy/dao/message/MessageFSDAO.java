package it.uniroma2.dicii.ispw.sostudy.dao.message;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Message;
import it.uniroma2.dicii.ispw.sostudy.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageFSDAO extends MessageDAO {

    private static final String FILE_PATH = "data/Message.JSON";

    @Override
    public void save(List<Message> message) throws DAOException {
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);

            for (Message m : message) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", m.getMessage());
                if (m.getSender() != null) {
                    jsonObject.put("sender", m.getSender().getEmail());
                }
                jsonObject.put("recipient", m.getRecipient().getEmail());
                jsonArray.put(jsonObject);
            }

            JSONHelper.writeJsonFile(FILE_PATH, jsonArray);
        } catch (IOException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Message> getUserMessages(String userEmail) throws ControllerException {
        List<Message> messages = new ArrayList<>();

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            User recipient = getUser(userEmail);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.has("recipient") && jsonObject.getString("recipient").equals(userEmail)) {
                    String content = jsonObject.getString("content");
                    Message msg;

                    if (jsonObject.has("sender") && !jsonObject.isNull("sender")) {
                        String senderEmail = jsonObject.getString("sender");
                        User sender = getUser(senderEmail);
                        msg = new Message(content, sender, recipient);
                    } else {
                        msg = new Message(content, recipient);
                    }
                    messages.add(msg);
                }
            }
            recipient.setMessages(messages);
        } catch (IOException e) {
            throw new ControllerException("Error reading from file: " + e.getMessage());
        }

        return messages;
    }

    private User getUser(String userEmail) throws ControllerException {
        User user = null;
        user = DAOFactory.getInstance().getStudentDAO().getStudentByEmail(userEmail);
        if (user == null) {
            user = DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail(userEmail);
        }
        return user;
    }
}
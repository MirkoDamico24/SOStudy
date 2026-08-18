package it.uniroma2.dicii.ispw.sostudy.dao.message;

import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Message;

import java.util.List;

public class MessageDemoDAO extends MessageDAO {
    @Override
    public void save(List<Message> message) throws DAOException {

    }

    @Override
    public List<Message> getUserMessages(String userEmail) throws ControllerException {
        return null;
    }
}

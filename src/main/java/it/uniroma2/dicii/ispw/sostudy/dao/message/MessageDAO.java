package it.uniroma2.dicii.ispw.sostudy.dao.message;


/*
    no need to cache, because it will be attached to a user,
    and the message will live in user's cache
*/

import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.Message;

import java.util.List;

public abstract class MessageDAO {
    public abstract void save(List<Message> message) throws ControllerException;
    public abstract List<Message> getUserMessages(String userEmail) throws ControllerException;
}

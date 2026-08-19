package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.MessageBean;
import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageDAO;
import it.uniroma2.dicii.ispw.sostudy.eng.observer.MessageObserver;
import it.uniroma2.dicii.ispw.sostudy.eng.observer.MessageSubject;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.util.ArrayList;
import java.util.List;

public class NotificationController {
    private DAOFactory factory = DAOFactory.getInstance();
    private MessageDAO messageDAO = factory.getMessageDAO();

    public void sendNewTestNotification(VirtualClass cls, Test newTest) throws ControllerException {
        String message = "Nuovo test assegnato: " + newTest.getVirtualClass().getName() + ", " + newTest.getName() + ", " + newTest.getDueDate();

        /*
            This can be optimized by implementing broadcast msgs,
            so that only one new Message needs to be instanced
            instead of one instance per student
        */
        List<Message> newMsg = new ArrayList<>();
        for (Student student : cls.getStudents()) {
            Message msg = new Message(message, student);
            newMsg.add(msg);
            student.addMessage(msg);
        }

        messageDAO.save(newMsg);       //message saved in the persistency layer
    }

    public List<MessageBean> fetchUserNotifications(UserBean ub, SessionBean session) throws ControllerException {
        List<Message> fetched = null;

        try {
            fetched = messageDAO.getUserMessages(ub.getEmail());
        } catch (DAOException e) {
            throw new ControllerException("Errore durante il caricamento dei messaggi.");
        }

        return toMessageBean(fetched);
    }

    private List<MessageBean> toMessageBean(List<Message> fetched) {
        List<MessageBean> messageBeans = new ArrayList<>();
        MessageBean bean = null;

        for (Message msg : fetched) {
            if (msg.getSender() != null)
                bean = new MessageBean(msg.getMessage(), msg.getRecipient().getEmail(), msg.getSender().getEmail());
            else bean = new MessageBean(msg.getMessage(), msg.getRecipient().getEmail());
            messageBeans.add(bean);
        }
        return messageBeans;
    }

    public void registerAsNotificationObserver(MessageObserver obs, SessionBean sessionBean) {
        MessageSubject subject = getCurrentSubject(sessionBean);
        subject.attach(obs);
    }

    public void detachFromObserved(MessageObserver obs, SessionBean sessionBean) {
        MessageSubject subject = getCurrentSubject(sessionBean);
        subject.detach(obs);
    }

    private MessageSubject getCurrentSubject(SessionBean sessionBean){
        MessageSubject subject = null;
        Session session = SessionManager.getInstance().getSession(sessionBean.getSessionID());
        if (sessionBean.getCurrentRole() == UserRole.STUDENT) {
            subject = session.getCurrentStudent();
        } else subject = session.getCurrentProfessor();

        return subject;
    }
}

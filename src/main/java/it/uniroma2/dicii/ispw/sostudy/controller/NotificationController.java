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
            Message msg = new Message(message, student, MessageType.NEWTEST);
            newMsg.add(msg);
            student.addMessage(msg);
        }

        messageDAO.save(newMsg);       //message saved in the persistency layer
    }

    public void sendNewEvaluationNotification(Test test, TestAttempt evaluatedTest) throws ControllerException {
        String message = "Il test '" + test.getName() + "' svolto in data " + evaluatedTest.getHandInDate() + " ha ricevuto la valutazione: " + evaluatedTest.getGrade();
        Student recipient = evaluatedTest.getStudent();
        Message msg = new Message(message, recipient, MessageType.GRADENOTIFICATION);
        recipient.addMessage(msg);

        List<Message> newMsg = new ArrayList<>();
        newMsg.add(msg);
        messageDAO.save(newMsg);
    }

    public void sendRevisionNotification(Professor recipient, TestAttempt attempt) throws ControllerException {
        String message = "Lo studente: " + attempt.getStudent().getEmail() + " ha fatto richiesta di revisione per il test '"
                + attempt.getTest().getName() + "' nella classe: " + attempt.getTest().getVirtualClass().getName();

        Message msg = new Message(message, recipient, MessageType.REVIEWNOTIFICATION);
        recipient.addMessage(msg);

        List<Message> newMsg = new ArrayList<>();
        newMsg.add(msg);
        messageDAO.save(newMsg);
    }

    public List<MessageBean> fetchUserNotifications(UserBean ub, SessionBean currentSession) throws ControllerException {
        List<Message> fetched = null;

        Session session = SessionManager.getInstance().getSession(currentSession.getSessionID());

        switch(currentSession.getCurrentRole()){
            case STUDENT ->{
                Student student = session.getCurrentStudent();
                fetched = student.getMessages();
            }

            case PROFESSOR ->{
                Professor prof = session.getCurrentProfessor();
                fetched = prof.getMessages();
            }
        }

        if(fetched == null || fetched.isEmpty()){
            try {
                fetched = messageDAO.getUserMessages(ub.getEmail());
            } catch (DAOException e) {
                throw new ControllerException("Errore durante il caricamento dei messaggi.");
            }
        }
        return toMessageBean(fetched);
    }

    private List<MessageBean> toMessageBean(List<Message> fetched) {
        List<MessageBean> messageBeans = new ArrayList<>();
        MessageBean bean = null;

        for (Message msg : fetched) {
            if (msg.getSender() != null)
                bean = new MessageBean(msg.getMessage(), msg.getRecipient().getEmail(), msg.getSender().getEmail(), msg.getType(), msg.isRead());
            else bean = new MessageBean(msg.getMessage(), msg.getRecipient().getEmail(), msg.getType(), msg.isRead());
            msg.setRead(true);
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

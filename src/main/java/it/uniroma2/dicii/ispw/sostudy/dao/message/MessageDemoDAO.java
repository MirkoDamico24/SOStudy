package it.uniroma2.dicii.ispw.sostudy.dao.message;

import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Message;
import it.uniroma2.dicii.ispw.sostudy.model.MessageType;
import it.uniroma2.dicii.ispw.sostudy.model.User;

import java.util.ArrayList;
import java.util.List;

public class MessageDemoDAO extends MessageDAO {
    private List<Message> messages = new ArrayList<>();

    @Override
    public void save(List<Message> message) throws DAOException {
        //no need to save. Demo version
    }

    private void populateFakeMessages() {
        User student = DAOFactory.getInstance().getStudentDAO().getStudentByEmail("giuseppe.bianchi@gmail.com");
        User professor = DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail("mario.rossi@gmail.com");

        Message first = new Message("Nuovo test pubblicato: Parziale SQL", professor, student, MessageType.NEWTEST);
        Message second = new Message("Richiesta chiarimento su Parziale SQL", student, professor, MessageType.REVIEWNOTIFICATION);

        messages.add(first);
        messages.add(second);

        student.addMessage(messages.getFirst());
        professor.addMessage(messages.getLast());
    }

    @Override
    public List<Message> getUserMessages(String userEmail) throws ControllerException {
        List<Message> fetched = new ArrayList<>();
        if(messages == null || messages.isEmpty()) populateFakeMessages();

        for (Message message : messages) {
            if (message.getRecipient().getEmail().equals(userEmail)) fetched.add(message);
        }

        User user = DAOFactory.getInstance().getStudentDAO().getStudentByEmail(userEmail);
        if(user == null) user =  DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail(userEmail);

        return user.getMessages();
    }
}

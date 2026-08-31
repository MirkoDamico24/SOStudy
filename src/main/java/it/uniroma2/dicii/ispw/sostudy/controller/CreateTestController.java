package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionMapper;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CreateTestController {
    private DAOFactory factory = DAOFactory.getInstance();
    private VirtualClassDAO classDAO = factory.getVirtualClassDAO();

    private VirtualClass getClass(int sessionID, String virtualClass) throws ControllerException {
        List<VirtualClass> vcls = null;

        Session s = SessionManager.getInstance().getSession(sessionID);
        try {
            vcls = classDAO.getClassesByProfessor(s.getCurrentProfessor().getEmail());
        }
        catch (DAOException e) {
            throw new ControllerException(e.getMessage());
        }

        for(VirtualClass vClass : vcls){
            if(vClass.getName().equals(virtualClass)){
                return vClass;
            }
        }
        return null;
    }

    public void createTest(int sessionID, TestBean test) throws ControllerException{
        TestDAO td = DAOFactory.getInstance().getTestDAO();

        VirtualClass cls = getClass(sessionID, test.getVirtualClass());
        if(cls == null){
            throw new ControllerException("Class not found");
        }

        List<Question> questionList = getQuestions(test.getQuestions());

        Test newTest = new Test(test.getName(), test.getDueDate(), test.getDueTime(), test.getDuration(), questionList, cls);
        try {
            td.saveTest(newTest);
        }
        catch (DAOException e) {
            e.printStackTrace();
            throw new ControllerException("Errore durante il salvataggio del test", e);
        }
        cls.addTest(newTest);

        NotificationController msgctrl = new NotificationController();
        try {
            msgctrl.sendNewTestNotification(cls, newTest);
        }
        catch(ControllerException e){
            throw new ControllerException(e.getMessage());
        }
    }

    private List<Question> getQuestions(List<QuestionBean> questions) {
        List<Question> questionList = new ArrayList<>();
        for(QuestionBean question : questions){
            questionList.add(QuestionMapper.beanToQuestion(question));
        }
        return questionList;
    }


    public List<VirtualClassBean> getProfessorClasses(String profEmail) throws ControllerException {
        List<VirtualClassBean> classBean = new ArrayList<>();
        List<VirtualClass> classes = null;

        try {
            classes = classDAO.getClassesByProfessor(profEmail);
        }
        catch(DAOException e) {
            throw new ControllerException(e.getMessage());
        }

        for(VirtualClass vClass : classes){
            VirtualClassBean bean = new VirtualClassBean(vClass.getName());
            classBean.add(bean);
        }
        return classBean;
    }

    public void validateDueDate(TestBean test) throws ControllerException {
        LocalDateTime toCheck = LocalDateTime.of(test.getDueDate(), test.getDueTime());
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        if(!toCheck.isAfter(now)) throw new  ControllerException("La data e l'ora di consegna devono essere successivi a quelli attuali");
    }
}

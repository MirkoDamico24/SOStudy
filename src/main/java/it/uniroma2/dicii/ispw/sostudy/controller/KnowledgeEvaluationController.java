package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionMapper;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeEvaluationController {
    private DAOFactory factory = DAOFactory.getInstance();
    private VirtualClassDAO classDAO = factory.getVirtualClassDAO();

    public List<QuestionBean> loadRequiredTest(SessionBean session, TestBean testBean, VirtualClassBean virtualClassBean) {
        List<VirtualClass> vcls = null;
        VirtualClass selectedClass = null;
        Test toTake = null;

        Session currentSession = SessionManager.getInstance().getSession(session.getSessionID());

        try {
            switch(currentSession.getRole()){
                case PROFESSOR -> vcls = classDAO.getClassesByProfessor(currentSession.getCurrentProfessor().getEmail());
                case STUDENT -> vcls = classDAO.getClassesByStudent(currentSession.getCurrentStudent().getEmail());
            }
        }
        catch(DAOException e){
            throw new ControllerException("Errore durante la ricerca della classe");
        }

        for(VirtualClass virtualClass : vcls){
            if(virtualClass.getName().equals(virtualClassBean.getClassName())){
                selectedClass = virtualClass;
                break;
            }
        }

        //extract test from class' test list
        for(Test test : selectedClass.getAvailableTests()){
            if(test.getName().equals(testBean.getName())){
                toTake = test;
                break;
            }
        }

        return questionToBean(toTake.getQuestions());
    }

    private List<QuestionBean> questionToBean(List<Question> questions){
        List<QuestionBean> questionBeans = new ArrayList<>();
        for(Question question : questions){
            questionBeans.add(QuestionMapper.questionToBean(question));
        }
        return questionBeans;
    }
}

package it.uniroma2.dicii.ispw.sostudy.controller;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
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

    /*public List<QuestionBean> loadRequiredTest(TestBean testBean, VirtualClassBean virtualClassBean) {
        VirtualClass vcls = null;
        Test toTake = null;
        try {
            //TODO: carica anche test classe insieme al caricamento della classe stessa
            vcls = classDAO.getVirtualClass(virtualClassBean.getClassName(), virtualClassBean.getProfessor().getEmail());
        }
        catch(DAOException e){
            throw new ControllerException("La classe indicata non esiste nel sistema");
        }

        //extract test from class' test list
        for(Test test : vcls.getAvailableTests()){
            if(test.getName().equals(testBean.getName())){
                toTake = test;
                break;
            }
        }

        return questionToBean(toTake.getQuestions());
    }*/

    private List<QuestionBean> questionToBean(List<Question> questions){
        List<QuestionBean> questionBeans = new ArrayList<>();
        for(Question question : questions){
            questionBeans.add(QuestionMapper.questionToBean(question));
        }
        return questionBeans;
    }
}

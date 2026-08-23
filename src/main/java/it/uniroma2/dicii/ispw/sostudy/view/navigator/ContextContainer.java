package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;

import java.util.ArrayList;
import java.util.List;

public class ContextContainer {
    private SessionBean session;
    private TestBean test;
    private List<QuestionBean> questions;

    public void setSession(SessionBean session) {
        this.session = session;
    }

    public SessionBean getSession() {
        return session;
    }

    public void setTest(TestBean test) {
        this.test = test;
    }

    public TestBean getTest() {
        return test;
    }

    public void setQuestions(QuestionBean questions) {
        if(this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(questions);
    }

    public void setQuestions(List<QuestionBean> questions) {
        this.questions = questions;
    }

    public List<QuestionBean> getQuestions() {
        return questions;
    }

    public void remove(QuestionBean question) {
        questions.remove(question);
    }
}

package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.bean.*;

import java.util.ArrayList;
import java.util.List;

public class ContextContainer {
    private SessionBean session;
    private TestBean test;
    private VirtualClassBean currentClass;
    private List<QuestionBean> questions;
    private List<AttemptBean> attempts;
    private AttemptBean currentSelectedAttempt;
    private int currentQuestionIndex = -1;

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

    public List<AttemptBean> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<AttemptBean> attempts) {
        this.attempts = attempts;
    }

    public AttemptBean getCurrentSelectedAttempt() {
        return currentSelectedAttempt;
    }

    public void setCurrentSelectedAttempt(AttemptBean currentSelectedAttempt) {
        this.currentSelectedAttempt = currentSelectedAttempt;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public void setCurrentClass (VirtualClassBean currentClass) {
        this.currentClass = currentClass;
    }

    public VirtualClassBean getCurrentClass() {
        return currentClass;
    }
}

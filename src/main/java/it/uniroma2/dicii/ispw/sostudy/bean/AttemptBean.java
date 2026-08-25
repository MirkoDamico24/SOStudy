package it.uniroma2.dicii.ispw.sostudy.bean;

import java.util.List;

public class AttemptBean {
    private List<QuestionBean> questions;
    private List<AnswerBean> answers;
    private StudentBean student;

    public AttemptBean(List<QuestionBean> questions, List<AnswerBean> answers, StudentBean student) {
        this.questions = questions;
        this.answers = answers;
        this.student = student;
    }

    public List<QuestionBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionBean> questions) {
        this.questions = questions;
    }

    public List<AnswerBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerBean> answers) {
        this.answers = answers;
    }

    public StudentBean getStudent() {
        return student;
    }

    public void setStudent(StudentBean student) {
        this.student = student;
    }
}

package it.uniroma2.dicii.ispw.sostudy.bean;

import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;

import java.util.List;

public class QuestionBean {
    private String header;
    private int maxScore;
    private QuestionType questionType;
    private List<String> options;
    private int solution;

    public QuestionBean(String header, int maxScore){
        this.header = header;
        this.maxScore = maxScore;
        this.questionType = QuestionType.OPENQUESTION;
    }

    public QuestionBean(String header, int maxScore, List<String> options, int solution){
        this.header = header;
        this.maxScore = maxScore;
        this.options = options;
        this.solution = solution;
        this.questionType = QuestionType.CLOSEQUESTION;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isOpenQuestion() {
        return questionType == QuestionType.OPENQUESTION;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }

}

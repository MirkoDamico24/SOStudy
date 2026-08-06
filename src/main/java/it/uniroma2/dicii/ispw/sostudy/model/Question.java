package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.List;

public abstract class Question <A extends Answer<?>> {
    private String header;
    private int maxScore;
    private List<A> answers;

    public Question(String header, int maxScore) {
        this.header = header;
        this.maxScore = maxScore;
    }

    public abstract void evaluate(A answer) throws Exception;
    public abstract Container getContent();

    public int getMaxScore()                  { return this.maxScore; }
    public String getHeader()                 { return this.header;}
    public void setHeader(String header)    { this.header = this.header; }
    public void setMaxScore(int maxScore)     { this.maxScore = maxScore; }
    public void addAnswer(A answer){ this.answers.add(answer); }

    public List<A> getNextAnswer() {
       //TODO: when Session concept introduced, implment
        System.out.println("getNextAnswer da implementare quando introduci concetto di Session!!!!");
        return this.answers;
    }

}

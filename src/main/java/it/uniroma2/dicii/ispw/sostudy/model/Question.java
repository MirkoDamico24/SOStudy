package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;


public abstract class Question{
    private String header;
    private int maxScore;

    protected Question(String header, int maxScore) {
        this.header = header;
        this.maxScore = maxScore;
    }

    public abstract void evaluate(TestAttemptAnswer answer) throws ModelException;
    public abstract Question copy();

    public int getMaxScore()                  { return this.maxScore; }
    public String getHeader()                 { return this.header; }
    public void setMaxScore(int maxScore)     { this.maxScore = maxScore; }
}

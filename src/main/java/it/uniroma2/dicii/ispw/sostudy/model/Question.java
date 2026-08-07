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

    public int getMaxScore()                  { return this.maxScore; }
    public String getHeader()                 { return this.header;}
    public void setHeader(String header)      { this.header = this.header; }
    public void setMaxScore(int maxScore)     { this.maxScore = maxScore; }
    public void addAnswer(A answer)           { this.answers.add(answer); }

    public A getNextAnswer(A answer) throws Exception{
       if(answer == null) throw new Exception();    //TODO: implementa eccezioni correttamente

       int currentPosition = this.answers.indexOf(answer);
       if(currentPosition != -1 && currentPosition + 1 < this.answers.size())  return answers.get(currentPosition+1);
       else throw new Exception();  //TODO: implementa eccezioni correttamente
    }

}

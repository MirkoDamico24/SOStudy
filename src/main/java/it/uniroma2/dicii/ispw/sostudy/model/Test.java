package it.uniroma2.dicii.ispw.sostudy.model;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public class Test {
    private String name;
    private Date dueDate;
    private Duration duration;
    private int maxScore;

    private List<Question> questions;

    public Test(String name, Date dueDate, Duration duration, int maxScore) {
        this.name = name;
        this.dueDate = dueDate;
        this.duration = duration;
        this.maxScore = maxScore;
    }

    public void addQuestion(OpenQuestion question) {
        Question q = new OpenQuestion(question.getHeader(), question.getMaxScore());
        this.questions.add(q);
    }

    public void addQuestion(CloseQuestion question) {
        Question q = new CloseQuestion(question.getHeader(), question.getMaxScore());
        this.questions.add(q);
    }

    public void deleteQuestion(Question question) {
        this.questions.remove(question);
    }

    public List<Question> getQuestions() {return this.questions;}

    public Question getNextQuestion(Question current) throws Exception{
        if(current == null) throw new Exception(); //TODO: implementa eccezioni correttamente

        int currentPosition = this.questions.indexOf(current);
        if(currentPosition != -1 &&  currentPosition + 1 < this.questions.size())  return questions.get(currentPosition+1);
        else throw new Exception(); //TODO: implementa eccezioni correttamente
    }

    public String getName() {return this.name;}
    public Date getDueDate() {return this.dueDate;}
    public Duration getDuration() {return this.duration;}
    public int getMaxScore() {return this.maxScore;}
    public void setName(String name) {this.name = name;}
    public void setDueDate(Date dueDate) {this.dueDate = dueDate;}
    public void setDuration(Duration duration) {this.duration = duration;}
    public void setMaxScore(int maxScore) {this.maxScore = maxScore;}

}

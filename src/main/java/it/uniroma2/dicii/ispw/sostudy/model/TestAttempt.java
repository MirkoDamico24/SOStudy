package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;

import java.util.ArrayList;
import java.util.List;

public class TestAttempt{
    private int grade;

    private List<Answer<?>> answers;
    private Student student;

    public TestAttempt(List<Answer<?>> answers, Student student) {
        List<Answer<?>> tmpAnswers = new ArrayList<>();
        for(Answer<?> a : answers){
            tmpAnswers.add(a.copy());
        }
        this.answers = tmpAnswers;
        this.student = student;
    }

    public TestAttempt(Student student) {
        this.student = student;
    }

    public void addAnswer(Answer<?> a){
        if(this.answers == null) {
            this.answers = new ArrayList<>();
        }
        Answer<?> tmpAnswer = a.copy();
        this.answers.add(tmpAnswer);
    }

    public Answer<?> getNextAnswer(Answer<?> a) throws ModelException{
         if( a == null ) throw new ModelException("[Class: TestAttempt] There is no next ANSWER for a null object");

        int currentPosition;
        currentPosition = answers.indexOf(a);
        if( currentPosition == -1 || currentPosition >= this.answers.size()) throw new ModelException("[Class: TestAttempt] Trying to access outside of TestAttempt answer's bound");

        return answers.get(currentPosition + 1);
    }

    public void computeGrade(){
        int totalScore = 0;

        for(Answer<?> a : this.answers){
            totalScore += a.getScore();
        }

        this.grade = totalScore;
    }

    public List<Answer<?>> getAnswers() { return answers; }
    public Student getStudent() { return student; }
}

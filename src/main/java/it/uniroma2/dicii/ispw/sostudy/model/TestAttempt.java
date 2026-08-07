package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;

import java.util.ArrayList;
import java.util.List;

public class TestAttempt{
    private int grade;

    private List<TestAttemptAnswer> answers;
    private Student student;

    public TestAttempt(List<TestAttemptAnswer> answers, Student student) {
        List<TestAttemptAnswer> tmpAnswers = new ArrayList<>();
        for(TestAttemptAnswer a : answers){
            tmpAnswers.add(a.copy());
        }
        this.answers = tmpAnswers;
        this.student = student;
    }

    public TestAttempt(Student student) {
        this.student = student;
    }

    public void addAnswer(TestAttemptAnswer a){
        if(this.answers == null) {
            this.answers = new ArrayList<>();
        }
        TestAttemptAnswer tmpAnswer = a.copy();
        this.answers.add(tmpAnswer);
    }

    public TestAttemptAnswer getNextAnswer(TestAttemptAnswer a) throws ModelException{
         if( a == null ) throw new ModelException("[Class: TestAttempt] There is no next ANSWER for a null object");

        int currentPosition;
        currentPosition = answers.indexOf(a);
        if( currentPosition == -1 || currentPosition >= this.answers.size() - 1) throw new ModelException("[Class: TestAttempt] Trying to access outside of TestAttempt answer's bound");

        return answers.get(currentPosition + 1);
    }

    public void computeGrade(){
        int totalScore = 0;

        for(TestAttemptAnswer a : this.answers){
            totalScore += a.getScore();
        }

        this.grade = totalScore;
    }

    public List<TestAttemptAnswer> getAnswers() { return answers; }
    public Student getStudent() { return student; }
    public int getGrade() { return grade; }
}

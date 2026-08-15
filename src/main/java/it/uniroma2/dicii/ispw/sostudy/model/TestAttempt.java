package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestAttempt{
    private int testId;
    private int grade;
    private TestGradingStatus testGradingStatus;
    private LocalTime handInTime;
    private LocalDate handInDate;

    private Test test;
    private List<TestAttemptAnswer> answers;
    private Student student;

    public TestAttempt(Test test, List<TestAttemptAnswer> answers, Student student, int id, int grade, TestGradingStatus testGradingStatus, LocalTime handInTime, LocalDate handInDate) {
        this.test = test;
        List<TestAttemptAnswer> tmpAnswers = new ArrayList<>();
        for(TestAttemptAnswer a : answers){
            tmpAnswers.add(a.copy());
        }
        this.answers = tmpAnswers;
        this.student = student;
        this.testId = id;
        this.grade = grade;
        this.testGradingStatus = testGradingStatus;
        this.handInTime = handInTime;
        this.handInDate = handInDate;
    }

    public TestAttempt(Test test, Student student) {
        this.test = test;
        this.student = student;
    }

    public TestAttempt(Test test, List<TestAttemptAnswer> answers, Student student) {
        this.test = test;
        this.answers = answers;
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
        if( currentPosition == -1 || currentPosition >= this.answers.size() - 1) throw new ModelException("[Class: TestAttempt] Trying to access outside of TestAttempt answers' bound");

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
    public void setTestGradingStatus(TestGradingStatus testGradingStatus) { this.testGradingStatus = testGradingStatus; }
    public TestGradingStatus getTestGradingStatus() { return testGradingStatus; }
    public void setHandInTime(LocalTime handInTime) { this.handInTime = handInTime; }
    public LocalTime getHandInTime() { return this.handInTime; }
    public void setHandInDate(LocalDate handInDate) { this.handInDate = handInDate; }
    public LocalDate getHandInDate() { return this.handInDate; }
    public Test getTest() { return test; }
    public void setStudent(Student student) { this.student = student; }
    public void setTestId(int id) { this.testId = id; }
    public int getTestId() { return testId; }
}

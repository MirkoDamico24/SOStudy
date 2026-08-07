package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private String name;
    private LocalDateTime dueDate;
    private Duration duration;
    private int maxScore;

    private List<Question> questions = new ArrayList<>();
    private List<TestAttempt> tests;

    public Test(String name, LocalDateTime dueDate, Duration duration, int maxScore, Question question) {
        this.name = name;
        this.dueDate = dueDate;
        this.duration = duration;
        this.maxScore = maxScore;
        this.questions.add(question.copy());
    }

    public Test(String name, LocalDateTime dueDate, Duration duration, int maxScore, List<Question> questions) {
        this.name = name;
        this.dueDate = dueDate;
        this.duration = duration;
        this.maxScore = maxScore;
        for (Question question : questions) {
            this.questions.add(question.copy());
        }
    }

    public void addQuestion(OpenQuestion question) {
        Question q = new OpenQuestion(question.getHeader(), question.getMaxScore());
        this.questions.add(q);
    }

    public void addQuestion(CloseQuestion question) {
        Question q = new CloseQuestion(question.getHeader(), question.getMaxScore());
        this.questions.add(q);
    }

    public void addTestAttempt(TestAttempt testAttempt) {
        if(this.tests == null) {
            this.tests = new ArrayList<>();
        }
        TestAttempt t = new TestAttempt(testAttempt.getAnswers(), testAttempt.getStudent());
        this.tests.add(t);
    }

    public void deleteQuestion(Question question) {
        this.questions.remove(question);
    }

    public List<Question> getQuestions() {return this.questions;}

    public Question getNextQuestion(Question current) throws ModelException{
        if(current == null) throw new ModelException("[Class: Test] There is no next QUESTION for a null object");

        int currentPosition = this.questions.indexOf(current);
        if(currentPosition != -1 &&  currentPosition + 1 < this.questions.size())  return questions.get(currentPosition+1);
        else throw new ModelException("[Class: Test] Trying to access outside of Test questions' bound");
    }

    public void gradeTest(TestAttempt test) throws ModelException{
        if(!this.tests.contains(test)) throw new ModelException("[Class: Test] Cannot evaluate an attempt that is not relative to this test");

        List<Answer<?>> testAnswers = test.getAnswers();
        for(Answer<?> a : testAnswers){
            int index = testAnswers.indexOf(a);
            Question q = this.questions.get(index);

            try {
                q.evaluate(a);  //TODO: in questo caso vengono valutate solo le domande autovalutabili, implementare le altre
            }
            catch(ModelException e){
                throw new ModelException(e.getMessage(), e.getCause());
            }
        }

        test.computeGrade();
    }

    public String getName() {return this.name;}
    public LocalDateTime getDueDate() {return this.dueDate;}
    public Duration getDuration() {return this.duration;}
    public int getMaxScore() {return this.maxScore;}
    public void setName(String name) {this.name = name;}
    public void setDueDate(LocalDateTime dueDate) {this.dueDate = dueDate;}
    public void setDuration(Duration duration) {this.duration = duration;}
    public void setMaxScore(int maxScore) {this.maxScore = maxScore;}
    public List<TestAttempt> getTests() { return tests; }
    public void setTests(List<TestAttempt> tests) { this.tests = tests; }
}

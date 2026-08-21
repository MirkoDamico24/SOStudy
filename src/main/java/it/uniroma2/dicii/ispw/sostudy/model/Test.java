package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;
import it.uniroma2.dicii.ispw.sostudy.exception.OpenModelException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private String name;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private Duration duration;

    private List<Question> questions = new ArrayList<>();
    private List<TestAttempt> tests = null;
    private VirtualClass virtualClass;

    public Test(String name, LocalDate dueDate, LocalTime dueTime, Duration duration, Question question, VirtualClass virtualClass) {
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.duration = duration;
        this.questions.add(question.copy());
        this.virtualClass = virtualClass;
    }

    public Test(String name, LocalDate dueDate, LocalTime dueTime, Duration duration, List<Question> questions, VirtualClass virtualClass) {
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.duration = duration;
        for (Question question : questions) {
            this.questions.add(question.copy());
        }
        this.virtualClass = virtualClass;
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
        TestAttempt t = new TestAttempt(this, testAttempt.getAnswers(), testAttempt.getStudent());
        this.tests.add(t);
    }

    public void deleteQuestion(Question question) {
        this.questions.remove(question);
    }

    public Question getNextQuestion(Question current) throws ModelException{
        if(current == null) {
            return this.questions.getFirst();   //returns the first question
        }

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
                q.evaluate(a);
            }
            catch(OpenModelException e){
                //professor will have to evaluate open questions
                test.setTestGradingStatus(TestGradingStatus.INCOMPLETE);
            }
            catch(ModelException e){
                throw new ModelException(e.getMessage(), e.getCause());
            }
        }

        test.computeGrade();
    }

    public String getName() {return this.name;}
    public LocalDate getDueDate() {return this.dueDate;}
    public Duration getDuration() {return this.duration;}
    public List<Question> getQuestions() {return this.questions;}
    public void setName(String name) {this.name = name;}
    public void setDueDate(LocalDate dueDate) {this.dueDate = dueDate;}
    public void setDuration(Duration duration) {this.duration = duration;}
    public List<TestAttempt> getTests() { return tests; }
    public void setTests(List<TestAttempt> tests) {
        if(this.tests == null) this.tests = tests;
    }
    public VirtualClass getVirtualClass() {return this.virtualClass;}
    public void setVirtualClass(VirtualClass virtualClass) {this.virtualClass = virtualClass;}
    public void setDueTime(LocalTime dueTime) {this.dueTime = dueTime;}
    public LocalTime getDueTime() {return this.dueTime;}
}

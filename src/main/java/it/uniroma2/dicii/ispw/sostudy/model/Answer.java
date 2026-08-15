package it.uniroma2.dicii.ispw.sostudy.model;

public abstract class Answer<T> implements TestAttemptAnswer {
    private int score;
    private T content;
    private Question question;

    protected Answer(int score, T content, Question question) {
        this.score = score;
        this.content = content;
        this.question = question;
    }

    @Override
    public abstract Answer<T> copy();

    @Override
    public int getScore() { return score; }

    @Override
    public void setScore(int score) { this.score = score; }

    @Override
    public Question getQuestion() { return question; }

    public void setContent(T content) { this.content = content; }
    public T getContent() { return this.content; }

    public void setQuestion(Question question) { this.question = question; }

}

package it.uniroma2.dicii.ispw.sostudy.model;

public abstract class Answer<T> implements TestAttemptAnswer {
    private int score;
    private T content;

    protected Answer(T content) {
        this.content = content;
    }

    @Override
    public abstract Answer<T> copy();

    @Override
    public int getScore() { return score; }

    @Override
    public void setScore(int score) { this.score = score; }

    public void setContent(T content) { this.content = content; }
    public T getContent() { return this.content; }

}

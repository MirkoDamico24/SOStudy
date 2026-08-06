package it.uniroma2.dicii.ispw.sostudy.model;

public abstract class Answer<T> {
    private int score;
    private T content;

    public void setScore(int score) { this.score = score; }
    public int getScore() { return score; }
    public void setContent(T content) { this.content = content; }
    public T getContent() { return this.content; }
}

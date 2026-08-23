package it.uniroma2.dicii.ispw.sostudy.model;

public class Answer<T>{
    private int score;
    private T content;
    private Question question;

    public Answer(int score, T content, Question question) {
        this.score = score;
        this.content = content;
        this.question = question;
    }

    public Answer(T content, Question question) {
        this.content = content;
        this.question = question;
    }

    public Answer<T> copy() {
        return new Answer<>(this.getScore(), this.getContent(), this.getQuestion());
    }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public Question getQuestion() { return question; }

    public void setContent(T content) { this.content = content; }
    public T getContent() { return this.content; }

    public void setQuestion(Question question) { this.question = question; }
}
package it.uniroma2.dicii.ispw.sostudy.model;

public class OpenAnswer extends Answer<String> {
    public OpenAnswer(int score, String content, Question question) {
        super(score, content, question);
    }

    @Override
    public Answer<String> copy() {
        return new OpenAnswer(this.getScore(), getContent(), this.getQuestion());
    }
}

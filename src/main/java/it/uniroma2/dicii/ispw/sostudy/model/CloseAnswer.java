package it.uniroma2.dicii.ispw.sostudy.model;

public class CloseAnswer extends Answer<Choice> {

    public CloseAnswer(int score, Choice content, Question question) {
        super(score, content, question);
    }

    @Override
    public Answer<Choice> copy() {
        return new CloseAnswer(this.getScore(), this.getContent(), this.getQuestion());
    }
}

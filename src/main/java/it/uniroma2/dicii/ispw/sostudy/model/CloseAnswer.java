package it.uniroma2.dicii.ispw.sostudy.model;

public class CloseAnswer extends Answer<Choice> {

    public CloseAnswer(Choice content) {
        super(content);
    }

    @Override
    public Answer<Choice> copy() {
        return new CloseAnswer(this.getContent());
    }
}

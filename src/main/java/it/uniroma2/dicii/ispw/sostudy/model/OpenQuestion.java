package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;

public class OpenQuestion extends Question/*<OpenAnswer>*/{

    public OpenQuestion(String header, int maxScore) {
        super(header, maxScore);
    }

    @Override
    public void evaluate(Answer answer) throws ModelException {
        throw new ModelException("[Class: OpenQuestion] Cannot auto-evaluate an OpenQuestion");
    }

    @Override
    public Question copy() {
        return new OpenQuestion(this.getHeader(), this.getMaxScore());
    }

}

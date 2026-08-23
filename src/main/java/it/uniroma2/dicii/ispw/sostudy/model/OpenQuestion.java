package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;
import it.uniroma2.dicii.ispw.sostudy.exception.OpenModelException;

public class OpenQuestion extends Question{

    public OpenQuestion(String header, int maxScore) {
        super(header, maxScore);
    }

    @Override
    public void evaluate(Answer<?> answer) throws ModelException {
        throw new OpenModelException("[Class: OpenQuestion] Cannot auto-evaluate an OpenQuestion");
    }

    @Override
    public Question copy() {
        return new OpenQuestion(this.getHeader(), this.getMaxScore());
    }

    @Override
    public Answer<String> createAnswer(String textualContent, Integer integerContent) {
        return new Answer<>(textualContent, this);
    }
}

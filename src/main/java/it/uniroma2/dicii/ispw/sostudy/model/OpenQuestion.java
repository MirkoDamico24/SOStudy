package it.uniroma2.dicii.ispw.sostudy.model;

public class OpenQuestion extends Question<OpenAnswer>{

    public OpenQuestion(String header, int maxScore) {
        super(header, maxScore);
    }

    @Override
    public void evaluate(OpenAnswer answer) {
        throw new UnsupportedOperationException("OpenQuestion doesn't support autograding"); //TODO: implementa eccezioni correttamente
    }

}

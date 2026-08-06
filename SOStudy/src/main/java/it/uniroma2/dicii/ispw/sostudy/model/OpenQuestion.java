package it.uniroma2.dicii.ispw.sostudy.model;

public class OpenQuestion extends Question<OpenAnswer>{

    public OpenQuestion(String content, int maxScore) {
        super(content, maxScore);
    }

    @Override
    public void evaluate(OpenAnswer answer) {
        throw new UnsupportedOperationException("OpenQuestion doesn't support autograding");
    }

    @Override
    public Container getContent() {
        Container c = new Container(this.getHeader());
        return c;
    }
}

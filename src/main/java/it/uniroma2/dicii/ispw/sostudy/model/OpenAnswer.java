package it.uniroma2.dicii.ispw.sostudy.model;

public class OpenAnswer extends Answer<String> {
    public OpenAnswer(String content) {
        super(content);
    }

    @Override
    public Answer<String> copy() {
        return new OpenAnswer(getContent());
    }
}

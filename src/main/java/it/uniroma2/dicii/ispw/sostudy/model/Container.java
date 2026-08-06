package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.List;

public class Container implements OpenQuestionContainer, CloseQuestionContainer {
    private String questionHeader;
    private List<Choice> options;

    public  Container(String questionHeader) {
        this.questionHeader = questionHeader;
    }

    public Container(String questionHeader, List<Choice> options) {
        this.questionHeader = questionHeader;
        this.options = options;
    }

    @Override
    public String getQuestionHeader() { return this.questionHeader; }

    @Override
    public List<Choice> getOptions() { return this.options; }
}

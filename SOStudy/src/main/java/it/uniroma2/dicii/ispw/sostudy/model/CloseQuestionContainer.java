package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.List;

public interface CloseQuestionContainer extends OpenQuestionContainer {
    public List<Choice> getOptions();
}

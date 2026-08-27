package it.uniroma2.dicii.ispw.sostudy.bean;

public class AnswerBean {
    private String textualContent;
    private Integer chosenOption = null;
    private int assignedScore = -1;

    public AnswerBean(String textualContent) {
        this.textualContent = textualContent;
    }

    public AnswerBean(Integer choiceOption) {
        this.chosenOption = choiceOption;
    }

    public String getTextualContent() {
        return textualContent;
    }

    public void setTextualContent(String textualContent) {
        this.textualContent = textualContent;
    }

    public Integer getChosenOption() {
        return chosenOption;
    }

    public void setChosenOption(int chosenOption) {
        this.chosenOption = chosenOption;
    }

    public void setAssignedScore(int assignedScore) {
        this.assignedScore = assignedScore;
    }

    public int getAssignedScore() {
        return assignedScore;
    }
}

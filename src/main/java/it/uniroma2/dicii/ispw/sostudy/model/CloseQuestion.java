package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;

import java.util.ArrayList;
import java.util.List;

public class CloseQuestion extends Question {
    private List<Choice> choices;
    private Choice solution;

    public CloseQuestion(String header, int maxScore) {
        super(header, maxScore);
    }

    public CloseQuestion(String header, int maxScore, List<Choice> choices, Choice solution) {
        super(header, maxScore);
        List<Choice> choicesCopy = new ArrayList<>(choices);
        for (Choice choice : choices) {
            choicesCopy.add(new Choice(choice.getContent()));
        }
        this.choices = choicesCopy;
        this.solution = solution;
    }

    public void addChoice(Choice choice){
        if(this.choices == null) {
            this.choices = new ArrayList<>();
        }
        Choice c = new Choice(choice.getContent());
        this.choices.add(c);
    }

    public void addChoice(List<Choice> choices){
        if(this.choices == null) {
            this.choices = new ArrayList<>();
        }
        for (Choice c : choices){
            Choice c2 = new Choice(c.getContent());
            this.choices.add(c2);
        }
    }

    //TODO: Check if the implementation of this method is doable
    public void deleteChoice(Choice choice){
        this.choices.remove(choice);
    }

    public void addSolution(Choice choice) throws Exception{
        if(this.choices.contains(choice)) {
            this.solution = choice;
        }
        else{
            throw new Exception("[Class: CloseQuestion] The provided solution is not a choice of the question. It cannot be a solution.");
        }
    }

    public List<Choice> getChoices() { return this.choices; }

    @Override
    public void evaluate(Answer answer) throws ModelException {
        Choice answerContent = ((CloseAnswer) answer).getContent();
        int assignedScore = 0;

        if(answerContent.equals(this.solution)) assignedScore = this.getMaxScore();

        answer.setScore(assignedScore);
    }

    @Override
    public Question copy() {
        return new CloseQuestion(this.getHeader(), this.getMaxScore(),  this.getChoices(), this.solution);
    }
}

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
        List<Choice> choicesCopy = new ArrayList<>();
        for (Choice choice : choices) {
            Choice newChoice = new Choice(choice.getContent());
            choicesCopy.add(newChoice);
            if(choice == solution) this.solution = newChoice;
        }
        this.choices = choicesCopy;
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

    public void addSolution(Choice choice) throws ModelException{
        if(this.choices.contains(choice)) {
            this.solution = choice;
        }
        else{
            throw new ModelException("[Class: CloseQuestion] The provided solution is not a choice of the question. It cannot be a solution.");
        }
    }

    public Choice getSolution() {
        return solution;
    }

    public List<Choice> getChoices() { return this.choices; }

    @Override
    public void evaluate(Answer<?> answer) throws ModelException {
        Object content = answer.getContent();
        if(!(content instanceof Choice)) throw new ModelException("[Class: CloseQuestion] The provided answer is not a suitable for a CloseQuestion.");

        Choice answerContent = ((Choice) content);
        int assignedScore = answerContent.getContent().equals(this.solution.getContent()) ? this.getMaxScore() : 0;
        answer.setScore(assignedScore);
    }

    @Override
    public Question copy() {
        return new CloseQuestion(this.getHeader(), this.getMaxScore(),  this.getChoices(), this.solution);
    }

    @Override
    public Answer<Choice>  createAnswer(String textualContent, Integer integerContent) {
        Choice c = new Choice(this.getChoices().get(integerContent).getContent());
        return new Answer<>(c, this);
    }

}

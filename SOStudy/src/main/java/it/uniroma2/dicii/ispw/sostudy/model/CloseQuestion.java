package it.uniroma2.dicii.ispw.sostudy.model;

import java.util.ArrayList;
import java.util.List;

public class CloseQuestion extends Question<CloseAnswer> {
    private List<Choice> choices;
    private Choice solution;

    public CloseQuestion(CloseQuestionContainer content, int maxScore) {
        super(content.getQuestionHeader(), maxScore);
        this.choices = content.getOptions();
    }

    public void addChoice(Choice choice){
        Choice c = new Choice(choice.getContent());
        this.choices.add(c);
    }

    public void addChoice(List<Choice> choices){
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
            //TODO: handle exception properly
            throw new Exception("Choice not found");
        }
    }

    public List<Choice> getChoices() { return this.choices; }

    @Override
    public void evaluate(CloseAnswer answer) throws Exception {
        Choice answerContent = answer.getContent();
        int assignedScore = 0;

        if(answerContent.equals(this.solution)) assignedScore = this.getMaxScore();

        answer.setScore(assignedScore);
    }

    @Override
    public Container getContent() {
        Container c = new Container(this.getHeader(), this.choices);
        return c;
    }
}

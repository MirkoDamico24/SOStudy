package it.uniroma2.dicii.ispw.sostudy.eng.functional;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.util.ArrayList;
import java.util.List;

public class QuestionMapper {
    private QuestionMapper() {}

    public static Question dtoToQuestion(QuestionDTO dto){
        return switch (dto.type()){
            case  OPENQUESTION -> new OpenQuestion(dto.header(), dto.score());
            case CLOSEQUESTION -> new CloseQuestion(dto.header(), dto.score(), dto.options(), dto.solution());
        };
    }


    public static QuestionDTO questionToDTO(Question question) {
        return switch (question) {
            case OpenQuestion open -> new QuestionDTO(open.getHeader(), open.getMaxScore(),
                    QuestionType.OPENQUESTION, null, null
            );

            case CloseQuestion close -> new QuestionDTO(close.getHeader(), close.getMaxScore(),
                    QuestionType.CLOSEQUESTION, close.getChoices(), close.getSolution()
            );

            default -> throw new IllegalArgumentException("Invalid question type");
        };
    }

    public static QuestionBean questionToBean(Question question) {
        return switch (question) {
            case OpenQuestion open -> new QuestionBean(open.getHeader(), open.getMaxScore());

            case CloseQuestion close -> {
                List<String> choices = new ArrayList<>();
                for (Choice choice : close.getChoices()) {
                    choices.add(choice.getContent());
                }
                yield new QuestionBean(close.getHeader(), close.getMaxScore(), choices, close.getChoices().indexOf(close.getSolution()));
            }

            default -> throw new IllegalArgumentException("Invalid question type");
        };
    }

    public static Question beanToQuestion(QuestionBean question) {
        Question tempQuestion = null;
        if(question.getOptions() == null || question.getOptions().isEmpty()){
            tempQuestion = new OpenQuestion(question.getHeader(), question.getMaxScore());
        }
        else{
            List<Choice> choices = new ArrayList<>();
            for(String option : question.getOptions()) {
                Choice choice = new Choice(option);
                choices.add(choice);
            }
            Choice solution = choices.get(question.getSolution());
            tempQuestion = new CloseQuestion(question.getHeader(), question.getMaxScore(), choices, solution);

        }
        return tempQuestion;
    }
}

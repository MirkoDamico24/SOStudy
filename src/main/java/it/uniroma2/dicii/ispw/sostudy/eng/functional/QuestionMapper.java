package it.uniroma2.dicii.ispw.sostudy.eng.functional;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.model.*;

import java.util.ArrayList;
import java.util.List;

public class QuestionMapper {
    private QuestionMapper() {}

    public static Question DTOToQuestion(QuestionDTO dto){
        return switch (dto.type()){
            case  OPENQUESTION -> new OpenQuestion(dto.header(), dto.score());
            case CLOSEQUESTION -> new CloseQuestion(dto.header(), dto.score(), dto.options(), dto.solution());
        };
    }

    public static QuestionBean questionToBean(Question question) {
        QuestionBean questionBean = null;
        if(question instanceof OpenQuestion open){
            questionBean = new QuestionBean(open.getHeader(), open.getMaxScore());
        }
        else if(question instanceof CloseQuestion close){
            List<String> choices = new ArrayList<>();
            for(Choice choice : close.getChoices()){
                choices.add(choice.getContent());
            }
            questionBean = new QuestionBean(close.getHeader(), close.getMaxScore(), choices, close.getChoices().indexOf(close.getSolution()));
        }
        else throw new IllegalArgumentException("Invalid question type");

        return questionBean;
    }

    public static QuestionDTO questionToDTO(Question question){
        if(question instanceof OpenQuestion open){
            return new QuestionDTO(open.getHeader(), open.getMaxScore(),
                    QuestionType.OPENQUESTION, null, null
            );
        }
        else if(question instanceof CloseQuestion close)
            return new QuestionDTO(close.getHeader(), close.getMaxScore(),
                    QuestionType.CLOSEQUESTION, close.getChoices(), close.getSolution()
            );
        else throw new IllegalArgumentException("Invalid question type");
    }

}

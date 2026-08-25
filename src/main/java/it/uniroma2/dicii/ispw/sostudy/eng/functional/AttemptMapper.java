package it.uniroma2.dicii.ispw.sostudy.eng.functional;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.AttemptBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.StudentBean;
import it.uniroma2.dicii.ispw.sostudy.model.Answer;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;

import java.util.ArrayList;
import java.util.List;

public class AttemptMapper {
    private AttemptMapper() {}

    public static AttemptBean toBean(TestAttempt test) {
        if(test == null) return null;

        List<AnswerBean> answers = new ArrayList<>();
        List<QuestionBean> questions = new ArrayList<>();

        for(Answer a : test.getAnswers()) {
            Object o = a.getContent();
            if(o instanceof String textContent) {       //no need to add close questions, because they are evaluated by the system
                answers.add(new AnswerBean(textContent));
                questions.add(QuestionMapper.questionToBean(a.getQuestion()));
            }
        }

        return new AttemptBean(questions, answers,
                new StudentBean(test.getStudent().getName(),
                test.getStudent().getSurname(), test.getStudent().getEmail()));
    }
}

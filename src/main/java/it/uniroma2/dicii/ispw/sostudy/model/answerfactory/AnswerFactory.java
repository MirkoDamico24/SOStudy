package it.uniroma2.dicii.ispw.sostudy.model.answerfactory;

import it.uniroma2.dicii.ispw.sostudy.model.*;

public class AnswerFactory {
    public static Answer<?> createAnswer(int score, String textualContent, Choice choice, Question question) {
          if(choice == null){
            return new OpenAnswer(score, textualContent, question);
          }
          return new CloseAnswer(score, choice, question);
    }
}

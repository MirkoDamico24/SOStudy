package it.uniroma2.dicii.ispw.sostudy.eng.functional;

import it.uniroma2.dicii.ispw.sostudy.model.Choice;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;

import java.util.List;

public record QuestionDTO(String header, int score, QuestionType type, List<Choice> options, Choice solution) {
}

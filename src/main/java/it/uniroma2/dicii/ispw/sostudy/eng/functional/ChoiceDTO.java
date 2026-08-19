package it.uniroma2.dicii.ispw.sostudy.eng.functional;

import it.uniroma2.dicii.ispw.sostudy.model.Choice;

import java.util.List;

public record ChoiceDTO(List<Choice> options, Choice solution, int questionID) {
}

package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

public class CreaDomandaApertaControllerCLI extends CreaDomandaControllerCLI {

    @Override
    protected String getSubtitle() {
        return "                 Domanda a risposta aperta                  ";
    }

    @Override
    protected void gatherSpecificData() {
        score = InputReaderCLI.readInteger(
                "Punteggio (Inserire un numero intero per il punteggio massimo): ",
                "\n--> Errore: Il punteggio deve essere un numero intero valido!\n",
                scanner
        );
    }

    @Override
    protected void createAndSaveQuestionBean(String questionText) {
        QuestionBean qb = new QuestionBean(questionText, score);
        nav.getCurrentTest().addQuestion(qb);
    }

    @Override
    protected Views getSuccessView() {
        return Views.OPENQUESTIONVIEW;
    }
}
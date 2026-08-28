package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.AttemptBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.util.ArrayList;

public class EvaluateOpenAnswerViewController extends BaseControllerCLI {

    public void start() {
        while (true) {
            clearConsole();
            TestBean test = nav.getCurrentTest();
            AttemptBean workingOn = nav.getCurrentAttempt();

            int currentIndex = nav.getCurrentQuestionIndex();
            if (currentIndex == -1) {
                currentIndex = 0;
                nav.setCurrentQuestionIndex(currentIndex);
            }

            QuestionBean question = workingOn.getQuestions().get(currentIndex);
            AnswerBean answer = workingOn.getAnswers().get(currentIndex);

            printStandardHeader("Valutazione: " + test.getName());
            printNavBar();

            System.out.println("\nStudente in valutazione: " + workingOn.getStudent().getName() + " " + workingOn.getStudent().getSurname());
            System.out.println("Domanda " + (currentIndex + 1) + " di " + workingOn.getQuestions().size());
            System.out.println("------------------------------------------------------------");
            System.out.println("DOMANDA:\n" + question.getHeader());
            System.out.println("\nRISPOSTA STUDENTE:\n" + answer.getTextualContent());
            System.out.println("------------------------------------------------------------");

            String currentScoreStr = (answer.getAssignedScore() != -1) ? String.valueOf(answer.getAssignedScore()) : "___";
            System.out.println("Punteggio assegnato: " + currentScoreStr + " / " + question.getMaxScore());

            System.out.println("\n--- OPZIONI ---");
            System.out.println("Inserisci un numero per assegnare o sovrascrivere il punteggio");
            System.out.println("[A] Prossima domanda");
            System.out.println("[I] Indietro");

            System.out.print("\nScelta: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("A")) {
                handleNextQuestion(workingOn);
                return;
            } else if (choice.equals("I")) {
                handleGoBack();
                return;
            } else {
                try {
                    int score = Integer.parseInt(choice);

                    if (score > question.getMaxScore()) {
                        System.out.println("\n[!] Punteggio elevato: Non si può assegnare un punteggio più alto di quello previsto.");
                        System.out.println("La risposta sarà valutata con il punteggio massimo (" + question.getMaxScore() + ").");
                        score = question.getMaxScore();
                        System.out.print("Premi Invio per continuare...");
                        scanner.nextLine();
                    }

                    if(score == -1) score = 0;
                    answer.setAssignedScore(score);
                    System.out.println("Punteggio registrato temporaneamente. Scegli [N] per salvare e proseguire.");
                } catch (NumberFormatException e) {
                    System.out.print("\nInput non valido. Inserisci un punteggio numerico o un comando. Premi Invio per riprovare...");
                    scanner.nextLine();
                }
            }
        }
    }

    private void handleNextQuestion(AttemptBean workingOn) {
        if(workingOn.getAnswers().get(nav.getCurrentQuestionIndex()).getAssignedScore() == -1) {
            workingOn.getAnswers().get(nav.getCurrentQuestionIndex()).setAssignedScore(0);
        }
        int nextIndex = nav.getCurrentQuestionIndex() + 1;
        if (nextIndex == workingOn.getQuestions().size()) {
            nextIndex = -1;
        }
        nav.setCurrentQuestionIndex(nextIndex);
        nav.setPreviousView(Views.EVALUATEOPENANSWER);

        if (nextIndex == -1) {
            System.out.println("\n--- Valutazione completata ---");
            System.out.println("Hai valutato tutte le risposte.");
            submitEvaluation(workingOn);

            System.out.print("Premi Invio per tornare alla lista dei tentativi...");
            scanner.nextLine();
            nav.setQuestions(new ArrayList<>());
            nav.goToTestAttemptView();
        } else {
            nav.goToEvaluateOpenAnswerView();
        }
    }

    private void submitEvaluation(AttemptBean attempt) {
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        try {
            ctrl.registerEvaluation(nav.getSession(), attempt);
        } catch (ControllerException e) {
            System.out.println("\nErrore: Errore durante il salvataggio della valutazione. Riprovare.");
        }
    }

    private void handleGoBack() {
        Views previous = nav.getPreviousView();
        nav.setPreviousView(Views.EVALUATEOPENANSWER);

        if (previous == null) {
            System.out.println("Errore: Schermata precedente incompatibile. Flusso d'esecuzione compromesso.");
            System.exit(0);
        }

        switch (previous) {
            case TESTATTEMPTVIEW -> nav.goToTestAttemptView();
            case EVALUATEOPENANSWER -> {
                int newIndex = nav.getCurrentQuestionIndex() - 1;
                nav.setCurrentQuestionIndex(newIndex);
                if (newIndex == -1) {
                    nav.setQuestions(new ArrayList<>());
                    nav.goToTestAttemptView();
                } else {
                    nav.goToEvaluateOpenAnswerView();
                }
            }
            default -> {
                System.out.println("Errore: Schermata precedente incompatibile. Flusso d'esecuzione compromesso.");
                System.exit(0);
            }
        }
    }
}
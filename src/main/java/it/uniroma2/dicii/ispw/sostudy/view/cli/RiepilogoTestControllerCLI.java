package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.CreateTestController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.util.List;

public class RiepilogoTestControllerCLI extends BaseControllerCLI {

    public void start() {
        super.clearConsole();
        showView();
        manageInput();
    }

    private void showView() {
        super.printStandardHeader("CREA TEST");
        super.printNavBar();

        System.out.println("\n------------------------------------------------------------");
        System.out.println("                       Riepilogo test                       ");
        System.out.println("------------------------------------------------------------\n");

        printTestDetails();
        printQuestions();
    }

    private void printTestDetails() {
        TestBean test = nav.getContext().getTest();
        if (test != null) {
            System.out.println("Dettagli test");
            System.out.println("Nome test: " + test.getName());
            System.out.println("Assegnato a: " + test.getVirtualClass());
            System.out.println("Data di consegna: " + test.getDueDate());
            System.out.println("Ora di consegna: " + test.getDueTime());
            System.out.println("Durata test: " + test.getDuration().toMinutes() + " minutes");
            System.out.println("------------------------------------------------------------\n");
        }
    }

    private void printQuestions() {
        List<QuestionBean> questions = nav.getQuestions();
        int totalScore = 0;
        int totalQuestions = (questions.isEmpty()) ? questions.size() : 0;

        for (QuestionBean q : questions) {
            totalScore += q.getMaxScore();
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Totale domande aggiunte: " + totalQuestions + "           Punteggio totale test: " + totalScore);
        System.out.println("------------------------------------------------------------");

        if (questions == null || questions.isEmpty()) {
            System.out.println("Nessuna domanda aggiunta al test.");
        } else {
            for (int i = 0; i < questions.size(); i++) {
                QuestionBean q = questions.get(i);
                String type = q.isOpenQuestion() ? "Aperta" : "Multipla";

                System.out.printf("%d) [%s] %s%n", (i + 1), type, q.getHeader());
                if (!q.isOpenQuestion() && q.getOptions() != null) {
                    System.out.println("     Opzioni: " + String.join(", ", q.getOptions()));
                }
                System.out.printf("     Punteggio: %d punti%n", q.getMaxScore());
                System.out.println();

                totalScore += q.getMaxScore();
            }
        }

    }

    private void manageInput() {
        System.out.println("\nAzioni disponibili:");
        System.out.println("[1] Aggiungi domanda");
        System.out.println("[2] Modifica domanda");
        System.out.println("[3] Rimuovi domanda");
        System.out.println("[4] Salva e pubblica");
        System.out.print("\nScegli un'opzione: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> handleAddQuestion();
            case "2" -> handleModify();
            case "3" -> handleRemove();
            case "4" -> handleSavePublish();
            default -> {
                System.out.println("\n--> Operazione non consentita!");
                start();
            }
        }
    }

    private void handleAddQuestion() {
        nav.setPreviousView(Views.RECAP);
        QuestionType qt = NavigatorCLI.selectQuestionType();
        if (qt == QuestionType.OPENQUESTION) {
            nav.goToOpenQuestionView();
        } else {
            nav.goToCloseQuestionView();
        }
    }

    private int getQuestionIndex(String actionMsg) {
        List<QuestionBean> questions = nav.getContext().getQuestions();
        if (questions == null || questions.isEmpty()) {
            System.out.println("\n--> Nessuna domanda disponibile per la " + actionMsg + ".");
            return -1;
        }

        System.out.print("Inserisci il numero della domanda da " + actionMsg + ": ");
        String input = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < questions.size()) {
                return index;
            } else {
                System.out.println("\n--> Numero domanda non trovato.");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.out.println("\n--> Input non valido.");
            return -1;
        }
    }

    private void removeQuestionByIndex(int index) {
        List<QuestionBean> questions = nav.getContext().getQuestions();
        if (questions != null && index >= 0 && index < questions.size()) {
            questions.remove(index);
        }
    }

    private void handleModify() {
        int index = getQuestionIndex("modificare");
        if (index == -1) {
            promptContinue();
            start();
            return;
        }

        QuestionBean q = nav.getContext().getQuestions().get(index);
        nav.setPreviousView(Views.RECAP);

        removeQuestionByIndex(index);

        if (q.isOpenQuestion()) {
            nav.goToOpenQuestionView();
        } else {
            nav.goToCloseQuestionView();
        }
    }

    private void handleRemove() {
        int index = getQuestionIndex("rimuovere");
        if (index == -1) {
            promptContinue();
            start();
            return;
        }

        removeQuestionByIndex(index);
        System.out.println("\n--> Domanda rimossa con successo.");
        promptContinue();
        start();
    }

    private void handleSavePublish() {
        CreateTestController createTestController = new CreateTestController();
        try {
            int sessionId = nav.getContext().getSession().getSessionID();
            TestBean test = nav.getContext().getTest();
            List<QuestionBean> questions = nav.getContext().getQuestions();

            System.out.println("\n--> Salvataggio e pubblicazione in corso...");
            createTestController.createTest(sessionId, test, questions);

            System.out.println("--> Test salvato e pubblicato con successo!");
            promptContinue();

            nav.setPreviousView(Views.RECAP);
            nav.goToHomeView();
        } catch (ControllerException e) {
            System.out.println("\n--> Errore durante il salvataggio: " + e.getMessage());
            promptContinue();
            start();
        }
    }

    private void promptContinue() {
        System.out.print("Premi INVIO per continuare...");
        scanner.nextLine();
    }
}
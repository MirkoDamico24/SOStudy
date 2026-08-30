package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

public class CreaDomandaApertaControllerCLI extends BaseControllerCLI {

    public void start() {
        super.clearConsole();
        showView();
        manageInput();
    }

    private void showView() {
        super.printStandardHeader("CREA TEST");

        System.out.println("\n------------------------------------------------------------");
        System.out.println("                 Domanda a risposta aperta                  ");
        System.out.println("------------------------------------------------------------\n");
    }

    private void manageInput() {
        System.out.print("Testo della domanda (Inserire il testo della domanda): ");
        String questionText = scanner.nextLine().trim();

        int score = InputReaderCLI.readInteger(
                "Punteggio (Inserire un numero intero per il punteggio massimo): ",
                "\n--> Errore: Il punteggio deve essere un numero intero valido!\n",
                scanner
        );

        System.out.println("\nAzioni disponibili:");
        System.out.println("[1] Salva domanda");
        System.out.println("[0] Indietro");
        System.out.print("\nScegli un'opzione: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.println("\n--> Salvataggio in corso...");
                System.out.println("--> Domanda salvata con successo!");
                saveQuestion(questionText, score);
                nav.setPreviousView(Views.OPENQUESTIONVIEW);
                nav.goToRecapView();
            }
            case "0" -> {
                System.out.println("\n--> Torno indietro...");
                nav.getContext().setQuestionToEdit(null);
                if(nav.getPreviousView() == Views.CREATETEST){
                    nav.goToCreateTestView();
                }
                else nav.goToRecapView();

                nav.setPreviousView(Views.OPENQUESTIONVIEW);
            }
            default -> {
                System.out.println("\n--> Operazione non consentita!");
                start();
            }
        }
    }

    private void saveQuestion(String questionText, int score) {
        QuestionBean qb = new QuestionBean(questionText, score);
        nav.getCurrentTest().addQuestion(qb);
    }
}
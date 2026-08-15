package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.util.Scanner;

public class CreaDomandaApertaControllerCLI {
    private NavigatorCLI nav;
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        clearConsole();
        showView();
        manageInput();
    }

    private void showView() {
        String username = nav.getContext().getSession().getProfessor().getName() + " " + nav.getContext().getSession().getProfessor().getSurname();

        System.out.println("\n============================================================");
        System.out.printf("  SoStudy | CREA TEST | %s%n", username);
        System.out.println("============================================================");

        printNavBar();

        System.out.println("\n------------------------------------------------------------");
        System.out.println("                 Domanda a risposta aperta                  ");
        System.out.println("------------------------------------------------------------\n");
    }

    private void printNavBar() {
        System.out.println("[NavBar]: Home | Classi Virtuali | Crea test");
        System.out.println("                                   ---------");
    }

    private void manageInput() {
        System.out.print("Testo della domanda (Inserire il testo della domanda): ");
        String questionText = scanner.nextLine().trim();

        int score = 0;
        boolean validScore = false;

        while (!validScore) {
            System.out.print("Punteggio (Inserire un numero intero per il punteggio massimo): ");
            String scoreInput = scanner.nextLine().trim();
            try {
                score = Integer.parseInt(scoreInput);
                validScore = true;
            } catch (NumberFormatException e) {
                System.out.println("\n--> Errore: Il punteggio deve essere un numero intero valido!\n");
            }
        }
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
                nav.goToHomeView();
            }
            default -> {
                System.out.println("\n--> Operazione non consentita!");
                start();
            }
        }
    }

    private void saveQuestion(String questionText, int score) {
        QuestionBean qb = new QuestionBean(questionText, score);
        nav.getContext().setQuestions(qb);
    }

    public void setNavigator(NavigatorCLI nav) {
        this.nav = nav;
    }

    private void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
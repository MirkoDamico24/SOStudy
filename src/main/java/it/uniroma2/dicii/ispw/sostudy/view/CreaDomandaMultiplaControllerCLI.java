package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreaDomandaMultiplaControllerCLI {
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
        System.out.println("                Domanda a risposta multipla                 ");
        System.out.println("------------------------------------------------------------\n");
    }

    private void printNavBar() {
        System.out.println("[NavBar]: Home | Classi Virtuali | Crea test");
        System.out.println("                                   ---------");
    }

    private void manageInput() {
        System.out.print("Testo della domanda (Inserire il testo della domanda): ");
        String questionText = scanner.nextLine().trim();

        List<String> options = getOptions();
        int solutionIndex = getSolutionIndex(options);
        int score = getScore();

        System.out.println("\nAzioni disponibili:");
        System.out.println("[1] Salva domanda");
        System.out.println("[0] Indietro");
        System.out.print("\nScegli un'opzione: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.println("\n--> Salvataggio in corso...");
                System.out.println("--> Domanda salvata con successo!");
                saveQuestion(questionText, score, options, solutionIndex);
                nav.setPreviousView(Views.CLOSEQUESTIONVIEW);
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

    private List<String> getOptions() {
        List<String> options = new ArrayList<>();
        System.out.println("\n--- Inserimento Opzioni ---");

        System.out.print("Opzione 1 (Inserire il testo della prima opzione): ");
        options.add(scanner.nextLine().trim());

        System.out.print("Opzione 2 (Inserire il testo della seconda opzione): ");
        options.add(scanner.nextLine().trim());

        boolean addingOptions = true;
        while (addingOptions) {
            System.out.println("\nVuoi aggiungere un'altra opzione?");
            System.out.println("[1] Si");
            System.out.println("[2] No");
            System.out.print("Scelta: ");
            String addChoice = scanner.nextLine().trim();

            if ("1".equals(addChoice)) {
                System.out.printf("Opzione %d (Inserire il testo dell'opzione): ", options.size() + 1);
                options.add(scanner.nextLine().trim());
            } else if ("2".equals(addChoice)) {
                addingOptions = false;
            } else {
                System.out.println("--> Scelta non valida, riprova.");
            }
        }
        return options;
    }

    private int getSolutionIndex(List<String> options) {
        System.out.println("\n--- Selezione Soluzione ---");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, options.get(i));
        }

        int solutionIndex = -1;
        boolean validSolution = false;
        while (!validSolution) {
            System.out.print("Quale opzione è la soluzione corretta? (Inserire il numero corrispondente): ");
            String solInput = scanner.nextLine().trim();
            try {
                int num = Integer.parseInt(solInput);
                if (num >= 1 && num <= options.size()) {
                    solutionIndex = num - 1;
                    validSolution = true;
                } else {
                    System.out.println("--> Numero non valido, deve essere compreso tra 1 e " + options.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("--> Errore: Inserire un numero intero valido!");
            }
        }
        return solutionIndex;
    }

    private int getScore() {
        int score = 0;
        boolean validScore = false;
        while (!validScore) {
            System.out.print("\nPunteggio (Inserire un numero intero per il punteggio massimo): ");
            String scoreInput = scanner.nextLine().trim();
            try {
                score = Integer.parseInt(scoreInput);
                validScore = true;
            } catch (NumberFormatException e) {
                System.out.println("\n--> Errore: Il punteggio deve essere un numero intero valido!\n");
            }
        }
        return score;
    }

    private void saveQuestion(String questionText, int score, List<String> options, int solutionIndex) {
        QuestionBean qb = new QuestionBean(questionText, score, options, solutionIndex);
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
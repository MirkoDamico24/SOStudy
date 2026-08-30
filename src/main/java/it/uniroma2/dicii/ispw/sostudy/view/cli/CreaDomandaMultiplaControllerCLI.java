package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.util.ArrayList;
import java.util.List;

public class CreaDomandaMultiplaControllerCLI extends CreaDomandaControllerCLI {

    private List<String> options;
    private int solutionIndex;

    @Override
    protected String getSubtitle() {
        return "                Domanda a risposta multipla                 ";
    }

    @Override
    protected void gatherSpecificData() {
        this.options = getOptions();
        this.solutionIndex = getSolutionIndex(options);
        this.score = InputReaderCLI.readInteger(
                "\nPunteggio (Inserire un numero intero per il punteggio massimo): ",
                "\n--> Errore: Il punteggio deve essere un numero intero valido!\n",
                scanner
        );
    }

    @Override
    protected void createAndSaveQuestionBean(String questionText) {
        QuestionBean qb = new QuestionBean(questionText, score, options, solutionIndex);
        nav.getCurrentTest().addQuestion(qb);
    }

    @Override
    protected Views getSuccessView() {
        return Views.CLOSEQUESTIONVIEW;
    }

    private List<String> getOptions() {
        List<String> optionsList = new ArrayList<>();
        System.out.println("\n--- Inserimento Opzioni ---");

        System.out.print("Opzione 1 (Inserire il testo della prima opzione): ");
        optionsList.add(scanner.nextLine().trim());

        System.out.print("Opzione 2 (Inserire il testo della seconda opzione): ");
        optionsList.add(scanner.nextLine().trim());

        boolean addingOptions = true;
        while (addingOptions) {
            System.out.println("\nVuoi aggiungere un'altra opzione?");
            System.out.println("[1] Si");
            System.out.println("[2] No");
            System.out.print("Scelta: ");
            String addChoice = scanner.nextLine().trim();

            if ("1".equals(addChoice)) {
                System.out.printf("Opzione %d (Inserire il testo dell'opzione): ", optionsList.size() + 1);
                optionsList.add(scanner.nextLine().trim());
            } else if ("2".equals(addChoice)) {
                addingOptions = false;
            } else {
                System.out.println("--> Scelta non valida, riprova.");
            }
        }
        return optionsList;
    }

    private int getSolutionIndex(List<String> optionsList) {
        System.out.println("\n--- Selezione Soluzione ---");
        for (int i = 0; i < optionsList.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, optionsList.get(i));
        }

        int solIndex = -1;
        boolean validSolution = false;
        while (!validSolution) {
            int num = InputReaderCLI.readInteger(
                    "Quale opzione è la soluzione corretta? (Inserire il numero corrispondente): ",
                    "--> Errore: Inserire un numero intero valido!",
                    scanner
            );

            if (num >= 1 && num <= optionsList.size()) {
                solIndex = num - 1;
                validSolution = true;
            } else {
                System.out.println("--> Numero non valido, deve essere compreso tra 1 e " + optionsList.size());
            }
        }
        return solIndex;
    }
}
package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;

import java.util.List;

public class CloseAnswerViewControllerCLI extends AnswerViewControllerCLI {

    @Override
    protected void gatherAndSubmitAnswer(QuestionBean question, int currentIndex) {
        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + options.get(i));
        }

        printTimerTip();
        attachToTimer();

        Integer rispostaSelezionata = null;

        while (rispostaSelezionata == null) {
            System.out.print("\nSeleziona l'opzione corretta (inserisci il numero): ");
            String input = scanner.nextLine().trim();

            if (handleTimerCommand(input)) {
                continue;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= options.size()) {
                    rispostaSelezionata = choice - 1;
                } else {
                    System.out.println("Selezione non valida. Inserisci un numero tra 1 e " + options.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Per favore, inserisci un numero o '?tempo'.");
            }
        }

        dispose();
        submitAnswer(rispostaSelezionata, currentIndex);
    }

    private void submitAnswer(int rispostaSelezionata, int currentIndex) {
        AnswerBean answer = new AnswerBean(rispostaSelezionata);
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.registerAnswer(nav.getSession(), answer, currentIndex);
    }

    @Override
    protected void submitAnswerOnTimeout(int currentIndex) {
        submitAnswer(0, currentIndex);
    }
}
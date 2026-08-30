package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;

public class OpenAnswerViewControllerCLI extends AnswerViewControllerCLI {

    @Override
    protected void gatherAndSubmitAnswer(QuestionBean question, int currentIndex) {
        printTimerTip();
        attachToTimer();

        String answerText = "";

        while (answerText.isEmpty() || answerText.equalsIgnoreCase("?tempo")) {
            System.out.print("\nInserisci la tua risposta: ");
            answerText = scanner.nextLine().trim();

            if (handleTimerCommand(answerText)) {
                answerText = "";
            } else if (answerText.isEmpty()) {
                System.out.println("La risposta non può essere vuota.");
            }
        }

        dispose();
        submitAnswer(answerText, currentIndex);
    }

    private void submitAnswer(String answerText, int currentIndex) {
        AnswerBean answer = new AnswerBean(answerText);
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.registerAnswer(nav.getSession(), answer, currentIndex);
    }

    @Override
    protected void submitAnswerOnTimeout(int currentIndex) {
        submitAnswer("[Tempo Scaduto]", currentIndex);
    }
}
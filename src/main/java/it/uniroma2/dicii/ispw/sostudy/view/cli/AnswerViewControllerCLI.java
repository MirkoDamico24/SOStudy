package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.TestTimerService;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.observer.TimerObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class AnswerViewControllerCLI extends BaseControllerCLI implements TimerObserver {

    protected TestTimerService testTimer;

    public void start() {
        clearConsole();
        TestBean test = nav.getCurrentTest();

        int currentIndex = nav.getCurrentQuestionIndex();
        if (currentIndex == -1) {
            currentIndex = 0;
            nav.setCurrentQuestionIndex(currentIndex);
        }

        List<QuestionBean> questions = nav.getQuestions();
        QuestionBean question = questions.get(currentIndex);

        printStandardHeader("Svolgimento Test: " + test.getName());

        System.out.println("\nDomanda " + (currentIndex + 1) + " di " + questions.size());
        System.out.println("------------------------------------------------------------");
        System.out.println(question.getHeader());
        System.out.println("------------------------------------------------------------");

        gatherAndSubmitAnswer(question, currentIndex);

        handleProssimaDomanda(currentIndex, questions.size());
    }

    protected abstract void gatherAndSubmitAnswer(QuestionBean question, int currentIndex);

    protected abstract void submitAnswerOnTimeout(int currentIndex);

    protected void printTimerTip() {
        System.out.println("\n[!] Il timer è attivo in background.");
        System.out.println("💡 Tip: Digita '?tempo' e premi Invio in qualsiasi momento per visualizzare il tempo rimanente.");
    }

    protected boolean handleTimerCommand(String input) {
        if (input.equalsIgnoreCase("?tempo")) {
            if (testTimer != null) {
                long minutes = testTimer.getRemaining().toMinutes();
                long seconds = testTimer.getRemaining().minusMinutes(minutes).getSeconds();
                System.out.printf("⏳ Tempo rimanente: %02d:%02d%n", minutes, seconds);
            }
            return true;
        }
        return false;
    }

    protected void handleProssimaDomanda(int currentIndex, int totalQuestions) {
        int nextIndex = currentIndex + 1;
        nav.setCurrentQuestionIndex(nextIndex);

        if (nextIndex >= totalQuestions) {
            System.out.println("\nTest completato! Salvataggio e consegna in corso...");
            KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
            ctrl.submitAttempt(nav.getSession());
            nav.setQuestions(new ArrayList<>());
            nav.setCurrentQuestionIndex(-1);

            System.out.print("Premi Invio per tornare alla Home...");
            scanner.nextLine();
            nav.goToHomeView();
        } else {
            QuestionBean nextQuestion = nav.getQuestions().get(nextIndex);
            if (nextQuestion.isOpenQuestion()) {
                nav.goToOpenAnswerView();
            } else {
                nav.goToCloseAnswerView();
            }
        }
    }

    protected void attachToTimer() {
        testTimer = nav.getSession().getTimer();
        if (testTimer != null) {
            testTimer.attach(this);
        }
    }

    public void dispose() {
        if (testTimer != null) {
            testTimer.detach(this);
        }
    }

    @Override
    public void update() {
        //using CLI not possible to update timer while gathering input
    }

    @Override
    public void conclude() {
        System.out.println("\n\n============================================================");
        System.out.println("⏳ TEMPO SCADUTO!");
        System.out.println("Il tempo a disposizione per lo svolgimento del test è terminato.");
        System.out.println("La prova verrà inviata automaticamente.");
        System.out.println("Premi Invio per continuare e tornare alla Home...");
        System.out.println("============================================================\n");

        dispose();

        submitAnswerOnTimeout(nav.getCurrentQuestionIndex());

        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.submitAttempt(nav.getSession());
        nav.setQuestions(new ArrayList<>());
        nav.setCurrentQuestionIndex(-1);
        nav.goToHomeView();
    }
}
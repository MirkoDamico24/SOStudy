package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class InsideClassViewControllerCLI extends BaseControllerCLI {

    public void start() {
        boolean isRunning = true;

        while (isRunning) {
            List<TestBean> allTests = nav.getCurrentClass().getTest();
            List<TestBean> activeTests = getActiveTests(allTests);
            List<TestBean> expiredTests = getExpiredTests(allTests);

            int totalIndex = renderScreen(activeTests, expiredTests);

            System.out.print("\nInserisci l'ID del test da selezionare o scegli un'opzione: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            isRunning = processUserChoice(choice, activeTests, expiredTests, totalIndex);
        }
    }

    private List<TestBean> getActiveTests(List<TestBean> allTests) {
        List<TestBean> activeTests = new ArrayList<>();
        if (allTests == null) {
            return activeTests;
        }

        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        for (TestBean t : allTests) {
            if (t.getDueDate() == null || !t.getDueDate().isBefore(today)) {
                activeTests.add(t);
            }
        }
        return activeTests;
    }

    private List<TestBean> getExpiredTests(List<TestBean> allTests) {
        List<TestBean> expiredTests = new ArrayList<>();
        if (allTests == null) {
            return expiredTests;
        }

        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        for (TestBean t : allTests) {
            if (t.getDueDate() != null && t.getDueDate().isBefore(today)) {
                expiredTests.add(t);
            }
        }
        return expiredTests;
    }

    private int renderScreen(List<TestBean> activeTests, List<TestBean> expiredTests) {
        clearConsole();
        printStandardHeader("Classe: " + nav.getCurrentClass().getClassName());
        printNavBar();

        System.out.println("\n--- TEST ATTIVI ---");
        int index = 1;
        for (TestBean t : activeTests) {
            System.out.println("[" + index + "] " + t.getName());
            index++;
        }

        System.out.println("\n--- TEST SCADUTI ---");
        for (TestBean t : expiredTests) {
            System.out.println("[" + index + "] " + t.getName());
            index++;
        }

        System.out.println("\n--- OPZIONI ---");
        System.out.println("[H] Torna alla Home");
        if (getCurrentUserRole() == UserRole.PROFESSOR) {
            System.out.println("[C] Crea nuovo Test");
            System.out.println("[I] Invita studente (non implementata)");
        }

        return index;
    }

    private boolean processUserChoice(String choice, List<TestBean> activeTests, List<TestBean> expiredTests, int totalIndex) {
        if (choice.equals("H")) {
            nav.setPreviousView(Views.INSIDECLASSVIEW);
            nav.goToHomeView();
            return false;
        }

        if (choice.equals("C") && getCurrentUserRole() == UserRole.PROFESSOR) {
            nav.setPreviousView(Views.INSIDECLASSVIEW);
            nav.goToCreateTestView();
            return false;
        }

        if (choice.equals("I") && getCurrentUserRole() == UserRole.PROFESSOR) {
            System.out.println("\nFunzionalità non ancora implementata. Premi Invio...");
            scanner.nextLine();
            return true;
        }

        return handleTestSelectionParsing(choice, activeTests, expiredTests, totalIndex);
    }

    private boolean handleTestSelectionParsing(String choice, List<TestBean> activeTests, List<TestBean> expiredTests, int totalIndex) {
        try {
            int selectedInt = Integer.parseInt(choice);

            if (selectedInt >= 1 && selectedInt < totalIndex) {
                int expiredStartIndex = activeTests.size() + 1;
                TestBean selectedTest = (selectedInt < expiredStartIndex)
                        ? activeTests.get(selectedInt - 1)
                        : expiredTests.get(selectedInt - expiredStartIndex);

                handleTestSelection(selectedTest);
                return false;
            }

            System.out.print("\nSelezione non valida. Premi Invio per riprovare...");
            scanner.nextLine();
            return true;

        } catch (NumberFormatException e) {
            System.out.print("\nInput non valido. Premi Invio per riprovare...");
            scanner.nextLine();
            return true;
        }
    }

    @Override
    public void printNavBar() {
        System.out.print("[NavBar]: Home | " + nav.getCurrentClass().getClassName());
        if (this.getCurrentUserRole() == UserRole.PROFESSOR) {
            System.out.print(" | Crea test");
        }
        System.out.println("\n                -----------------");
    }

    private void handleTestSelection(TestBean test) {
        if (getCurrentUserRole() == UserRole.STUDENT) {
            processStudentTestSelection(test);
        } else if (getCurrentUserRole() == UserRole.PROFESSOR) {
            nav.setPreviousView(Views.INSIDECLASSVIEW);
            nav.setCurrentTest(test);
            nav.goToTestAttemptView();
        }
    }

    private void processStudentTestSelection(TestBean test) {
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        List<QuestionBean> questions;

        try {
            questions = ctrl.loadRequiredTest(nav.getSession(), test);
        } catch (ControllerException e) {
            System.out.println("\nErrore: " + e.getMessage());
            System.out.print("Premi Invio per tornare indietro...");
            scanner.nextLine();
            nav.goToInsideClassView();
            return;
        }

        if (questions == null || questions.isEmpty()) {
            System.out.println("\nErrore: Il test selezionato è già stato svolto. Non è possibile svolgere più di un tentativo.");
            System.out.print("Premi Invio per selezionare un altro test...");
            scanner.nextLine();
            nav.goToInsideClassView();
            return;
        }

        nav.setQuestions(questions);
        nav.setCurrentTest(test);
        System.out.println("\nIl tentativo sarà avviato. Premi Invio per iniziare...");
        scanner.nextLine();

        nav.setPreviousView(Views.INSIDECLASSVIEW);

        if (questions.getFirst().isOpenQuestion()) {
            nav.goToOpenAnswerView();
        } else {
            nav.goToCloseAnswerView();
        }
    }
}
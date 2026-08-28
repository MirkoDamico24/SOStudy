package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.AttemptBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.SessionManager;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.util.List;

public class TestAttemptViewControllerCLI extends BaseControllerCLI {

    public void start() {
        boolean isRunning = true;

        while (isRunning) {
            clearConsole();
            printStandardHeader("Sottomissioni degli Studenti");
            printNavBar();

            if (!loadAndDisplayAttempts()) {
                return;
            }

            System.out.print("\nSeleziona l'ID del test da valutare o un'opzione di navigazione: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            isRunning = processNavigationChoice(choice);
        }
    }

    private boolean loadAndDisplayAttempts() {
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        List<AttemptBean> attempts;

        try {
            attempts = ctrl.loadTestAttempts(nav.getSession(), nav.getCurrentTest());
        } catch (ControllerException e) {
            System.out.println("\nErrore: Errore durante il caricamento dei tentativi di test");
            System.out.print("Premi Invio per riprovare...");
            scanner.nextLine();
            handleGoBack();
            return false;
        }

        nav.setAttempts(attempts);
        renderAttemptsList(attempts);

        return true;
    }

    private void renderAttemptsList(List<AttemptBean> attempts) {
        System.out.println("\n--- LISTA TENTATIVI ---");

        if (attempts == null) {
            System.out.println("Nessuno studente ha ancora svolto il test!");
        } else if (attempts.isEmpty()) {
            System.out.println("Non ci sono test da corregere!");
        } else {
            for (int i = 0; i < attempts.size(); i++) {
                AttemptBean a = attempts.get(i);
                System.out.printf("[%d] Test di: %s %s%n", i + 1, a.getStudent().getName(), a.getStudent().getSurname());
            }
        }

        System.out.println("\n--- MENU DI NAVIGAZIONE ---");
        System.out.println("[I] Indietro (Torna alla Classe)");
        System.out.println("[V] Classi Virtuali");
        System.out.println("[C] Crea test");
        System.out.println("[L] Logout");
    }

    private boolean processNavigationChoice(String choice) {
        switch (choice) {
            case "I" -> {
                handleGoBack();
                return false;
            }
            case "V" -> {
                handleNavClassiVirtuali();
                return false;
            }
            case "C" -> {
                handleNavCreaTest();
                return false;
            }
            case "L" -> {
                handleLogout();
                return false;
            }
            default -> {
                return handleAttemptSelection(choice);
            }
        }
    }

    private boolean handleAttemptSelection(String choice) {
        List<AttemptBean> attempts = nav.getAttempts();

        try {
            int selectedInt = Integer.parseInt(choice);

            if (attempts != null && selectedInt >= 1 && selectedInt <= attempts.size()) {
                AttemptBean selected = attempts.get(selectedInt - 1);

                System.out.println("\nVerrà avviata la valutazione del test.");
                System.out.print("Premi Invio per iniziare...");
                scanner.nextLine();

                nav.setCurrentAttempt(selected);
                nav.setPreviousView(Views.TESTATTEMPTVIEW);
                nav.goToEvaluateOpenAnswerView();
                return false;
            }

            System.out.print("\nSelezione non valida. Premi Invio per riprovare...");
            scanner.nextLine();
            return true;

        } catch (NumberFormatException e) {
            System.out.print("\nInput non valido. Seleziona un ID numerico o una lettera del menu. Premi Invio per riprovare...");
            scanner.nextLine();
            return true;
        }
    }

    private void handleGoBack() {
        nav.setCurrentTest(null);
        nav.setPreviousView(Views.TESTATTEMPTVIEW);
        nav.goToInsideClassView();
    }

    private void handleNavClassiVirtuali() {
        nav.setCurrentTest(null);
        nav.setPreviousView(Views.TESTATTEMPTVIEW);
        nav.goToClassesView();
    }

    private void handleNavCreaTest() {
        nav.setCurrentTest(null);
        nav.setPreviousView(Views.TESTATTEMPTVIEW);
        nav.goToCreateTestView();
    }

    private void handleLogout() {
        if (nav.getSession() != null) {
            SessionManager.getInstance().deleteSession(nav.getSession().getSessionID());
            nav.setSession(null);
        }
        nav.setPreviousView(Views.TESTATTEMPTVIEW);
        nav.goToLoginView();
    }
}
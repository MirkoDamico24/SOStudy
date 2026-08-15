package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.controller.CreateTestController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreaTestDetailControllerCLI {
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
        System.out.println("                     Dettagli del test                      ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Informazioni del test");
        System.out.println("------------------------------------------------------------\n");
    }

    private void printNavBar() {
        System.out.println("[NavBar]: Home | Classi Virtuali | Crea test");
        System.out.println("                                   ---------");
    }

    private List<String> loadAvailableClasses() {
        List<String> classes = new ArrayList<>();
        try {
            CreateTestController createTestController = new CreateTestController();
            String profEmail = nav.getContext().getSession().getProfessor().getEmail();
            List<VirtualClassBean> professorClasses = createTestController.getProfessorClasses(profEmail);
            for (VirtualClassBean vClassBean : professorClasses) {
                classes.add(vClassBean.getClassName());
            }
        } catch (ControllerException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return classes;
    }

    private void manageInput() {
        System.out.print("Nome test (Inserire un nome per il test): ");
        String testName = scanner.nextLine().trim();

        List<String> availableClasses = loadAvailableClasses();
        System.out.println("\nAvailable classes:");
        for (String availableClass : availableClasses) {
            System.out.println("- " + availableClass);
        }
        System.out.print("Assegna a (inserire il nome della classe virtuale): ");
        String virtualClass = scanner.nextLine().trim();

        System.out.print("Data di consegna (yyyy-mm-dd): ");
        String deliveryDate = scanner.nextLine().trim();

        System.out.print("Orario di consegna (formato HH:MM): ");
        String deliveryTime = scanner.nextLine().trim();

        System.out.print("Durata del test (Durata in minuti): ");
        String duration = scanner.nextLine().trim();

        System.out.println("\nAzioni disponibili:");
        System.out.println("[1] Salva");
        System.out.println("[0] Annulla e torna alla Home");
        System.out.print("\nScegli un'opzione: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.println("\n--> Salvataggio in corso...");
                System.out.println("--> Test salvato con successo!");
                saveTest(testName, virtualClass, deliveryDate, deliveryTime, duration);
                QuestionType qt = NavigatorCLI.selectQuestionType();
                nav.setPreviousView(Views.CREATETEST);
                if(qt == QuestionType.OPENQUESTION) nav.goToOpenQuestionView();
                else nav.goToCloseQuestionView();
            }
            case "0" -> {
                System.out.println("\n--> Operazione annullata. Ritorno alla Home...");
                nav.setPreviousView(Views.CREATETEST);
                nav.goToHomeView();
            }
            default -> {
                System.out.println("\n--> Operazione non consentita!");
                start();
            }
        }
    }

    private void saveTest(String testName, String virtualClass, String deliveryDate, String deliveryTime, String duration) {
        LocalDate date = LocalDate.parse(deliveryDate);
        LocalTime dueTime = LocalTime.parse(deliveryTime);
        long durationLong = Long.parseLong(duration);
        Duration finalDuration = Duration.ofMinutes(durationLong);

        TestBean test = new TestBean(testName, date, dueTime, finalDuration, virtualClass);
        nav.getContext().setTest(test);
    }


    public void setNavigator(NavigatorCLI nav) {
        this.nav = nav;
    }

    private void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.controller.CreateTestController;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreaTestDetailControllerCLI extends BaseControllerCLI {

    private CreateTestController createTestController = new CreateTestController();

    public void start() {
        super.clearConsole();
        showView();
        manageInput();
    }

    private void showView() {
        super.printStandardHeader("CREA TEST");
        super.printNavBar();

        System.out.println("\n------------------------------------------------------------");
        System.out.println("                     Dettagli del test                      ");
        System.out.println("------------------------------------------------------------");
        System.out.println("\n");
    }

    @Override
    public void printNavBar(){
        System.out.print("[NavBar]: Home | Classi Virtuali");
        if (this.getCurrentUserRole() == UserRole.PROFESSOR) {
            System.out.print(" | Crea test");
        }
        System.out.println("\n                                   ---------");
    }

    private List<String> loadAvailableClasses() {
        List<String> classes = new ArrayList<>();
        try {
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
                if(!saveTest(testName, virtualClass, deliveryDate, deliveryTime, duration)) nav.goToCreateTestView();
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

    private boolean saveTest(String testName, String virtualClass, String deliveryDate, String deliveryTime, String duration) {
        LocalDate date = LocalDate.parse(deliveryDate);
        LocalTime dueTime = LocalTime.parse(deliveryTime);
        long durationLong = Long.parseLong(duration);
        Duration finalDuration = Duration.ofMinutes(durationLong);

        TestBean test = new TestBean(testName, date, dueTime, finalDuration, virtualClass);

        try{
            createTestController.validateDueDate(test);
        }
        catch (ControllerException e) {
            System.err.println(e.getMessage() + ". Riprovare.");
            return false;
        }

        nav.getContext().setTest(test);
        return true;
    }
}
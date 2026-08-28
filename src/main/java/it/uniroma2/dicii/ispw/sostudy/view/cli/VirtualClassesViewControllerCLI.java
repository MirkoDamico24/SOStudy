package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;

import java.util.List;
import java.util.Scanner;

public class VirtualClassesViewControllerCLI {
    private NavigatorCLI nav;
    private final Scanner scanner = new Scanner(System.in);
    private List<VirtualClassBean> userClasses;

    public void start() {
        boolean running = true;
        while (running) {
            loadClasses();
            clearConsole();
            showView();
            running = manageInput();
        }
    }

    private void loadClasses() {
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        List<VirtualClassBean> classes = null;

        try{
            classes = ctrl.getUserClasses(nav.getSession());
        }
        catch(ControllerException e){
           System.err.println("Errore durante il caricamento delle classi disponibili");
        }
        userClasses = classes;
    }

    private void showView() {
        String username;
        UserRole role = nav.getContext().getSession().getCurrentRole();

        if (role == UserRole.PROFESSOR) {
            username = nav.getContext().getSession().getProfessor().getName() + " " + nav.getContext().getSession().getProfessor().getSurname();
        } else {
            username = nav.getContext().getSession().getStudent().getName() + " " + nav.getContext().getSession().getStudent().getSurname();
        }

        System.out.println("\n============================================================");
        System.out.printf("  SoStudy | CLASSI VIRTUALI | %s%n", username);
        System.out.println("============================================================");

        printNavBar(role);

        System.out.println("\n------------------------------------------------------------");
        System.out.println("                       Classi Virtuali                   ");
        System.out.println("------------------------------------------------------------");

        printClassesList();
        printMenu(role);
    }

    private void printNavBar(UserRole role) {
        System.out.print("[NavBar]: Home | Classi Virtuali");
        if (role == UserRole.PROFESSOR) {
            System.out.print(" | Crea test");
        }
        System.out.println("\n                -----------------");
    }

    private void printClassesList() {
        if (userClasses == null || userClasses.isEmpty()) {
            System.out.println("Nessuna classe virtuale associata.");
            return;
        }

        for (int i = 0; i < userClasses.size(); i++) {
            System.out.printf("[%d] %s%n", (i + 1), userClasses.get(i).getClassName());
        }
    }

    private void printMenu(UserRole role) {
        System.out.println("\nAzioni disponibili:");
        if (userClasses != null && !userClasses.isEmpty()) {
            System.out.println("-> Digita il numero della classe per entrarvi");
        }

        if (role == UserRole.PROFESSOR) {
            System.out.println("[C] Crea nuova classe virtuale (non implementata)");
        }

        System.out.println("[H] Torna alla Home");
        System.out.print("\nScegli un'opzione: ");
    }

    private boolean manageInput() {
        String input = scanner.nextLine().trim().toUpperCase();
        UserRole role = nav.getContext().getSession().getCurrentRole();

        if (input.equals("C")) {
            if (role == UserRole.PROFESSOR) {
                System.out.println("\n--> Navigazione verso 'Crea Nuova Classe' in corso...");
                return false;
            } else {
                System.out.println("\n--> Operazione non consentita agli studenti!");
                return true;
            }
        }else if (input.equals("H")) {
            System.out.println("\n--> Ritorno alla Home in corso...");
            nav.goToHomeView();
            return false;
        } else {
            try {
                int choice = Integer.parseInt(input);
                if (userClasses != null && choice > 0 && choice <= userClasses.size()) {
                    VirtualClassBean selectedClass = userClasses.get(choice - 1);
                    nav.setCurrentClass(selectedClass);
                    System.out.println("\n--> Ingresso nella classe '" + selectedClass.getClassName() + "' in corso...");
                    nav.setPreviousView(Views.CLASSVIEW);
                    nav.goToInsideClassView();
                    return false;
                } else {
                    System.out.println("\n--> Scelta non valida!");
                    return true;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n--> Comando non riconosciuto!");
                return true;
            }
        }
    }

    public void setNavigator(NavigatorCLI nav) {
        this.nav = nav;
    }

    private void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
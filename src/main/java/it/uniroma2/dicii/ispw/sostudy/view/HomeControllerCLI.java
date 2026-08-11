package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.model.SessionManager;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;

import java.util.Scanner;

public class HomeControllerCLI {
    private NavigatorCLI nav;

    private final Scanner scanner = new Scanner(System.in);

    private void showHomeView() {
        String username;
        if(nav.getContext().getSession().getCurrentRole() == UserRole.PROFESSOR){
            username = nav.getContext().getSession().getProfessor().getName() + " " +  nav.getContext().getSession().getProfessor().getSurname();
        }
        else username = nav.getContext().getSession().getStudent().getName() + " " +  nav.getContext().getSession().getStudent().getSurname();

        System.out.println("\n============================================================");
        System.out.printf("  SoStudy | HOME | %s%n", username);
        System.out.println("============================================================");

        printNavBar();

        System.out.println("\n------------------------------------------------------------");
        System.out.println("                 Comunicazioni in arrivo                    ");
        System.out.println("------------------------------------------------------------");

        printNotifications();

        System.out.println("------------------------------------------------------------");
        printMenu();
    }

    private void printNavBar() {
        System.out.print("[NavBar]: Home | Classi Virtuali");
        if (nav.getContext().getSession().getCurrentRole() == UserRole.PROFESSOR) {
            System.out.print(" | Crea test");
        }
        System.out.print("\n");
        System.out.print("          ----");
        System.out.println();
    }

    private void printNotifications() {
        //List<String> comunicazioni = new ArrayList<>();

        if (nav.getContext().getSession().getCurrentRole() == UserRole.STUDENT) {
            System.out.println("Implementa notifihce studente");
        } else if (nav.getContext().getSession().getCurrentRole() == UserRole.PROFESSOR) {
            System.out.println("Implementa notifihce professore");
        }

        /*for (int i = 0; i < comunicazioni.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), comunicazioni.get(i));
        }*/
    }

    private void printMenu() {
        System.out.println("\nAzioni disponibili:");
        System.out.println("[1] Vai a Classi Virtuali");

        if (nav.getContext().getSession().getCurrentRole() == UserRole.PROFESSOR) {
            System.out.println("[2] Vai a Crea test");
        }

        System.out.println("[0] Logout (Esci)");
        System.out.print("\nScegli un'opzione: ");
    }

    private boolean manageInput() {
        String input = scanner.nextLine().trim();

        return switch (input) {
            case "1" -> {
                System.out.println("\n--> Navigazione verso 'Classi Virtuali' in corso...");
                nav.goToClassesView();
                yield true;
            }
            case "2" -> {
                System.out.println("\n--> Navigazione verso 'Crea test' in corso...");
                nav.goToCreateTestView();
                yield true;
            }
            case "0" -> {
                System.out.println("\n--> Logout in corso. Arrivederci, " + "Mario" + "!");
                SessionManager.getInstance().deleteSession(nav.getContext().getSession().getSessionID());
                nav.goToLoginView();
                yield false;
            }
            default -> {
                System.out.println("\n--> Operazione non consentita!");
                yield false;
            }
        };
    }

    public void start() {
        boolean running = true;
        while (running) {
            clearConsole();
            showHomeView();
            running = manageInput();
        }
    }

    public void setNavigator(NavigatorCLI nav) { this.nav = nav;}

    private void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

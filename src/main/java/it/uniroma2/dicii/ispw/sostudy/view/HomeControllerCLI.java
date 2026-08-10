package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeControllerCLI {
    private NavigatorCLI nav;

    private final Scanner scanner = new Scanner(System.in);

    private void showHomeView() {
        System.out.println("\n============================================================");
        System.out.printf("  SoStudy | HOME | %s\n", "Mario");
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
        List<String> comunicazioni = new ArrayList<>();

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
                yield true;
            }
            case "2" -> {
                System.out.println("\n--> Navigazione verso 'Crea test' in corso...");
                yield true;
            }
            case "0" -> {
                System.out.println("\n--> Logout in corso. Arrivederci, " + "Mario" + "!");
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
            showHomeView();
            running = manageInput();
        }
    }

    public void setNavigator(NavigatorCLI nav) { this.nav = nav;}
}

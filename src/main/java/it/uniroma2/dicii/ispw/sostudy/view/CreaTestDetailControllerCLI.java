package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;

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

    private void manageInput() {
        System.out.print("Nome test (Inserire un nome per il test): ");
        String testName = scanner.nextLine().trim();

        System.out.print("Assegna a (Selezionare una classe virtuale): ");
        String virtualClass = scanner.nextLine().trim();

        System.out.print("Data di consegna (Selezionare una data di consegna): ");
        String deliveryDate = scanner.nextLine().trim();

        System.out.print("Orario di consegna (Selezionare un orario di consegna): ");
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
                nav.goToHomeView();
            }
            case "0" -> {
                System.out.println("\n--> Operazione annullata. Ritorno alla Home...");
                nav.goToHomeView();
            }
            default -> {
                System.out.println("\n--> Operazione non consentita!");
                start();
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

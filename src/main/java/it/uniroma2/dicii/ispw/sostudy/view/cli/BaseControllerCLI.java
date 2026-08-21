package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;

import java.util.Scanner;

public abstract class BaseControllerCLI {
    protected NavigatorCLI nav;
    protected final Scanner scanner = new Scanner(System.in);

    public void setNavigator(NavigatorCLI nav) {
        this.nav = nav;
    }

    protected void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    protected String getFormattedUsername() {
        if (nav == null || nav.getContext() == null || nav.getContext().getSession() == null) return "";

        UserRole role = nav.getContext().getSession().getCurrentRole();
        if (role == UserRole.PROFESSOR) {
            return nav.getContext().getSession().getProfessor().getName() + " " + nav.getContext().getSession().getProfessor().getSurname();
        } else {
            return nav.getContext().getSession().getStudent().getName() + " " + nav.getContext().getSession().getStudent().getSurname();
        }
    }

    protected void printStandardHeader(String sectionName) {
        System.out.println("\n============================================================");
        System.out.printf("  SoStudy | %s | %s%n", sectionName.toUpperCase(), getFormattedUsername());
        System.out.println("============================================================");
    }

    protected void printNavBar() {
        System.out.print("[NavBar]: Home | Classi Virtuali");
        if (nav != null && nav.getContext().getSession().getCurrentRole() == UserRole.PROFESSOR) {
            System.out.print(" | Crea test");
        }
        System.out.println("\n                                   ---------");
    }
}
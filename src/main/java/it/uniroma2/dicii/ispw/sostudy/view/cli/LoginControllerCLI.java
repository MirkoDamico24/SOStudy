package it.uniroma2.dicii.ispw.sostudy.view.cli;

import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.controller.LoginController;
import it.uniroma2.dicii.ispw.sostudy.exception.InvalidCredentialException;

import java.io.Console;

public class LoginControllerCLI extends BaseControllerCLI {

    private void showBanner() {
        System.out.println("========================================");
        System.out.println("          BENVENUTO IN SOSTUDY          ");
        System.out.println("========================================");
        System.out.println("Inserisci le tue credenziali per accedere.\n");
    }

    public void handleLogin() {
        LoginController login = new LoginController();
        clearConsole();
        showBanner();

        String email;
        String password;

        System.out.print("Email: ");
        email = scanner.nextLine().trim();

        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword("Password: ");
            password = new String(passwordArray);
        } else {
            System.out.print("Password: ");
            password = scanner.nextLine();
        }

        UserBean ub = new UserBean(email, password);
        SessionBean currentSession = null;

        try {
            currentSession = login.authenticate(ub);
        } catch(InvalidCredentialException e) {
            System.out.println("Email o password errate");
        }

        nav.setSession(currentSession);
        nav.goToHomeView();
    }
}
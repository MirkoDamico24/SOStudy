package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.controller.LoginController;
import it.uniroma2.dicii.ispw.sostudy.exception.InvalidCredentialException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorCLI;

import java.io.Console;
import java.util.Scanner;

public class LoginControllerCLI {
    private NavigatorCLI nav;

    private void showBanner() {
        System.out.println("========================================");
        System.out.println("          BENVENUTO IN SOSTUDY          ");
        System.out.println("========================================");
        System.out.println("Inserisci le tue credenziali per accedere.\n");
    }

    public void handleLogin() {
        LoginController login = new LoginController();

        showBanner();
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";

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

        try{
            currentSession = login.authenticate(ub);
        }
        catch(InvalidCredentialException e){
            System.out.println("Email o password errate");
        }

        nav.setSession(currentSession);
        nav.goToHomeView();

    }

    public void setNavigator(NavigatorCLI nav) { this.nav = nav; }
}

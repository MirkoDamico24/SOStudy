package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.controller.LoginController;
import it.uniroma2.dicii.ispw.sostudy.exception.InvalidCredentialException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControllerGUI extends BaseControllerGUI {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private static final String title = "Errore";
    private static final String header = "Credenziali non valide";

    @FXML
    void handleLoginAction(ActionEvent event) {
        LoginController lc = new LoginController();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            super.showAlert(title, header, "Entrambi i campi devono essere riempiti");
            return;
        }

        UserBean ub = new UserBean(email, password);
        SessionBean currentSession;

        try {
            currentSession = lc.authenticate(ub);
        } catch(InvalidCredentialException e) {
            super.showAlert(title, header, "Email o password errati");
            return;
        }

        navigatorGUI.setSession(currentSession);
        navigatorGUI.setPreviousView(Views.LOGIN);
        navigatorGUI.goToHomeView();
    }

    public void prepare(){
        emailField.clear();
        passwordField.clear();
    }
}
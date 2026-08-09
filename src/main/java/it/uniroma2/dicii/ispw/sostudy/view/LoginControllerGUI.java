package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControllerGUI {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    void handleLoginAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Inserisci qui la logica di autenticazione
        System.out.println("Tentativo di login con Email: " + email);
    }
}

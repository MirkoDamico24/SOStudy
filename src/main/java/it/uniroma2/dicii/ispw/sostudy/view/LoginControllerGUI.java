package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.controller.LoginController;
import it.uniroma2.dicii.ispw.sostudy.exception.InvalidCredentialException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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

    private NavigatorGUI navigatorGUI;
    private Parent logView;

    private String title = "Errore";
    private String header = "Credenziali non valide";

    @FXML
    void handleLoginAction(ActionEvent event) {
        LoginController lc = new LoginController();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(title, header, "Entrambi i campi devono essere riempiti");
        }

        UserBean ub = new UserBean(email, password);
        SessionBean currentSession;

        try {
            currentSession = lc.authenticate(ub);
        }
        catch(InvalidCredentialException e){
            showAlert(title, header, "Email o password errati");
            return;
        }

        navigatorGUI.setSession(currentSession);
        navigatorGUI.setPreviousView(Views.LOGIN);
        navigatorGUI.goToHomeView();

    }

    private void showAlert(String title, String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) { this.navigatorGUI = navigatorGUI; }
    public void setView(Parent logView) { this.logView = logView; }
    public Parent getView() { return logView; }

    public void prepare(){
        emailField.clear();
        passwordField.clear();
    }
}

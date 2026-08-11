package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.model.SessionManager;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class HomeControllerGUI {

    @FXML
    private Button btnHome;
    @FXML
    private Button btnCreaTest;
    @FXML
    private Button btnClassiVirtuali;
    @FXML
    private Button btnLogout;

    @FXML
    private Label userNameLabel;
    @FXML
    private ImageView userAvatar;
    @FXML
    private VBox listaComunicazioni;

    private NavigatorGUI navigatorGUI;
    private Parent view;

    public void prepare(boolean isProfessor){
        configureViewByRole(isProfessor);
        setUsernameBundle(isProfessor);
    }

    public void configureViewByRole(boolean isProfessore) {
        if (!isProfessore) {
            btnCreaTest.setVisible(false);
            btnCreaTest.setManaged(false);
        }
    }


    public void setUsernameBundle(boolean isProfessor) {
        String username;
        if (isProfessor) {
            username = navigatorGUI.getContext().getSession().getProfessor().getName() + " " + navigatorGUI.getContext().getSession().getProfessor().getSurname();
        }
        else username = navigatorGUI.getContext().getSession().getStudent().getName() + " " + navigatorGUI.getContext().getSession().getStudent().getSurname();

        userNameLabel.setText(username);
    }

    /**
     * Popola la lista centrale con le comunicazioni o le notifiche dei test.
     */
    public void popolaComunicazioni(List<String> notifiche) {
        listaComunicazioni.getChildren().clear();

        for (int i = 0; i < notifiche.size(); i++) {
            Label notificaLabel = new Label(notifiche.get(i));
            notificaLabel.setFont(new Font("Serif", 26));
            // Uso setStyle per forzare la priorità del colore grigio scuro
            notificaLabel.setStyle("-fx-text-fill: #555555;");
            notificaLabel.setMaxWidth(Double.MAX_VALUE);
            notificaLabel.setPadding(new Insets(15, 0, 15, 0));

            listaComunicazioni.getChildren().add(notificaLabel);

            if (i < notifiche.size() - 1) {
                Separator separator = new Separator();
                listaComunicazioni.getChildren().add(separator);
            }
        }
    }

    // --- AZIONI DEI BOTTONI ---

    @FXML
    void handleNavCreaTest(ActionEvent event) {
        navigatorGUI.goToCreateTestView();
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        navigatorGUI.goToClassesView();
    }

    @FXML
    void handleLogout(ActionEvent event) {
        // Logica per chiudere la sessione e tornare al login
        SessionManager.getInstance().deleteSession(navigatorGUI.getContext().getSession().getSessionID());
        navigatorGUI.goToLoginView();
    }

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) { this.navigatorGUI = navigatorGUI; }
    public void setView(Parent view) { this.view = view; }
    public NavigatorGUI getNavigatorGUI() { return this.navigatorGUI; }
    public Parent getView() { return this.view; }
}

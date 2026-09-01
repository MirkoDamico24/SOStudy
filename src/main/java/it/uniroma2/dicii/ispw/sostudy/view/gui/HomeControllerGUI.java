package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.*;
import it.uniroma2.dicii.ispw.sostudy.controller.NotificationController;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.eng.observer.MessageObserver;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.MessageType;
import it.uniroma2.dicii.ispw.sostudy.model.SessionManager;
import it.uniroma2.dicii.ispw.sostudy.model.User;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class HomeControllerGUI extends BaseControllerGUI implements MessageObserver {
    @FXML
    private Button btnCreaTest;
    @FXML
    private Label userNameLabel;
    @FXML
    private VBox listaComunicazioni;

    private NotificationController nctrl = new NotificationController();

    public void prepare(){
        configureViewByRole(getNavigatorGUI().getCurrentUserRole() == UserRole.PROFESSOR);
        setUsernameBundle();

        nctrl.registerAsNotificationObserver(this, getNavigatorGUI().getSession());

        List<MessageBean> messages = nctrl.fetchUserNotifications(getNavigatorGUI().getCorrectUserBean(), getNavigatorGUI().getSession());
        populateNotificationSection(messages);
    }

    public void configureViewByRole(boolean isProfessor) {
        if (!isProfessor) {
            btnCreaTest.setVisible(false);
            btnCreaTest.setManaged(false);
        } else {
            btnCreaTest.setVisible(true);
            btnCreaTest.setManaged(true);
        }
    }

    public void setUsernameBundle() {
        userNameLabel.setText(getFormattedUsername());
    }

    public void populateNotificationSection(List<MessageBean> msg) {
        listaComunicazioni.getChildren().clear();

        for (int i = 0; i < msg.size(); i++) {
            MessageBean currentMessage = msg.get(i);
            Label msgLabel = new Label(currentMessage.getMessage());
            msgLabel.setFont(new Font("Serif", 26));
            msgLabel.setStyle("-fx-text-fill: #555555;");
            msgLabel.setMaxWidth(Double.MAX_VALUE);
            msgLabel.setPadding(new Insets(15, 0, 15, 0));

            listaComunicazioni.getChildren().add(msgLabel);

            if (i < msg.size() - 1) {
                Separator separator = new Separator();
                listaComunicazioni.getChildren().add(separator);
            }

            if (getNavigatorGUI().getCurrentUserRole() == UserRole.STUDENT && currentMessage.getType() == MessageType.GRADENOTIFICATION && !currentMessage.isRead()) {
                Platform.runLater(() ->
                        showAlert("Proposta di voto disponibile", "Recarsi nella classe interessata e cliccare sul test indicato per accettare o rifiutare la proposta di voto",
                                currentMessage.getMessage())
                );
            }
        }
    }

    @FXML
    void handleNavCreaTest(ActionEvent event) {
        nctrl.detachFromObserved(this, navigatorGUI.getSession());
        navigatorGUI.setPreviousView(Views.HOME);
        navigatorGUI.goToCreateTestView();
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        nctrl.detachFromObserved(this, navigatorGUI.getSession());
        navigatorGUI.setPreviousView(Views.HOME);
        navigatorGUI.goToClassesView();
    }

    @FXML
    void handleLogout(ActionEvent event) {
        nctrl.detachFromObserved(this, navigatorGUI.getSession());
        SessionManager.getInstance().deleteSession(navigatorGUI.getContext().getSession().getSessionID());
        navigatorGUI.setPreviousView(Views.HOME);
        navigatorGUI.goToLoginView();
    }

    @Override
    public void update() {
        UserBean ub = navigatorGUI.getCorrectUserBean();

        List<MessageBean> messages = null;
        try {
            messages = nctrl.fetchUserNotifications(ub, getNavigatorGUI().getSession());
        } catch(ControllerException e) {
            Platform.runLater(() ->
                    showAlert("Errore", "Errore durante il caricamento delle notifiche", "Riavviare l'applicazione.")
            );
            return;
        }

        final List<MessageBean> finalMessages = (messages == null) ? new ArrayList<>() : messages;

        Platform.runLater(() ->
                populateNotificationSection(finalMessages)
        );
    }
}
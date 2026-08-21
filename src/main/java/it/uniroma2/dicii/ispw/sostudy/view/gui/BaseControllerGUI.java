package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

public abstract class BaseControllerGUI {
    protected NavigatorGUI navigatorGUI;
    protected Parent view;

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) {
        this.navigatorGUI = navigatorGUI;
    }

    public NavigatorGUI getNavigatorGUI() {
        return navigatorGUI;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public Parent getView() {
        return view;
    }

    protected void showAlert(String title, String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    protected String getFormattedUsername() {
        if (navigatorGUI == null || navigatorGUI.getContext() == null || navigatorGUI.getContext().getSession() == null) {
            return "Unavailable";
        }

        UserRole role = navigatorGUI.getContext().getSession().getCurrentRole();
        if (role == UserRole.PROFESSOR && navigatorGUI.getContext().getSession().getProfessor() != null) {
            return navigatorGUI.getContext().getSession().getProfessor().getName() + " " + navigatorGUI.getContext().getSession().getProfessor().getSurname();
        } else if (role == UserRole.STUDENT && navigatorGUI.getContext().getSession().getStudent() != null) {
            return navigatorGUI.getContext().getSession().getStudent().getName() + " " + navigatorGUI.getContext().getSession().getStudent().getSurname();
        }

        return "Unavailable";
    }
}
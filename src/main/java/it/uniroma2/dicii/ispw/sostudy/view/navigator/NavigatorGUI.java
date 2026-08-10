package it.uniroma2.dicii.ispw.sostudy.view.navigator;


import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.view.HomeControllerGUI;
import it.uniroma2.dicii.ispw.sostudy.view.LoginControllerGUI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigatorGUI extends Navigator{
    private Stage stage;
    private LoginControllerGUI login;
    private HomeControllerGUI home;

    public NavigatorGUI(){
        super();
        /*this.stage = new Stage();
        stage.setTitle("SoStudy");
        this.stage.setMinWidth(600);
        this.stage.setMinHeight(400);*/
    }

    private void showAlert(String title, String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void buildView(Parent root)
    {
        if(root == null) return;
        if(this.stage.getScene() == null){
            this.stage.setScene(new Scene(root));
        }
        else this.stage.getScene().setRoot(root);

        this.stage.sizeToScene();

        if(!this.stage.isShowing()){
            this.stage.centerOnScreen();
            this.stage.show();
        }
    }

    @Override
    public void startup(){
        Platform.startup(() -> {
            this.stage = new Stage();
            this.stage.setTitle("SoStudy");
            this.stage.setMinWidth(1920);
            this.stage.setMinHeight(1080);
            goToLoginView();
        });
    }

    @Override
    public void createLoginView(){
        try {
            if(this.login == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/Login.fxml"));
                Parent root = fxmlLoader.load();
                this.login = fxmlLoader.getController();
                this.login.setView(root);
                this.login.setNavigatorGUI(this);
            }
            this.login.prepare();
            buildView(this.login.getView());
        }
        catch(IOException e){
            showAlert("Errore grafico", "Risorse non disponibili", "Non è stato possibile trovare il file di configurazione della login view");
        }

    }

    @Override
    public void createHomeView(){
        try {
            if(this.home == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/Home.fxml"));
                Parent root = fxmlLoader.load();
                this.home = fxmlLoader.getController();
                this.home.setView(root);
                this.home.setNavigatorGUI(this);
            }
            this.home.configureViewByRole(getContext().getSession().getCurrentRole() == UserRole.PROFESSOR);
            buildView(this.home.getView());
        }
        catch(IOException e){
            showAlert("Errore grafico", "Risorse non disponibili", "Non è stato possibile trovare il file di configurazione della login view");
        }

    }
}

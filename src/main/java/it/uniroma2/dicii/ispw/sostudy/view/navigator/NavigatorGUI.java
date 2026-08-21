package it.uniroma2.dicii.ispw.sostudy.view.navigator;


import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.gui.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class NavigatorGUI extends Navigator{
    private Stage stage;
    private LoginControllerGUI login;
    private HomeControllerGUI home;
    private CreaTestDetailControllerGUI createTest;
    private VirtualClassesViewControllerGUI virtualClasses;
    private CreaDomandaApertaControllerGUI openQuestion;
    private CreaDomandaMultiplaControllerGUI closeQuestion;
    private RiepilogoTestControllerGUI testRecap;

    private static final String TITLEERROR = "Errore grafico";
    private static final String MESSAGE = "Risorse non disponibili";

    public NavigatorGUI(){
        super();
    }

    private void showAlert(String title, String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static QuestionType showPopUp(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Scelta tipo domanda");
        alert.setHeaderText("Tipo di domanda");
        alert.setContentText("Seleziona il tipo di domanda da aggiungere al test");

        ButtonType btnOpenQuestion = new ButtonType("A risposta aperta");
        ButtonType btnMultipleChoice = new ButtonType("A risposta multipla");
        ButtonType btnCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnOpenQuestion, btnMultipleChoice, btnCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnOpenQuestion) {
                return QuestionType.OPENQUESTION;

            } else if (result.get() == btnMultipleChoice) {
                return QuestionType.CLOSEQUESTION;
            }
        }
        return null;
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
            showAlert(TITLEERROR, MESSAGE, "Non è stato possibile trovare il file di configurazione della login view");
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
            this.home.prepare(getContext().getSession().getCurrentRole() == UserRole.PROFESSOR);
            buildView(this.home.getView());
        }
        catch(IOException e){
            showAlert(TITLEERROR, MESSAGE, "Non è stato possibile trovare il file di configurazione della home view");
        }

    }

    @Override
    public void creatTestView(){
        try{
            if(this.createTest == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/CreaTestView.fxml"));
                Parent root = fxmlLoader.load();
                this.createTest = fxmlLoader.getController();
                this.createTest.setView(root);
                this.createTest.setNavigatorGUI(this);
            }
            this.createTest.prepare();
            buildView(this.createTest.getView());
        }
        catch(IOException e) {
            showAlert(TITLEERROR, MESSAGE, "Non è stato possibile trovare il file di configurazione della test view");
        }
    }

    @Override
    public void createClassesView(){
        try{
            if(this.virtualClasses == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/VirtualClassesView.fxml"));
                Parent root = fxmlLoader.load();
                this.virtualClasses = fxmlLoader.getController();
                this.virtualClasses.setView(root);
                this.virtualClasses.setNavigatorGUI(this);
            }
            this.virtualClasses.prepare();
            buildView(this.virtualClasses.getView());
        }
        catch(IOException e) {
            showAlert(TITLEERROR, MESSAGE, "Non è stato possibile trovare il file di configurazione della virtual class view");
        }
    }

    @Override
    public void createOpenQuestionView(){
        try{
            if(this.openQuestion == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/CreaDomandaAperta.fxml"));
                Parent root = fxmlLoader.load();
                this.openQuestion = fxmlLoader.getController();
                this.openQuestion.setView(root);
                this.openQuestion.setNavigatorGUI(this);
            }
            this.openQuestion.prepare();
            buildView(this.openQuestion.getView());
        }
        catch(IOException e) {
            showAlert(TITLEERROR, MESSAGE, "Non è stato possibile trovare il file di configurazione della open question creation view");
        }
    }

    @Override
    public void createCloseQuestionView(){
        try{
            if(this.closeQuestion == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/CreaDomandaMultipla.fxml"));
                Parent root = fxmlLoader.load();
                this.closeQuestion = fxmlLoader.getController();
                this.closeQuestion.setView(root);
                this.closeQuestion.setNavigatorGUI(this);
            }
            this.closeQuestion.prepare();
            buildView(this.closeQuestion.getView());
        }
        catch(IOException e) {
            showAlert(TITLEERROR, MESSAGE, "Non è stato possibile trovare il file di configurazione della close question creation view");
        }
    }

    @Override
    public void createRecapView(){
        try{
            if(this.testRecap == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/RiepilogoTest.fxml"));
                Parent root = fxmlLoader.load();
                this.testRecap = fxmlLoader.getController();
                this.testRecap.setView(root);
                this.testRecap.setNavigatorGUI(this);
            }
            this.testRecap.prepare();
            buildView(this.testRecap.getView());
        }
        catch(IOException e) {
            showAlert(TITLEERROR, MESSAGE, "Non è stato possibile trovare il file di configurazione della test recap view");
        }
    }
}

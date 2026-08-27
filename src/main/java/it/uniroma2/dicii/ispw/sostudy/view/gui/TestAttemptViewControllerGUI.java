package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AttemptBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.SessionManager;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class TestAttemptViewControllerGUI extends BaseControllerGUI{
    @FXML
    private Button btnClassiVirtuali;

    @FXML
    private Label userNameLabel;

    @FXML
    private VBox listaAttempt;

    private static final String SERIF = "Serif";
    private static final String COLOR = "-fx-text-fill: #555555;";


    public void prepare(){
        setUsernameBundle();

        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        List<AttemptBean> attempts = null;

        try{
            attempts = ctrl.loadTestAttempts(getNavigatorGUI().getSession(), getNavigatorGUI().getCurrentTest());
        }
        catch(ControllerException e){
            showAlert("Errore", "Errore durante il caricamento dei tentativi di test", "Riprovare");
            return;
        }

        getNavigatorGUI().setAttempts(attempts);
        populateAttemptSection(attempts);
    }

    public void setUsernameBundle() {
        userNameLabel.setText(getFormattedUsername());
        btnClassiVirtuali.setText(getNavigatorGUI().getCurrentClass().getClassName());
    }

    public void populateAttemptSection(List<AttemptBean> attempt) {
        listaAttempt.getChildren().clear();

        if(attempt == null){
            Label testLabel = new Label("Nessuno studente ha ancora svolto il test!");
            testLabel.setFont(new Font(SERIF, 26));
            testLabel.setStyle(COLOR);
            testLabel.setMaxWidth(Double.MAX_VALUE);
            testLabel.setPadding(new Insets(15, 0, 15, 0));
            listaAttempt.getChildren().add(testLabel);
        }
        else if(attempt.isEmpty()){
            Label testLabel = new Label("Non ci sono test da corregere!");
            testLabel.setFont(new Font(SERIF, 26));
            testLabel.setStyle(COLOR);
            testLabel.setMaxWidth(Double.MAX_VALUE);
            testLabel.setPadding(new Insets(15, 0, 15, 0));
            listaAttempt.getChildren().add(testLabel);
        }
        else{
            for (int i = 0; i < attempt.size(); i++) {
                Label testLabel = new Label("Test di: " + attempt.get(i).getStudent().getName() + " " + attempt.get(i).getStudent().getSurname());
                testLabel.setFont(new Font(SERIF, 26));
                testLabel.setStyle(COLOR);
                testLabel.setMaxWidth(Double.MAX_VALUE);
                testLabel.setPadding(new Insets(15, 0, 15, 0));

                AttemptBean selected =  attempt.get(i);

                testLabel.setOnMouseClicked(event ->{
                    showAlert("Test", "Verrà avviata la valutazione del test", "Premere 'OK' per iniziare");
                    getNavigatorGUI().setCurrentAttempt(selected);
                    getNavigatorGUI().setPreviousView(Views.TESTATTEMPTVIEW);
                    getNavigatorGUI().goToEvaluateOpenAnswerView();
                });

                listaAttempt.getChildren().add(testLabel);

                if (i < attempt.size() - 1) {
                    Separator separator = new Separator();
                    listaAttempt.getChildren().add(separator);
                }
            }
        }
    }

    @FXML
    void handleNavCreaTest(ActionEvent event) {
        getNavigatorGUI().setCurrentTest(null);
        navigatorGUI.setPreviousView(Views.TESTATTEMPTVIEW);
        navigatorGUI.goToCreateTestView();
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        getNavigatorGUI().setCurrentTest(null);
        navigatorGUI.setPreviousView(Views.TESTATTEMPTVIEW);
        navigatorGUI.goToClassesView();
    }

    @FXML
    void handleLogout(ActionEvent event) {
        getNavigatorGUI().setSession(null);
        getNavigatorGUI().setContext(null);
        SessionManager.getInstance().deleteSession(navigatorGUI.getSession().getSessionID());
        navigatorGUI.setPreviousView(Views.TESTATTEMPTVIEW);
        navigatorGUI.goToLoginView();
    }
}

package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;

import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;
import javafx.scene.control.ScrollPane;

public class InsideClassViewControllerGUI extends BaseControllerGUI{
    @FXML private Button btnCreaTest;
    @FXML private Button btnNomeClasseNav;

    @FXML private Label userNameLabel;

    @FXML private Button btnInvitaStudente;
    @FXML private ScrollPane testListScrollPane;
    @FXML private VBox listaTestVBox;

    @FXML
    public void prepare() {
        configureViewByRole(getNavigatorGUI().getCurrentUserRole() == UserRole.PROFESSOR);
        setUsernameBundle();
        configureScrollPane();

        btnNomeClasseNav.setText(getNavigatorGUI().getCurrentClass().getClassName());

        List<TestBean> availableTests = getNavigatorGUI().getCurrentClass().getTest();
        populateTests(availableTests);
    }

    private void configureScrollPane() {
        testListScrollPane.setFitToWidth(true);
        testListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        testListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        testListScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    }

    private void configureViewByRole(boolean isProfessore) {
        if (!isProfessore) {
            btnCreaTest.setVisible(false);
            btnCreaTest.setManaged(false);

            btnInvitaStudente.setVisible(false);
            btnInvitaStudente.setManaged(false);
        }
    }

    public void setUsernameBundle() {
        userNameLabel.setText(getFormattedUsername());
    }

    public void populateTests(List<TestBean> tests) {
        listaTestVBox.getChildren().clear();

        for (TestBean test : tests) {
            VBox testItemBox = new VBox(10);

            Label lblTest = new Label(test.getName());
            lblTest.setFont(new Font("Serif", 28));
            lblTest.setStyle("-fx-text-fill: #555555; -fx-cursor: hand;");

            lblTest.setOnMouseClicked(event ->{
                switch(getNavigatorGUI().getCurrentUserRole()) {
                    case UserRole.STUDENT -> {
                        showAlert("Test", "Il tentativo sarà avviato", "Premere 'Ok' per iniziare");
                        Views next = getNextView(test);
                        switch (next) {
                            case OPENANSWERVIEW -> getNavigatorGUI().goToOpenAnswerView();
                            case CLOSEANSWERVIEW -> getNavigatorGUI().goToCloseAnswerView();
                            default -> showAlert("Errore", "Tipo di domanda successivo non supportato dalla UI", "");
                        }
                    }

                    case UserRole.PROFESSOR -> {
                        showAlert("Test", "", "Premere 'Ok' per iniziare");
                        getNavigatorGUI().setCurrentTest(test);
                        getNavigatorGUI().goToTestAttemptView();
                    }
                }
            });

            Region underline = new Region();
            underline.setPrefHeight(1.5);
            underline.setMinHeight(1.5);
            underline.setStyle("-fx-background-color: #CCCCCC;");

            testItemBox.getChildren().addAll(lblTest, underline);
            testItemBox.setPadding(new Insets(10, 0, 10, 0));

            listaTestVBox.getChildren().add(testItemBox);
        }
    }

    private Views getNextView(TestBean test) {
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        List<QuestionBean> questions = null;

        try{
            questions = ctrl.loadRequiredTest(getNavigatorGUI().getSession(), test);
        }
        catch(ControllerException e){
            showAlert("Errore", e.getMessage(), "");
        }

        getNavigatorGUI().setQuestions(questions);
        getNavigatorGUI().setCurrentTest(test);

        if(questions.getFirst().isOpenQuestion()) return Views.OPENANSWERVIEW;
        else return Views.CLOSEANSWERVIEW;
    }

    @FXML
    void handleNavHome(ActionEvent event) {
        getNavigatorGUI().setPreviousView(Views.INSIDECLASSVIEW);
        getNavigatorGUI().goToHomeView();
    }

    @FXML
    void handleNavCreaTest(ActionEvent event) {
        getNavigatorGUI().setPreviousView(Views.INSIDECLASSVIEW);
        getNavigatorGUI().goToCreateTestView();
    }

    @FXML
    void handleInvitaStudente(ActionEvent event) {
            //yet to implement
    }
}

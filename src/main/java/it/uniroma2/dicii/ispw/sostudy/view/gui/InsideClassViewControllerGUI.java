package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.controller.TakeTestController;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class InsideClassViewControllerGUI extends BaseControllerGUI {
    @FXML private Button btnCreaTest;
    @FXML private Button btnNomeClasseNav;

    @FXML private Label userNameLabel;

    @FXML private Button btnInvitaStudente;
    @FXML private ScrollPane testListScrollPane;
    @FXML private VBox activeTestsVBox;
    @FXML private VBox expiredTestsVBox;

    private static final String ERROR = "Errore";

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
        } else {
            btnCreaTest.setVisible(true);
            btnCreaTest.setManaged(true);

            btnInvitaStudente.setVisible(true);
            btnInvitaStudente.setManaged(true);
        }
    }

    public void setUsernameBundle() {
        userNameLabel.setText(getFormattedUsername());
    }

    public void populateTests(List<TestBean> tests) {
        activeTestsVBox.getChildren().clear();
        expiredTestsVBox.getChildren().clear();

        if (tests == null) {
            return;
        }

        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        for (TestBean test : tests) {
            VBox targetContainer = (test.getDueDate() != null && test.getDueDate().isBefore(today))
                    ? expiredTestsVBox
                    : activeTestsVBox;

            VBox testItemBox = new VBox(10);

            Label lblTest = new Label(test.getName());
            lblTest.setFont(new Font("Serif", 28));
            lblTest.setStyle("-fx-text-fill: #555555; -fx-cursor: hand;");

            lblTest.setOnMouseClicked(event -> {
                switch (getNavigatorGUI().getCurrentUserRole()) {
                    case UserRole.STUDENT -> {
                        Views next = getNextView(test);
                        switch (next) {
                            case OPENANSWERVIEW -> {
                                showAlert("Test", "Il tentativo sarà avviato", "Premere 'Ok' per iniziare");
                                getNavigatorGUI().goToOpenAnswerView();
                            }
                            case CLOSEANSWERVIEW -> {
                                showAlert("Test", "Il tentativo sarà avviato", "Premere 'Ok' per iniziare");
                                getNavigatorGUI().goToCloseAnswerView();
                            }
                            case INSIDECLASSVIEW -> getNavigatorGUI().goToInsideClassView();
                            default -> showAlert(ERROR, "Tipo di domanda successivo non supportato dalla UI", "");
                        }
                    }

                    case UserRole.PROFESSOR -> {
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

            targetContainer.getChildren().add(testItemBox);
        }
    }

    private Views getNextView(TestBean test) {
        TakeTestController ctrl = new TakeTestController();
        List<QuestionBean> questions = null;

        try {
            questions = ctrl.loadRequiredTest(getNavigatorGUI().getSession(), test);
        } catch (ControllerException e) {
            showAlert(ERROR, e.getMessage(), "");
        }

        if (questions == null || questions.isEmpty()) {
            checkSelectedTest(test);
            return Views.INSIDECLASSVIEW;
        }

        getNavigatorGUI().setQuestions(questions);
        getNavigatorGUI().setCurrentTest(test);

        if (questions.getFirst().isOpenQuestion()) return Views.OPENANSWERVIEW;
        else return Views.CLOSEANSWERVIEW;
    }

    private void checkSelectedTest(TestBean test) {
        KnowledgeEvaluationController kctrl = new KnowledgeEvaluationController();
        Integer toAccept = kctrl.checkGradeToAccept(getNavigatorGUI().getSession(), test);
        if(toAccept != null){
            boolean accepted = showPopUp(toAccept);
            try {
                kctrl.acceptGrade(getNavigatorGUI().getSession(), accepted);
            }
            catch (ControllerException e) {
                showAlert(ERROR, e.getMessage(), "");
            }
        }
        else {
            showAlert(ERROR, "Il test selezionato è già stato svolto. Non è possibile svolgere più di un tentativo.", "Selezionare un altro test");
        }
    }

    private boolean showPopUp(Integer grade){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma proposta di voto");
        alert.setHeaderText("Il voto proposto è: " + grade);
        alert.setContentText("Selezionare accetta per confermare e registrare il voto, rifiuta per richiedere una revisione della valutazione");

        ButtonType btnAccept = new ButtonType("Accetta voto");
        ButtonType btnReject = new ButtonType("Rifiuta voto");

        alert.getButtonTypes().setAll(btnAccept, btnReject);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnAccept) {
                return true;

            } else if (result.get() == btnReject) {
                return false;
            }
        }
        return false;
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
    public void handleGoBack(){
        getNavigatorGUI().setPreviousView(Views.INSIDECLASSVIEW);
        getNavigatorGUI().goToClassesView();
    }

    @FXML
    void handleInvitaStudente(ActionEvent event) {
        //yet to implement
    }
}
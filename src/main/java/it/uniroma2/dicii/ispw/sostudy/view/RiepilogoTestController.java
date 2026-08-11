package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.CreateTestController;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

public class RiepilogoTestController {
    @FXML private Label lblNomeTest;
    @FXML private Label lblClasse;
    @FXML private Label lblData;
    @FXML private Label lblOra;
    @FXML private Label lblDurata;

    // --- Box Centrale ---
    @FXML private Label lblTotaleDomande;
    @FXML private Label lblPunteggioTotale;
    @FXML private VBox listaDomandeVBox;
    @FXML private Label profNameLabel;

    private NavigatorGUI navigatorGUI;
    private Parent view;

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) {
        this.navigatorGUI = navigatorGUI;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public NavigatorGUI getNavigatorGUI() {
        return navigatorGUI;
    }

    public Parent getView() {
        return view;
    }

    public void prepare() {
        setUsernameBundle();

        TestBean currentTest = navigatorGUI.getContext().getTest();
        if (currentTest != null) {
            setTestDetails(
                    currentTest.getName(),
                    currentTest.getVirtualClass(),
                    currentTest.getDueDate().toString(),
                    currentTest.getDueTime().toString(),
                    String.valueOf(currentTest.getDuration().toMinutes())
            );
        }

        List<QuestionBean> questions = navigatorGUI.getContext().getQuestions();
        populateQuestionList(questions);
    }

    public void setUsernameBundle() {
        String username = navigatorGUI.getContext().getSession().getProfessor().getName() + " " + navigatorGUI.getContext().getSession().getProfessor().getSurname();
        profNameLabel.setText(username);
    }

    public void setTestDetails(String name, String virtualClass, String date, String time, String duration) {
        lblNomeTest.setText("Nome test: " + name);
        lblClasse.setText("Assegnato a: " + virtualClass);
        lblData.setText("Data di consegna: " + date);
        lblOra.setText("Ora di consegna: " + time);
        lblDurata.setText("Durata test: " + duration + " minutes");
    }

    public void populateQuestionList(List<QuestionBean> questions) {
        listaDomandeVBox.getChildren().clear();

        if (questions == null || questions.isEmpty()) {
            updateTotals();
            return;
        }

        int index = 1;
        for (QuestionBean question : questions) {
            addQuestionRowUI(index, question);
            index++;
        }
        updateTotals();
    }

    private void addQuestionRowUI(int index, QuestionBean question) {
        GridPane row = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(650); col1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(150); col2.setHalignment(javafx.geometry.HPos.CENTER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(120); col3.setHalignment(javafx.geometry.HPos.CENTER);
        row.getColumnConstraints().addAll(col1, col2, col3);

        String questionType = question.isOpenQuestion() ? "Aperta" : "Multipla";

        VBox textContainer = new VBox(5);
        Label lblText = new Label(index + ") [" + questionType + "] " + question.getHeader());
        lblText.setStyle("-fx-font-family: 'Serif'; -fx-font-size: 20px; -fx-text-fill: #555555;");
        lblText.setWrapText(true);
        textContainer.getChildren().add(lblText);

        if (!question.isOpenQuestion() && question.getOptions() != null) {
            String optionsString = String.join(", ", question.getOptions());
            Label lblOptions = new Label("     Opzioni: " + optionsString);
            lblOptions.setStyle("-fx-font-family: 'Serif'; -fx-font-size: 16px; -fx-text-fill: #777777;");
            lblOptions.setWrapText(true);
            textContainer.getChildren().add(lblOptions);
        }
        row.add(textContainer, 0, 0);

        Label lblScore = new Label(question.getMaxScore() + " punti");
        lblScore.setStyle("-fx-font-family: 'Serif'; -fx-font-size: 20px; -fx-text-fill: #555555;");
        lblScore.getProperties().put("valore", question.getMaxScore());
        row.add(lblScore, 1, 0);

        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER);

        Button btnEdit = new Button("✎");
        btnEdit.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-cursor: hand; -fx-font-size: 26px; -fx-padding: 0;");
        btnEdit.setMinSize(40, 40);

        Button btnDelete = new Button("🗑");
        btnDelete.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-cursor: hand; -fx-font-size: 24px; -fx-padding: 0;");
        btnDelete.setMinSize(40, 40);

        VBox rowWrapper = new VBox(15);

        btnDelete.setOnAction(e -> {
            listaDomandeVBox.getChildren().remove(rowWrapper);
            navigatorGUI.getContext().getQuestions().remove(question);
            updateTotals();
        });

        actionBox.getChildren().addAll(btnEdit, btnDelete);
        row.add(actionBox, 2, 0);

        Region divider = new Region();
        divider.setPrefHeight(1.5);
        divider.setMinHeight(1.5);
        divider.setStyle("-fx-background-color: #8CB8F5;");

        rowWrapper.getChildren().addAll(row, divider);

        listaDomandeVBox.getChildren().add(rowWrapper);
    }

    private void updateTotals() {
        int totalQuestions = listaDomandeVBox.getChildren().size();
        int totalScore = 0;

        for (Node node : listaDomandeVBox.getChildren()) {
            if (node instanceof VBox wrapper) {
                GridPane grid = (GridPane) wrapper.getChildren().getFirst();
                Label scoreLabel = (Label) grid.getChildren().get(1);

                int points = (int) scoreLabel.getProperties().get("valore");
                totalScore += points;
            }
        }

        lblTotaleDomande.setText("Totale domande aggiunte: " + totalQuestions);
        lblPunteggioTotale.setText("Punteggio totale test: " + totalScore);
    }

    @FXML
    void handleModificaDettagli(ActionEvent event) {

    }

    @FXML
    void handleAddQuestion(ActionEvent event) {
        if(NavigatorGUI.showPopUp() == QuestionType.OPENQUESTION){
            navigatorGUI.goToOpenQuestionView();
        }
        else navigatorGUI.goToCloseQuestionView();
    }

    @FXML
    void handleSavePublish(ActionEvent event) {
        CreateTestController createTestController = new CreateTestController();
        createTestController.createTest(navigatorGUI.getContext().getTest(),  navigatorGUI.getContext().getQuestions());

        navigatorGUI.goToHomeView();
    }
}
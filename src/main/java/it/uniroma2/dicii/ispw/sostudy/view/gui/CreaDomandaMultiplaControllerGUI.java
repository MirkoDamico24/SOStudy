package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CreaDomandaMultiplaControllerGUI extends BaseControllerGUI {
    @FXML private Label profNameLabel;

    @FXML private TextArea testoDomandaArea;
    @FXML private ComboBox<Integer> punteggioComboBox;

    @FXML private VBox optionsListVBox;

    private ToggleGroup solutionToggleGroup;

    private int opzioniCount = 0;

    public void prepare() {
        testoDomandaArea.clear();
        punteggioComboBox.getSelectionModel().clearSelection();

        setUsernameBundle();
        ObservableList<Integer> punteggi = FXCollections.observableArrayList();
        for (int i = 1; i <= 100; i++) { punteggi.add(i); }
        punteggioComboBox.setItems(punteggi);

        optionsListVBox.getChildren().clear();
        opzioniCount = 0;
        solutionToggleGroup = new ToggleGroup();

        QuestionBean questionToEdit = getNavigatorGUI().getQuestionToEdit();
        if (questionToEdit != null) {
            testoDomandaArea.setText(questionToEdit.getHeader());
            punteggioComboBox.setValue(questionToEdit.getMaxScore());

            List<String> options = questionToEdit.getOptions();
            int solutionIndex = questionToEdit.getSolution();

            for (int i = 0; i < options.size(); i++) {
                addOptionRow(options.get(i), i == solutionIndex, false);
            }
        } else {
            addOptionRow("Inserire il testo della prima opzione", true, true);
            addOptionRow("Inserire il testo della seconda opzione", false, true);
            addOptionRow("Inserire il testo della terza opzione", false, true);
        }
    }

    public void setUsernameBundle() {
        profNameLabel.setText(getFormattedUsername());
    }

    @FXML
    void handleAggiungiOpzione(ActionEvent event) {
        addOptionRow("Inserire il testo della nuova opzione", false, true);
    }

    private void addOptionRow(String text, boolean isSelected, boolean isPrompt) {
        opzioniCount++;

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);

        RadioButton radio = new RadioButton();
        radio.setToggleGroup(solutionToggleGroup);
        if (isSelected) { radio.setSelected(true); }

        TextField textField = new TextField();
        if (isPrompt) {
            textField.setPromptText(text);
        } else {
            textField.setText(text);
        }
        textField.setPrefHeight(45.0);
        textField.setStyle("-fx-background-color: white; -fx-border-color: #8CB8F5; -fx-border-radius: 3; -fx-font-size: 16px;");
        HBox.setHgrow(textField, Priority.ALWAYS);

        Button btnDelete = new Button("🗑");
        btnDelete.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-cursor: hand; -fx-font-size: 24px;");

        btnDelete.setOnAction(e -> {
            if (optionsListVBox.getChildren().size() > 2) {
                optionsListVBox.getChildren().remove(row);
            }
        });

        row.getChildren().addAll(radio, textField, btnDelete);

        optionsListVBox.getChildren().add(row);
    }

    @FXML
    void handleSaveQuestion(ActionEvent event) {
        List<String> options = new ArrayList<>();
        int solutionIndex = 0;
        int currentPosition = 0;

        for (Node node : optionsListVBox.getChildren()) {
            if (node instanceof HBox row) {
                RadioButton rb = (RadioButton) row.getChildren().get(0);
                TextField tf = (TextField) row.getChildren().get(1);

                String optionText = tf.getText().trim();

                if (!optionText.isEmpty()) {
                    options.add(optionText);

                    if (rb.isSelected()) {
                        solutionIndex = currentPosition;
                    }

                    currentPosition++;
                }
            }
        }

        String header = testoDomandaArea.getText();
        int maxScore = punteggioComboBox.getValue();

        QuestionBean qb = new QuestionBean(header, maxScore, options, solutionIndex);

        QuestionBean questionToEdit = getNavigatorGUI().getQuestionToEdit();
        if (questionToEdit != null) {
            List<QuestionBean> qList = getNavigatorGUI().getCurrentTest().getQuestions();
            int index = qList.indexOf(questionToEdit);
            if (index != -1) {
                qList.set(index, qb);
            }
            getNavigatorGUI().setQuestionToEdit(null);
        } else {
            getNavigatorGUI().getCurrentTest().addQuestion(qb);
        }

        getNavigatorGUI().setPreviousView(Views.CLOSEQUESTIONVIEW);
        getNavigatorGUI().goToRecapView();
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
        getNavigatorGUI().getContext().setQuestionToEdit(null);
        if(getNavigatorGUI().getPreviousView() == Views.CREATETEST){
            getNavigatorGUI().goToCreateTestView();
        }
        else getNavigatorGUI().goToRecapView();

        getNavigatorGUI().setPreviousView(Views.CLOSEQUESTIONVIEW);
    }

    @FXML
    void handleHome(){
        getNavigatorGUI().setCurrentQuestionIndex(-1);
        getNavigatorGUI().setQuestions(new ArrayList<>());
        getNavigatorGUI().setCurrentTest(null);
        getNavigatorGUI().getContext().setQuestionToEdit(null);
        getNavigatorGUI().setPreviousView(Views.CLOSEQUESTIONVIEW);
        getNavigatorGUI().goToHomeView();
    }

    @FXML
    public void handleGoVirtualClasses(ActionEvent event) {
        getNavigatorGUI().setCurrentQuestionIndex(-1);
        getNavigatorGUI().setQuestions(new ArrayList<>());
        getNavigatorGUI().setCurrentTest(null);
        getNavigatorGUI().getContext().setQuestionToEdit(null);
        getNavigatorGUI().setPreviousView(Views.CLOSEQUESTIONVIEW);
        getNavigatorGUI().goToClassesView();
    }
}
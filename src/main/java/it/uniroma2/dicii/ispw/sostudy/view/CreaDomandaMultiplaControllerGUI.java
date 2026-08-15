package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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

public class CreaDomandaMultiplaControllerGUI {
    @FXML private Label profNameLabel;

    @FXML private TextArea testoDomandaArea;
    @FXML private ComboBox<Integer> punteggioComboBox;

    @FXML private VBox optionsListVBox;

    private ToggleGroup solutionToggleGroup;

    private int opzioniCount = 0;

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
        testoDomandaArea.clear();
        punteggioComboBox.getSelectionModel().clearSelection();

        setUsernameBundle();
        ObservableList<Integer> punteggi = FXCollections.observableArrayList();
        for (int i = 1; i <= 100; i++) { punteggi.add(i); }
        punteggioComboBox.setItems(punteggi);

        optionsListVBox.getChildren().clear();
        opzioniCount = 0;
        solutionToggleGroup = new ToggleGroup();

        addOptionRow("Inserire il testo della prima opzione");
        addOptionRow("Inserire il testo della seconda opzione");
        addOptionRow("Inserire il testo della terza opzione");
    }

    public void setUsernameBundle() {
        String username = navigatorGUI.getContext().getSession().getProfessor().getName() + " " + navigatorGUI.getContext().getSession().getProfessor().getSurname();
        profNameLabel.setText(username);
    }

    @FXML
    void handleAggiungiOpzione(ActionEvent event) {
        addOptionRow("Inserire il testo della nuova opzione");
    }

    private void addOptionRow(String promptTesto) {
        opzioniCount++;

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);

        RadioButton radio = new RadioButton();
        radio.setToggleGroup(solutionToggleGroup);
        if (opzioniCount == 1) { radio.setSelected(true); }

        TextField textField = new TextField();
        textField.setPromptText(promptTesto);
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
        navigatorGUI.getContext().setQuestions(qb);
        navigatorGUI.setPreviousView(Views.CLOSEQUESTIONVIEW);
        navigatorGUI.goToRecapView();
    }
}
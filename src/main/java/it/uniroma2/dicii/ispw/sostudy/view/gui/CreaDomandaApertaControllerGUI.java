package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CreaDomandaApertaControllerGUI extends BaseControllerGUI {
    @FXML private Label profNameLabel;

    @FXML private TextArea testoDomandaArea;
    @FXML private ComboBox<Integer> punteggioComboBox;

    public void prepare() {
        testoDomandaArea.clear();
        punteggioComboBox.getSelectionModel().clearSelection();

        profNameLabel.setText(super.getFormattedUsername());

        ObservableList<Integer> scores = FXCollections.observableArrayList();
        for (int i = 1; i <= 100; i++) {
            scores.add(i);
        }
        punteggioComboBox.setItems(scores);
    }

    @FXML
    void handleSalvaDomanda(ActionEvent event) {
        String header = testoDomandaArea.getText();
        Integer maxScore = punteggioComboBox.getValue();

        QuestionBean qb = new QuestionBean(header, maxScore);
        getNavigatorGUI().setQuestions(qb);

        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
        getNavigatorGUI().goToRecapView();
    }

    @FXML
    void handleIndietro(ActionEvent event) {
        if(getNavigatorGUI().getPreviousView() == Views.CREATETEST) return;
        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
        getNavigatorGUI().goToRecapView();
    }

    @FXML
    void handleNavHome(ActionEvent event) {
        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
        getNavigatorGUI().goToHomeView();
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
        getNavigatorGUI().goToClassesView();
    }
}
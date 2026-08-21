package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class CreaDomandaApertaControllerGUI extends BaseControllerGUI {
    @FXML private Button btnHome;
    @FXML private Button btnCreaTest;
    @FXML private Button btnClassiVirtuali;
    @FXML private Button btnNotifiche;

    @FXML private Label profNameLabel;
    @FXML private ImageView profAvatar;

    @FXML private TextArea testoDomandaArea;
    @FXML private ComboBox<Integer> punteggioComboBox;
    @FXML private Button btnSalvaDomanda;
    @FXML private Button btnIndietro;

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
        navigatorGUI.getContext().setQuestions(qb);

        navigatorGUI.setPreviousView(Views.OPENQUESTIONVIEW);
        navigatorGUI.goToRecapView();
    }

    @FXML
    void handleIndietro(ActionEvent event) {
        if(navigatorGUI.getPreviousView() == Views.CREATETEST) return;
        navigatorGUI.setPreviousView(Views.OPENQUESTIONVIEW);
        navigatorGUI.goToRecapView();
    }

    @FXML
    void handleNavHome(ActionEvent event) {
        navigatorGUI.setPreviousView(Views.OPENQUESTIONVIEW);
        navigatorGUI.goToHomeView();
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        navigatorGUI.setPreviousView(Views.OPENQUESTIONVIEW);
        navigatorGUI.goToClassesView();
    }
}
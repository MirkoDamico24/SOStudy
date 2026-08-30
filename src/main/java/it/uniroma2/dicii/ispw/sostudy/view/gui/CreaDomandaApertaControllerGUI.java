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

import java.util.ArrayList;
import java.util.List;

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

        QuestionBean questionToEdit = getNavigatorGUI().getQuestionToEdit();
        if (questionToEdit != null) {
            testoDomandaArea.setText(questionToEdit.getHeader());
            punteggioComboBox.setValue(questionToEdit.getMaxScore());
        }
    }

    @FXML
    void handleSalvaDomanda(ActionEvent event) {
        String header = testoDomandaArea.getText();
        Integer maxScore = punteggioComboBox.getValue();

        QuestionBean qb = new QuestionBean(header, maxScore);

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

        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
        getNavigatorGUI().goToRecapView();
    }

    @FXML
    void handleGoBack(ActionEvent event) {
        getNavigatorGUI().getContext().setQuestionToEdit(null);
        if(getNavigatorGUI().getPreviousView() == Views.CREATETEST){
            getNavigatorGUI().goToCreateTestView();
        }
        else getNavigatorGUI().goToRecapView();

        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
    }

    @FXML
    void handleNavHome(ActionEvent event) {
        getNavigatorGUI().setCurrentQuestionIndex(-1);
        getNavigatorGUI().setQuestions(new ArrayList<>());
        getNavigatorGUI().setCurrentTest(null);
        getNavigatorGUI().getContext().setQuestionToEdit(null);
        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
        getNavigatorGUI().goToHomeView();
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        getNavigatorGUI().setCurrentQuestionIndex(-1);
        getNavigatorGUI().setQuestions(new ArrayList<>());
        getNavigatorGUI().setCurrentTest(null);
        getNavigatorGUI().getContext().setQuestionToEdit(null);
        getNavigatorGUI().setPreviousView(Views.OPENQUESTIONVIEW);
        getNavigatorGUI().goToClassesView();
    }
}
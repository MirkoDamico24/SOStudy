package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.Duration;
import java.util.ArrayList;


public class OpenQuestionViewController extends BasicAnswerViewControllerGUI {

    @FXML private Label lblTestoDomanda;
    @FXML private TextArea txtRisposta;


    public void prepare() {
        txtRisposta.clear();
        this.setTestInfo(getNavigatorGUI().getCurrentTest());

        int currentIndex = getNavigatorGUI().getCurrentQuestionIndex();
        if(currentIndex == -1) {
            currentIndex++;
            getNavigatorGUI().setCurrentQuestionIndex(currentIndex);
        }

        QuestionBean question = getNavigatorGUI().getQuestions().get(currentIndex);

        lblTestoDomanda.setText(question.getHeader());

        populateProgressBox(getNavigatorGUI().getQuestions());

        attachToTimer();
    }

    @Override
    public void conclude() {
        Platform.runLater(() -> {
            renderRemaining(Duration.ZERO);
            showAlert("Tempo scaduto.", "Il tempo a disposizione per lo svolgimento del test è saduto.", "Si verrà reindirizzati alla home");
            submitAnswer();
            KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
            ctrl.submitAttempt(getNavigatorGUI().getSession());
            getNavigatorGUI().setQuestions(new ArrayList<>());
            getNavigatorGUI().goToHomeView();
        });
    }

    private void submitAnswer(){
        String answerText = txtRisposta.getText();
        AnswerBean answer = new AnswerBean(answerText);
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.registerAnswer(getNavigatorGUI().getSession(), answer, getNavigatorGUI().getCurrentQuestionIndex());
    }

    @FXML
    void handleProssimaDomanda(ActionEvent event) {
        dispose();
        submitAnswer();
        updateQuestion();

        switch(getNextView()){
            case OPENANSWERVIEW -> getNavigatorGUI().goToOpenAnswerView();
            case CLOSEANSWERVIEW -> getNavigatorGUI().goToCloseAnswerView();
            case HOME -> {
                new KnowledgeEvaluationController().submitAttempt(getNavigatorGUI().getSession());
                getNavigatorGUI().setQuestions(new ArrayList<>());
                getNavigatorGUI().goToHomeView();
            }
            default -> {
                showAlert("Errore", "La schermata selezionata è inesistente", "");
                throw new IllegalArgumentException("Errore");
            }
        }
    }
}
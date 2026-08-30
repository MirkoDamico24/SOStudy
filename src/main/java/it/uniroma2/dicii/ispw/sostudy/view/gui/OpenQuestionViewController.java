package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class OpenQuestionViewController extends BasicAnswerViewControllerGUI {

    @FXML private Label lblTestoDomanda;
    @FXML private TextArea txtRisposta;

    @Override
    protected void setupQuestionUI(QuestionBean question) {
        txtRisposta.clear();
        lblTestoDomanda.setText(question.getHeader());
    }

    @Override
    protected void submitAnswer() {
        String answerText = txtRisposta.getText();
        AnswerBean answer = new AnswerBean(answerText);
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.registerAnswer(getNavigatorGUI().getSession(), answer, getNavigatorGUI().getCurrentQuestionIndex());
    }

    @FXML
    void handleProssimaDomanda(ActionEvent event) {
        proceedToNextQuestion();
    }
}
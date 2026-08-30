package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.AttemptBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Navigator;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class EvaluateOpenAnswerViewControllerGUI extends BaseControllerGUI {

    @FXML private Label testNameLabel;
    @FXML private Label professorNameLabel;
    @FXML private Label questionTextLabel;
    @FXML private TextArea answerTextArea;
    @FXML private TextField scoreInput;
    @FXML private Label maxScoreLabel;
    @FXML private Label evaluatedStudentNameLabel;

    private ChangeListener<String> scoreInputListener;

    public void prepare() {
        scoreInput.clear();
        setTestInfo(getNavigatorGUI().getCurrentTest());

        AttemptBean workingOn = getNavigatorGUI().getCurrentAttempt();

        int currentIndex = getNavigatorGUI().getCurrentQuestionIndex();

        if(currentIndex == -1){
            currentIndex++;
            getNavigatorGUI().setCurrentQuestionIndex(currentIndex);
        }

        QuestionBean question = workingOn.getQuestions().get(currentIndex);
        AnswerBean answer = workingOn.getAnswers().get(currentIndex);

        if (scoreInputListener != null) {
            scoreInput.textProperty().removeListener(scoreInputListener);
        }

        if (question != null) {
            questionTextLabel.setText(question.getHeader());
            answerTextArea.setText(answer.getTextualContent());
            if(answer.getAssignedScore() != -1) {
                maxScoreLabel.setText(answer.getAssignedScore() + "/" + question.getMaxScore());
                scoreInput.setPromptText("");
            } else {
                maxScoreLabel.setText("/" + question.getMaxScore());
                scoreInput.setPromptText("___");
            }

            scoreInputListener = (observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.isEmpty()) {
                    maxScoreLabel.setText("/" + question.getMaxScore());
                } else if (answer.getAssignedScore() != -1) {
                    maxScoreLabel.setText(answer.getAssignedScore() + "/" + question.getMaxScore());
                } else {
                    maxScoreLabel.setText("/" + question.getMaxScore());
                }
            };
            scoreInput.textProperty().addListener(scoreInputListener);
        }

        evaluatedStudentNameLabel.setText(workingOn.getStudent().getName() + " " +  workingOn.getStudent().getSurname());
    }

    private void setTestInfo(TestBean test) {
        testNameLabel.setText(test.getName());
        professorNameLabel.setText(this.getFormattedUsername());
    }

    private void updateCurrentAnswer() {
        AttemptBean attempt = getNavigatorGUI().getCurrentAttempt();
        int nextIndex = getNavigatorGUI().getCurrentQuestionIndex() + 1;
        if(nextIndex == attempt.getQuestions().size()) {nextIndex = -1;}
        getNavigatorGUI().setCurrentQuestionIndex(nextIndex);
    }

    private void registerAnswerScore(){
        int score = 0;
        Navigator nav = getNavigatorGUI();
        if(!scoreInput.getText().isEmpty()){
            score = Integer.parseInt(scoreInput.getText());
            QuestionBean q = nav.getCurrentAttempt().getQuestions().get(nav.getCurrentQuestionIndex());
            if(score > q.getMaxScore()){
                showAlert("Punteggio elevato", "Non si può assegnare un punteggio più alto di quello previsto.", "La risposta sarà valutata con il punteggio massimo");
                score = q.getMaxScore();
            }

        }
        AnswerBean answer = nav.getCurrentAttempt().getAnswers().get(nav.getCurrentQuestionIndex());
        answer.setAssignedScore(score);
    }

    private void submitEvaluation() {
        AttemptBean attempt = getNavigatorGUI().getCurrentAttempt();

        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        try {
            ctrl.registerEvaluation(getNavigatorGUI().getSession(), attempt);
        }
        catch(ControllerException e){
            showAlert("Errore", "Errore durante il salvataggio della valutazione", "Riprovare");
        }
    }

    @FXML
    void handleNextQuestion(ActionEvent event) {
        registerAnswerScore();
        updateCurrentAnswer();

        Views next = getNextView();
        getNavigatorGUI().setPreviousView(Views.EVALUATEOPENANSWER);
        if (next == Views.TESTATTEMPTVIEW) {
            getNavigatorGUI().setQuestions(new ArrayList<>());
            getNavigatorGUI().goToTestAttemptView();
        } else if (next == Views.EVALUATEOPENANSWER) {
            getNavigatorGUI().goToEvaluateOpenAnswerView();
        }
    }

    private Views getNextView() {
        if (getNavigatorGUI().getCurrentQuestionIndex() == -1) {
            showAlert("SoStudy", "Valutazione completata", "Hai valutato tutte le risposte.");
            submitEvaluation();
            return Views.TESTATTEMPTVIEW;
        }
        else return Views.EVALUATEOPENANSWER;
    }

    @FXML
    public void handleGoBack(){
        Views previous = getNavigatorGUI().getPreviousView();
        switch (previous) {
            case TESTATTEMPTVIEW -> getNavigatorGUI().goToTestAttemptView();
            case EVALUATEOPENANSWER ->{
                getNavigatorGUI().setCurrentQuestionIndex(getNavigatorGUI().getCurrentQuestionIndex() -1);
                if(getNavigatorGUI().getCurrentQuestionIndex() == -1){
                    getNavigatorGUI().setQuestions(new ArrayList<>());
                    getNavigatorGUI().goToTestAttemptView();
                }
                else getNavigatorGUI().goToEvaluateOpenAnswerView();
            }
            default-> {
                showAlert("Errore", "Schermata precedente incompatibile", "Flusso d'esecuzione compromesso");
                System.exit(0);
            }
        }
        getNavigatorGUI().setPreviousView(Views.EVALUATEOPENANSWER);
    }

}
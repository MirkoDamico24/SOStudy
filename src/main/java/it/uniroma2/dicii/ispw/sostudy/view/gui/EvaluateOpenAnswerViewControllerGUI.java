package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.AttemptBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Navigator;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EvaluateOpenAnswerViewControllerGUI extends BaseControllerGUI {

    @FXML private Label testNameLabel;
    @FXML private Label professorNameLabel;
    @FXML private Label questionTextLabel;
    @FXML private TextArea answerTextArea;
    @FXML private TextField scoreInput;
    @FXML private Label maxScoreLabel;
    @FXML private Label evaluatedStudentNameLabel;

    public void prepare() {
        setTestInfo(getNavigatorGUI().getContext().getTest());

        AttemptBean workingOn = getNavigatorGUI().getCurrentAttempt();

        int currentIndex = getNavigatorGUI().getCurrentQuestionIndex();

        if(currentIndex == -1){
            currentIndex++;
            getNavigatorGUI().setCurrentQuestionIndex(currentIndex);
        }

        QuestionBean question = workingOn.getQuestions().get(currentIndex);
        AnswerBean answer = workingOn.getAnswers().get(currentIndex);

        if (question != null) {
            questionTextLabel.setText(question.getHeader());
            answerTextArea.setText(answer.getTextualContent());
            maxScoreLabel.setText("/" + question.getMaxScore());
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
        if(!scoreInput.getText().isEmpty()){
            int score = Integer.parseInt(scoreInput.getText());
            Navigator nav = getNavigatorGUI();
            AnswerBean answer = nav.getCurrentAttempt().getAnswers().get(nav.getCurrentQuestionIndex());
            answer.setAssignedScore(score);
        }

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
        if (next == Views.TESTATTEMPTVIEW) {
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
}
package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.AttemptBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.List;

public class EvaluateOpenAnswerViewControllerGUI extends BaseControllerGUI {

    @FXML private Label testNameLabel;
    @FXML private Label professorNameLabel;
    @FXML private ImageView professorAvatar;
    @FXML private Label questionTextLabel;
    @FXML private TextArea answerTextArea;
    @FXML private Button nextQuestionButton;
    @FXML private TextField scoreInput;
    @FXML private Label maxScoreLabel;
    @FXML private Label evaluatedStudentNameLabel;

    private record AttemptLine(QuestionBean questionBean, AnswerBean answerBean) {}

    private AttemptLine currentAnswer;

    public void prepare() {
        setTestInfo(getNavigatorGUI().getContext().getTest());

        AttemptBean workingOn = getNavigatorGUI().getCurrentAttempt();

        QuestionBean question = workingOn.getQuestions().getFirst();
        AnswerBean answer = workingOn.getAnswers().getFirst();

        setCurrentAnswer(new AttemptLine(question, answer));

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
        AttemptLine currentAnswer = getCurrentAnswer();
        List<QuestionBean> questions = getNavigatorGUI().getContext().getCurrentSelectedAttempt().getQuestions();
        List<AnswerBean> answers = getNavigatorGUI().getContext().getCurrentSelectedAttempt().getAnswers();

        int currentIndex = questions.indexOf(currentAnswer.questionBean);

        if (currentIndex != -1 && answers.get(currentIndex).equals(currentAnswer.answerBean)) {

            int nextQuestion = (currentIndex == questions.size() - 1) ? 0 : currentIndex + 1;

            if (nextQuestion == 0) {
                setCurrentAnswer(null);
            } else {
                setCurrentAnswer(new AttemptLine(
                        questions.get(nextQuestion),
                        answers.get(nextQuestion)
                ));
            }
        }
    }

    private void registerAnswerScore(){
        if(!scoreInput.getText().isEmpty()){
            int score = Integer.parseInt(scoreInput.getText());
            AttemptLine currentAnswer = getCurrentAnswer();
            currentAnswer.answerBean.setAssignedScore(score);
        }

    }

    private void submitEvaluation() {
        AttemptBean attempt = getNavigatorGUI().getCurrentAttempt();

        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        try {
            ctrl.registerEvaluation(getNavigatorGUI().getSession(), attempt, getNavigatorGUI().getCurrentTest());
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
        if (next == Views.HOME) {
            getNavigatorGUI().goToHomeView();
        } else if (next == Views.EVALUATEOPENANSWER) {
            getNavigatorGUI().goToEvaluateOpenAnswerView();
        }
    }

    private Views getNextView() {
        if (getCurrentAnswer() == null) {
            showAlert("SoStudy", "Valutazione completata", "Hai valutato tutte le risposte.");
            submitEvaluation();
            return Views.HOME;
        }
        else return Views.EVALUATEOPENANSWER;
    }

    private void setCurrentAnswer(AttemptLine line){
        this.currentAnswer = line;
    }

    private AttemptLine getCurrentAnswer(){
        return this.currentAnswer;
    }
}
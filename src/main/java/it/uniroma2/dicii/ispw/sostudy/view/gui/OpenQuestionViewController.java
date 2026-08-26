package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.TestTimerService;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.observer.TimerObserver;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Duration;
import java.util.List;

public class OpenQuestionViewController extends BaseControllerGUI implements TimerObserver {

    @FXML private Label lblNomeTest;
    @FXML private Label studentNameLabel;
    @FXML private Label lblTestoDomanda;
    @FXML private TextArea txtRisposta;
    @FXML private Button btnProssimaDomanda;
    @FXML private ProgressIndicator progressTimer;
    @FXML private Label lblTimer;
    @FXML private VBox listaProgressoVBox;

    private TestTimerService testTimer;

    public void prepare() {
        setTestInfo(getNavigatorGUI().getContext().getTest());

        QuestionBean question = getNextQuestion(getNavigatorGUI().getContext().getQuestions());
        assert question != null;
        lblTestoDomanda.setText(question.getHeader());

        populateProgressBox(getNavigatorGUI().getContext().getQuestions());

        attachToTimer();
    }

    private void attachToTimer() {
        testTimer = getNavigatorGUI().getSession().getTimer();
        testTimer.attach(this);
        renderRemaining(testTimer.getRemaining());
    }

    public void dispose() {
        if (testTimer != null) {
            testTimer.detach(this);
        }
    }

    @Override
    public void update() {
        Platform.runLater(() -> renderRemaining(navigatorGUI.getSession().getTimer().getRemaining()));
    }

    @Override
    public void conclude() {
        Platform.runLater(() -> {
            renderRemaining(Duration.ZERO);
            showAlert("Tempo scaduto.", "Il tempo a disposizione per lo svolgimento del test è saduto.", "Si verrà reindirizzati alla home");
            submitAnswer();
            KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
            ctrl.submitAttempt(getNavigatorGUI().getSession());
            getNavigatorGUI().goToHomeView();
        });
    }

    private void renderRemaining(Duration remaining) {
        long totalSeconds = Math.max(remaining.getSeconds(), 0);
        long minuti = totalSeconds / 60;
        long secondi = totalSeconds % 60;
        lblTimer.setText(String.format("%02d:%02d", minuti, secondi));

        double totalSecondsAll = testTimer.getTotalDuration().getSeconds();
        double percentuale = totalSecondsAll > 0 ? totalSeconds / totalSecondsAll : 0;
        progressTimer.setProgress(percentuale);
    }

    private QuestionBean getNextQuestion(List<QuestionBean> questions) {
        for (QuestionBean question : questions) {
            if (!question.isAnswerd()) return question;
        }
        return null;
    }

    private void setTestInfo(TestBean test) {
        lblNomeTest.setText(test.getName());
        studentNameLabel.setText(this.getFormattedUsername());
    }

    public void populateProgressBox(List<QuestionBean> questions) {
        listaProgressoVBox.getChildren().clear();

        for (int i = 0; i < questions.size(); i++) {
            QuestionBean q = questions.get(i);
            String tick = q.isAnswerd() ? "☑ " : "☐ ";
            Label lblQuestion = new Label(tick + "Question " + (i + 1));
            lblQuestion.setFont(new Font("Serif Regular", 22));
            lblQuestion.setStyle(q.isAnswerd() ? "-fx-text-fill: #1C77FF;" : "-fx-text-fill: #555555;");
            listaProgressoVBox.getChildren().add(lblQuestion);

            if (i < questions.size() - 1) {
                Separator sep = new Separator();
                sep.setStyle("-fx-background-color: #8CB8F5;");
                listaProgressoVBox.getChildren().add(sep);
            }
        }
    }

    private void submitAnswer(){
        String answerText = txtRisposta.getText();
        QuestionBean question = getNextQuestion(getNavigatorGUI().getContext().getQuestions());
        AnswerBean answer = new AnswerBean(answerText);
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.registerAnswer(getNavigatorGUI().getSession(), question, answer);
        question.setAnswerd(true);
    }

    @FXML
    void handleProssimaDomanda(ActionEvent event) {
        dispose();
        submitAnswer();

        switch(getNextView()){
            case OPENANSWERVIEW -> getNavigatorGUI().goToOpenAnswerView();
            case CLOSEANSWERVIEW -> getNavigatorGUI().goToCloseAnswerView();
            case HOME -> {
                new KnowledgeEvaluationController().submitAttempt(getNavigatorGUI().getSession());
                getNavigatorGUI().goToHomeView();
            }
            default -> {
                showAlert("Errore", "La schermata selezionata è inesistente", "");
                throw new IllegalArgumentException("Errore");
            }
        }
    }

    private Views getNextView() {
        if(getNextQuestion(getNavigatorGUI().getContext().getQuestions()) == null){
            showAlert("SoStudy", "Test Concluso", "Confermare per tornare alle home.");
            return Views.HOME;
        }

        if(getNextQuestion(getNavigatorGUI().getContext().getQuestions()).isOpenQuestion()) return Views.OPENANSWERVIEW;
        else return Views.CLOSEANSWERVIEW;
    }
}
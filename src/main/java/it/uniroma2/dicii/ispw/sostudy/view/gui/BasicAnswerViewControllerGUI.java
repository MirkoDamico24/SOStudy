package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.TakeTestController;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.TestTimerService;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.observer.TimerObserver;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicAnswerViewControllerGUI extends BaseControllerGUI implements TimerObserver {
    @FXML
    private Label lblTimer;
    @FXML
    private ProgressIndicator progressTimer;
    @FXML
    private Label lblNomeTest;
    @FXML
    private Label studentNameLabel;
    @FXML
    private VBox listaProgressoVBox;

    private TestTimerService testTimer;


    protected abstract void setupQuestionUI(QuestionBean question);
    protected abstract void submitAnswer();

    public void prepare() {
        setTestInfo(getNavigatorGUI().getCurrentTest());

        int currentIndex = getNavigatorGUI().getCurrentQuestionIndex();
        if (currentIndex == -1) {
            currentIndex++;
            getNavigatorGUI().setCurrentQuestionIndex(currentIndex);
        }

        QuestionBean question = getNavigatorGUI().getQuestions().get(currentIndex);

        setupQuestionUI(question);

        populateProgressBox(getNavigatorGUI().getQuestions());
        attachToTimer();
    }

    protected void proceedToNextQuestion() {
        dispose();
        submitAnswer();
        updateQuestion();

        switch (getNextView()) {
            case OPENANSWERVIEW -> getNavigatorGUI().goToOpenAnswerView();
            case CLOSEANSWERVIEW -> getNavigatorGUI().goToCloseAnswerView();
            case HOME -> {
                new TakeTestController().submitAttempt(getNavigatorGUI().getSession());
                getNavigatorGUI().setQuestions(new ArrayList<>());
                getNavigatorGUI().goToHomeView();
            }
            default -> showAlert("Errore", "Tipo di domanda successivo non supportato dalla UI", "");
        }
    }

    protected void attachToTimer() {
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
            TakeTestController ctrl = new TakeTestController();
            ctrl.submitAttempt(getNavigatorGUI().getSession());
            getNavigatorGUI().setQuestions(new ArrayList<>());
            getNavigatorGUI().goToHomeView();
        });
    }

    protected void renderRemaining(Duration remaining) {
        long totalSeconds = Math.max(remaining.getSeconds(), 0);
        long minuti = totalSeconds / 60;
        long secondi = totalSeconds % 60;
        lblTimer.setText(String.format("%02d:%02d", minuti, secondi));

        double totalSecondsAll = testTimer.getTotalDuration().getSeconds();
        double percentuale = totalSecondsAll > 0 ? totalSeconds / totalSecondsAll : 0;
        progressTimer.setProgress(percentuale);
    }

    protected void updateQuestion() {
        int nextIndex = getNavigatorGUI().getCurrentQuestionIndex() + 1;
        if (nextIndex == getNavigatorGUI().getQuestions().size()) nextIndex = -1;
        getNavigatorGUI().setCurrentQuestionIndex(nextIndex);
    }

    protected void setTestInfo(TestBean test) {
        lblNomeTest.setText(test.getName());
        studentNameLabel.setText(this.getFormattedUsername());
    }

    public void populateProgressBox(List<QuestionBean> questions) {
        listaProgressoVBox.getChildren().clear();

        for (int i = 0; i < questions.size(); i++) {
            String tick = (i < getNavigatorGUI().getCurrentQuestionIndex()) ? "☑ " : "☐ ";
            Label lblQuestion = new Label(tick + "Question " + (i + 1));
            lblQuestion.setFont(new Font("Serif Regular", 22));
            lblQuestion.setStyle((i < getNavigatorGUI().getCurrentQuestionIndex()) ? "-fx-text-fill: #1C77FF;" : "-fx-text-fill: #555555;");
            listaProgressoVBox.getChildren().add(lblQuestion);

            if (i < questions.size() - 1) {
                Separator sep = new Separator();
                sep.setStyle("-fx-background-color: #8CB8F5;");
                listaProgressoVBox.getChildren().add(sep);
            }
        }
    }

    protected Views getNextView() {
        if (getNavigatorGUI().getCurrentQuestionIndex() == -1) {
            showAlert("SoStudy", "Test Concluso", "Confermare per tornare alle home.");
            return Views.HOME;
        }

        int index = getNavigatorGUI().getCurrentQuestionIndex();
        if (getNavigatorGUI().getQuestions().get(index).isOpenQuestion()) return Views.OPENANSWERVIEW;
        else return Views.CLOSEANSWERVIEW;
    }
}
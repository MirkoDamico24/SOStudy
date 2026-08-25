package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.TestTimerService;
import it.uniroma2.dicii.ispw.sostudy.eng.timer.observer.TimerObserver;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Duration;
import java.util.List;

public class CloseQuestionViewController extends BaseControllerGUI implements TimerObserver {
    @FXML private Label lblNomeTest;
    @FXML private ImageView studentAvatar;
    @FXML private Label studentNameLabel;

    // --- Form Centrale ---
    @FXML private Label lblTestoDomanda;
    @FXML private VBox optionsVBox;
    @FXML private Button btnProssimaDomanda;

    // --- Sidebar Destra ---
    @FXML private ProgressIndicator progressTimer;
    @FXML private Label lblTimer;
    @FXML private VBox listaProgressoVBox;

    // Riferimenti per gestire la logica di mutua esclusione (tipo RadioButton)
    private HBox nodoOpzioneAttiva = null;
    private Integer rispostaSelezionata = null;
    private String fillColor = "-fx-text-fill: #555555;";

    private TestTimerService testTimer;

    public void prepare() {
        setTestInfo(getNavigatorGUI().getContext().getTest());

        QuestionBean question = getNextQuestion(getNavigatorGUI().getContext().getQuestions());
        assert question != null;
        lblTestoDomanda.setText(question.getHeader());
        popolaOpzioni(question.getOptions());

        populateProgressBox(getNavigatorGUI().getContext().getQuestions());

        attachToTimer();
    }

    private void setTestInfo(TestBean test) {
        lblNomeTest.setText(test.getName());
        studentNameLabel.setText(this.getFormattedUsername());
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

    private void submitAnswer(){
        int answerInt = getRispostaSelezionata();
        QuestionBean question = getNextQuestion(getNavigatorGUI().getContext().getQuestions());
        AnswerBean answer = new AnswerBean(answerInt);
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.registerAnswer(getNavigatorGUI().getSession(), question, answer);
        question.setAnswerd(true);
    }

    private void renderRemaining(java.time.Duration remaining) {
        long totalSeconds = Math.max(remaining.getSeconds(), 0);
        long minuti = totalSeconds / 60;
        long secondi = totalSeconds % 60;
        lblTimer.setText(String.format("%02d:%02d", minuti, secondi));

        double totalSecondsAll = testTimer.getTotalDuration().getSeconds();
        double percentuale = totalSecondsAll > 0 ? totalSeconds / totalSecondsAll : 0;
        progressTimer.setProgress(percentuale);
    }


    public void popolaOpzioni(List<String> options) {
        optionsVBox.getChildren().clear();

        int index = 1;
        for (String text : options) {
            // Contenitore esterno dell'opzione
            HBox optionBox = new HBox();
            optionBox.setAlignment(Pos.CENTER_LEFT);
            optionBox.setPadding(new Insets(10, 20, 10, 10));
            optionBox.setStyle("-fx-border-color: #777777; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: white; -fx-cursor: hand;");

            Label lblNum = new Label(index + ")");
            lblNum.setFont(new Font("Serif", 32));
            lblNum.setStyle("-fx-background-color: #EAEAEA; -fx-border-color: #999999; -fx-border-radius: 50em; -fx-background-radius: 50em; -fx-padding: 2 15 2 15;" + fillColor);

            Region spacer1 = new Region();
            spacer1.setPrefWidth(20);

            Label lblTesto = new Label(text);
            lblTesto.setFont(new Font("Serif", 32));
            lblTesto.setStyle(fillColor);

            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            Label lblCheck = new Label("☑");
            lblCheck.setFont(new Font("System", 32));
            lblCheck.setStyle(fillColor);
            lblCheck.setVisible(false);

            optionBox.getChildren().addAll(lblNum, spacer1, lblTesto, spacer2, lblCheck);


            final String optionValue = text;
            optionBox.setOnMouseClicked(e -> {

                // A. Deseleziona la precedente (se esiste)
                if (nodoOpzioneAttiva != null) {
                    nodoOpzioneAttiva.setStyle("-fx-border-color: #777777; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: white; -fx-cursor: hand;");
                    // Nascondi la checkbox (è il 5° elemento, indice 4)
                    (nodoOpzioneAttiva.getChildren().get(4)).setVisible(false);
                }

                // B. Seleziona l'attuale (Bordo Blu #1C77FF)
                optionBox.setStyle("-fx-border-color: #1C77FF; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: white; -fx-cursor: hand;");
                lblCheck.setVisible(true);

                // C. Salva in memoria lo stato
                nodoOpzioneAttiva = optionBox;
                rispostaSelezionata = options.indexOf(optionValue);
            });

            optionsVBox.getChildren().add(optionBox);
            index++;
        }
    }

    /**
     * Metodo per permettere ad altri Controller di recuperare la risposta
     */
    public Integer getRispostaSelezionata() {
        return rispostaSelezionata;
    }

    @FXML
    void handleProssimaDomanda(ActionEvent event) {
        if (rispostaSelezionata == null) {
            showAlert("SoStudy", "Nessuna risposta selezionata.", "Per proseguire selezionare un'opzione");
        }

        dispose();
        submitAnswer();

        switch(getNextView()){
            case OPENANSWERVIEW -> getNavigatorGUI().goToOpenAnswerView();
            case CLOSEANSWERVIEW -> getNavigatorGUI().goToCloseAnswerView();
            case HOME -> {
                new KnowledgeEvaluationController().submitAttempt(getNavigatorGUI().getSession());
                getNavigatorGUI().goToHomeView();
            }
            default -> showAlert("Errore", "Tipo di domanda successivo non supportato dalla UI", "");
        }
    }

    private QuestionBean getNextQuestion(List<QuestionBean> questions) {
        for (QuestionBean question : questions) {
            if (!question.isAnswerd()) return question;
        }
        return null;
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


    private Views getNextView() {
        if(getNextQuestion(getNavigatorGUI().getContext().getQuestions()) == null){
            showAlert("SoStudy", "Test Concluso", "Confermare per tornare alle home.");
            return Views.HOME;
        }

        if(getNextQuestion(getNavigatorGUI().getContext().getQuestions()).isOpenQuestion()) return Views.OPENANSWERVIEW;
        else return Views.CLOSEANSWERVIEW;
    }

}

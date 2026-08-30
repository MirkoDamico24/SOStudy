package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.AnswerBean;
import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class CloseQuestionViewController extends BasicAnswerViewControllerGUI {

    @FXML
    private Label lblTestoDomanda;
    @FXML
    private VBox optionsVBox;

    private HBox nodoOpzioneAttiva = null;
    private Integer rispostaSelezionata = null;
    private String fillColor = "-fx-text-fill: #555555;";

    @Override
    protected void setupQuestionUI(QuestionBean question) {
        lblTestoDomanda.setText(question.getHeader());
        popolaOpzioni(question.getOptions());
    }

    @Override
    protected void submitAnswer() {
        int answerInt = rispostaSelezionata;
        AnswerBean answer = new AnswerBean(answerInt);
        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        ctrl.registerAnswer(getNavigatorGUI().getSession(), answer, getNavigatorGUI().getCurrentQuestionIndex());
    }

    public void popolaOpzioni(List<String> options) {
        optionsVBox.getChildren().clear();

        int index = 1;
        for (String text : options) {
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
                if (nodoOpzioneAttiva != null) {
                    nodoOpzioneAttiva.setStyle("-fx-border-color: #777777; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: white; -fx-cursor: hand;");
                    (nodoOpzioneAttiva.getChildren().get(4)).setVisible(false);
                }

                optionBox.setStyle("-fx-border-color: #1C77FF; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: white; -fx-cursor: hand;");
                lblCheck.setVisible(true);

                nodoOpzioneAttiva = optionBox;
                rispostaSelezionata = options.indexOf(optionValue);
            });

            optionsVBox.getChildren().add(optionBox);
            index++;
        }
    }

    @FXML
    void handleProssimaDomanda(ActionEvent event) {
        if (rispostaSelezionata == null) {
            showAlert("SoStudy", "Nessuna risposta selezionata.", "Per proseguire selezionare un'opzione");
            getNavigatorGUI().goToCloseAnswerView();
        }
        proceedToNextQuestion();
    }
}
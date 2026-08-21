package it.uniroma2.dicii.ispw.sostudy.view.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.List;
import java.util.Arrays;

public class CloseQuestionViewController {
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

    // --- Variabili di stato ---
    private Timeline timeline;
    private int secondiRimanenti;
    private int tempoTotaleInSecondi;

    // Riferimenti per gestire la logica di mutua esclusione (tipo RadioButton)
    private HBox nodoOpzioneAttiva = null;
    private String rispostaSelezionata = null;
    private String fillColor = "-fx-text-fill: #555555;";

    class QuestionStatus {
        int numero;
        boolean completata;
        public QuestionStatus(int numero, boolean completata) {
            this.numero = numero; this.completata = completata;
        }
    }

    @FXML
    public void initialize() {
        setDatiStudenteETest("Verifica Ingegneria del Software", "Mario Rossi", null);
        lblTestoDomanda.setText("Il BCE è un pattern di analisi?");

        avviaTimer(2720); // 45 minuti e 20 secondi

        // Popola la Sidebar di destra
        List<QuestionStatus> progresso = Arrays.asList(
                new QuestionStatus(1, true),
                new QuestionStatus(2, true),
                new QuestionStatus(3, false),
                new QuestionStatus(4, false)
        );
        popolaProgresso(progresso);

        // Popola dinamicamente le opzioni (potrebbero essere 2, 3, 4 o più)
        List<String> opzioni = Arrays.asList("Vero", "Falso");
        popolaOpzioni(opzioni);
    }

    public void setDatiStudenteETest(String nomeTest, String nomeStudente, Image avatar) {
        lblNomeTest.setText(nomeTest);
        studentNameLabel.setText(nomeStudente);
        if (avatar != null) { studentAvatar.setImage(avatar); }
    }

    /**
     * Crea le righe cliccabili per le opzioni multiple adattandosi al loro numero.
     */
    public void popolaOpzioni(List<String> opzioni) {
        optionsVBox.getChildren().clear();

        int index = 1;
        for (String testoOpzione : opzioni) {
            // Contenitore esterno dell'opzione
            HBox optionBox = new HBox();
            optionBox.setAlignment(Pos.CENTER_LEFT);
            optionBox.setPadding(new Insets(10, 20, 10, 10));
            // Stile di default (Grigio, non selezionato)
            optionBox.setStyle("-fx-border-color: #777777; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: white; -fx-cursor: hand;");

            // 1. Il "Pillolo" grigio con il numero (es. "1) ")
            Label lblNum = new Label(index + ")");
            lblNum.setFont(new Font("Serif", 32));
            lblNum.setStyle("-fx-background-color: #EAEAEA; -fx-border-color: #999999; -fx-border-radius: 50em; -fx-background-radius: 50em; -fx-padding: 2 15 2 15;" + fillColor);

            // Spaziatura tra numero e testo
            Region spacer1 = new Region();
            spacer1.setPrefWidth(20);

            // 2. Il Testo dell'opzione
            Label lblTesto = new Label(testoOpzione);
            lblTesto.setFont(new Font("Serif", 32));
            lblTesto.setStyle(fillColor);

            // Spazio espandibile per spingere la checkbox a destra
            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            // 3. Icona Checkbox (Nascosta di default)
            Label lblCheck = new Label("☑");
            lblCheck.setFont(new Font("System", 32));
            lblCheck.setStyle(fillColor);
            lblCheck.setVisible(false);

            optionBox.getChildren().addAll(lblNum, spacer1, lblTesto, spacer2, lblCheck);

            // --- EVENTO CLICK ---
            final String valoreDiQuestaOpzione = testoOpzione;
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
                rispostaSelezionata = valoreDiQuestaOpzione;
            });

            optionsVBox.getChildren().add(optionBox);
            index++;
        }
    }

    /**
     * Metodo per permettere ad altri Controller di recuperare la risposta
     */
    public String getRispostaSelezionata() {
        return rispostaSelezionata;
    }

    @FXML
    void handleProssimaDomanda(ActionEvent event) {
        if (rispostaSelezionata == null) {

            return;
        }
        // Logica applicativa per salvare la risposta sul DB e passare oltre...
    }

    // --- METODI TIMER E PROGRESSO (Identici alla versione a risposta aperta) ---

    public void avviaTimer(int secondiTotali) {
        this.tempoTotaleInSecondi = secondiTotali;
        this.secondiRimanenti = secondiTotali;
        if (timeline != null) timeline.stop();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondiRimanenti--;
            int minuti = secondiRimanenti / 60;
            int secondi = secondiRimanenti % 60;

            lblTimer.setText(String.format("%02d:%02d", minuti, secondi));
            progressTimer.setProgress((double) secondiRimanenti / tempoTotaleInSecondi);

            if (secondiRimanenti <= 0) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void popolaProgresso(List<QuestionStatus> domande) {
        listaProgressoVBox.getChildren().clear();
        for (int i = 0; i < domande.size(); i++) {
            QuestionStatus q = domande.get(i);
            String spunta = q.completata ? "☑ " : "☐ ";
            Label lblQuestion = new Label(spunta + "Question " + q.numero);
            lblQuestion.setFont(new Font("Serif Regular", 22));
            lblQuestion.setStyle(q.completata ? "-fx-text-fill: #1C77FF;" : fillColor);

            listaProgressoVBox.getChildren().add(lblQuestion);
            if (i < domande.size() - 1) {
                Separator sep = new Separator();
                sep.setStyle("-fx-background-color: #8CB8F5;");
                listaProgressoVBox.getChildren().add(sep);
            }
        }
    }
}

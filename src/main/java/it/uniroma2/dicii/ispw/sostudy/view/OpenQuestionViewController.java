package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.List;
import java.util.Arrays;

public class OpenQuestionViewController {

    @FXML private Label lblNomeTest;
    @FXML private ImageView studentAvatar;
    @FXML private Label studentNameLabel;

    // --- Form Centrale ---
    @FXML private Label lblTestoDomanda;
    @FXML private TextArea txtRisposta;
    @FXML private Button btnProssimaDomanda;

    // --- Sidebar Destra ---
    @FXML private ProgressIndicator progressTimer;
    @FXML private Label lblTimer;
    @FXML private VBox listaProgressoVBox;

    // --- Gestione Timer Interna ---
    private Timeline timeline;
    private int secondiRimanenti;
    private int tempoTotaleInSecondi;

    // Struttura dati per simulare le domande del test
    class QuestionStatus {
        int numero;
        boolean completata;

        public QuestionStatus(int numero, boolean completata) {
            this.numero = numero;
            this.completata = completata;
        }
    }

    @FXML
    public void initialize() {
        // Esempio di caricamento dati per test
        setDatiStudenteETest("Verifica di Modellazione", "Mario Rossi", null);

        // Imposta e avvia un timer di 45 minuti e 20 secondi (2720 secondi)
        avviaTimer(2720);

        // Simulazione lista di 4 domande (le prime 2 già viste/completate)
        List<QuestionStatus> progresso = Arrays.asList(
                new QuestionStatus(1, true),
                new QuestionStatus(2, true),
                new QuestionStatus(3, false),
                new QuestionStatus(4, false)
        );
        popolaProgresso(progresso);
    }

    public void setDatiStudenteETest(String nomeTest, String nomeStudente, Image avatar) {
        lblNomeTest.setText(nomeTest);
        studentNameLabel.setText(nomeStudente);
        if (avatar != null) {
            studentAvatar.setImage(avatar);
        }
    }

    /**
     * Avvia il conteggio alla rovescia e aggiorna l'anello visivo ogni secondo.
     */
    public void avviaTimer(int secondiTotali) {
        this.tempoTotaleInSecondi = secondiTotali;
        this.secondiRimanenti = secondiTotali;

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondiRimanenti--;

            // Calcolo minuti e secondi
            int minuti = secondiRimanenti / 60;
            int secondi = secondiRimanenti % 60;

            // Formattazione stringa 00:00
            lblTimer.setText(String.format("%02d:%02d", minuti, secondi));

            // Aggiorna l'anello del ProgressIndicator (valore da 0.0 a 1.0)
            double percentuale = (double) secondiRimanenti / tempoTotaleInSecondi;
            progressTimer.setProgress(percentuale);

            if (secondiRimanenti <= 0) {
                timeline.stop();
                System.out.println("Tempo Scaduto! Consegna automatica del test.");
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Genera dinamicamente l'elenco delle domande a destra.
     * Le domande già affrontate sono marcate con la spunta ☑ e scritte in blu scuro (#1C77FF).
     */
    public void popolaProgresso(List<QuestionStatus> domande) {
        listaProgressoVBox.getChildren().clear();

        for (int i = 0; i < domande.size(); i++) {
            QuestionStatus q = domande.get(i);

            // Simbolo spunta (☑ se vista, ☐ se futura)
            String spunta = q.completata ? "☑ " : "☐ ";
            Label lblQuestion = new Label(spunta + "Question " + q.numero);
            lblQuestion.setFont(new Font("Serif Regular", 22));

            // Applicazione dei colori specificati
            if (q.completata) {
                // Blu scuro RGB(28, 119, 255)
                lblQuestion.setStyle("-fx-text-fill: #1C77FF;");
            } else {
                // Grigio standard per quelle non ancora fatte
                lblQuestion.setStyle("-fx-text-fill: #555555;");
            }

            listaProgressoVBox.getChildren().add(lblQuestion);

            // Aggiungi un separatore grigio chiaro tra le voci (tranne l'ultima)
            if (i < domande.size() - 1) {
                Separator sep = new Separator();
                sep.setStyle("-fx-background-color: #8CB8F5;");
                listaProgressoVBox.getChildren().add(sep);
            }
        }
    }

    @FXML
    void handleProssimaDomanda(ActionEvent event) {
        String rispostaInserita = txtRisposta.getText();
        System.out.println("Risposta salvata: " + rispostaInserita);

        // Logica per caricare la domanda successiva...
    }
}

package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class RiepilogoTestController {
    @FXML private Label lblNomeTest;
    @FXML private Label lblClasse;
    @FXML private Label lblData;
    @FXML private Label lblOra;
    @FXML private Label lblDurata;

    // --- Box Centrale ---
    @FXML private Label lblTotaleDomande;
    @FXML private Label lblPunteggioTotale;
    @FXML private VBox listaDomandeVBox;

    // --- Header ---
    @FXML private ImageView profAvatar;
    @FXML private Label profNameLabel;

    // Classe fittizia per rappresentare i dati da caricare
    class DomandaMock {
        String tipo; // "Aperta" o "Multipla"
        String testo;
        int punteggio;
        String opzioni; // Es: "Vero, Falso" (null se aperta)

        public DomandaMock(String tipo, String testo, int punteggio, String opzioni) {
            this.tipo = tipo; this.testo = testo; this.punteggio = punteggio; this.opzioni = opzioni;
        }
    }

    @FXML
    public void initialize() {
        // Esempio di caricamento dati (Simulazione)
        setDettagliTest("Verifica di UML", "Classe 5A", "20/10/2026", "10:00", "60");

        List<DomandaMock> mieDomande = Arrays.asList(
                new DomandaMock("Aperta", "Per cosa è utilizzato lo use case diagram?", 10, null),
                new DomandaMock("Multipla", "Il BCE è un pattern di analisi?", 3, "Vero, Falso")
        );
        popolaListaDomande(mieDomande);
    }

    /**
     * Aggiorna i dettagli nella colonna di sinistra.
     */
    public void setDettagliTest(String nome, String classe, String data, String ora, String durata) {
        lblNomeTest.setText("Nome test: " + nome);
        lblClasse.setText("Assegnato a: " + classe);
        lblData.setText("Data di consegna: " + data);
        lblOra.setText("Ora di consegna: " + ora);
        lblDurata.setText("Durata test: " + durata + " minutes");
    }

    /**
     * Prende una lista di domande e genera le righe nell'interfaccia
     */
    public void popolaListaDomande(List<DomandaMock> domande) {
        listaDomandeVBox.getChildren().clear();
        int index = 1;

        for (DomandaMock d : domande) {
            aggiungiRigaDomandaUI(index, d.tipo, d.testo, d.punteggio, d.opzioni);
            index++;
        }
        aggiornaTotali();
    }

    /**
     * Costruisce visivamente una singola riga per una domanda e i relativi bottoni.
     */
    private void aggiungiRigaDomandaUI(int indice, String tipo, String testo, int punteggio, String stringaOpzioni) {
        GridPane row = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(650); col1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(150); col2.setHalignment(javafx.geometry.HPos.CENTER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(120); col3.setHalignment(javafx.geometry.HPos.CENTER);
        row.getColumnConstraints().addAll(col1, col2, col3);

        // 1. Colonna Testo (e opzioni se multipla)
        VBox textContainer = new VBox(5);
        Label lblTesto = new Label(indice + ") [" + tipo + "] " + testo);
        lblTesto.setStyle("-fx-font-family: 'Serif'; -fx-font-size: 20px; -fx-text-fill: #555555;");
        lblTesto.setWrapText(true);
        textContainer.getChildren().add(lblTesto);

        // Se è multipla, aggiunge la riga compatta delle opzioni
        if (tipo.equals("Multipla") && stringaOpzioni != null) {
            Label lblOpzioni = new Label("     Opzioni: " + stringaOpzioni);
            lblOpzioni.setStyle("-fx-font-family: 'Serif'; -fx-font-size: 16px; -fx-text-fill: #777777;");
            lblOpzioni.setWrapText(true);
            textContainer.getChildren().add(lblOpzioni);
        }
        row.add(textContainer, 0, 0);

        // 2. Colonna Punteggio
        Label lblPunteggio = new Label(punteggio + " punti");
        lblPunteggio.setStyle("-fx-font-family: 'Serif'; -fx-font-size: 20px; -fx-text-fill: #555555;");
        lblPunteggio.getProperties().put("valore", punteggio);
        row.add(lblPunteggio, 1, 0);

        // 3. Colonna Azioni (Matita e Cestino) - FIX ICONE
        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER);

        // Matita
        Button btnEdit = new Button("✎");
        // Aggiunto -fx-padding: 0; per rimuovere il margine che "schiaccia" l'icona
        btnEdit.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-cursor: hand; -fx-font-size: 26px; -fx-padding: 0;");
        // Forziamo una larghezza e altezza minime per impedire la comparsa dei tre puntini "..."
        btnEdit.setMinSize(40, 40);
        btnEdit.setOnAction(e -> System.out.println("Modifica domanda: " + indice));

        // Cestino
        Button btnDelete = new Button("🗑");
        btnDelete.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-cursor: hand; -fx-font-size: 24px; -fx-padding: 0;");
        btnDelete.setMinSize(40, 40);

        VBox rowWrapper = new VBox(15);

        btnDelete.setOnAction(e -> {
            listaDomandeVBox.getChildren().remove(rowWrapper);
            aggiornaTotali();
        });

        actionBox.getChildren().addAll(btnEdit, btnDelete);
        row.add(actionBox, 2, 0);

        // FIX LINEA DI DIVISIONE: Usiamo una Region piatta al posto di un Separator
        Region lineaDivisione = new Region();
        lineaDivisione.setPrefHeight(1.5);
        lineaDivisione.setMinHeight(1.5);
        lineaDivisione.setStyle("-fx-background-color: #8CB8F5;"); // Solo blu, niente ombra nera

        rowWrapper.getChildren().addAll(row, lineaDivisione);

        listaDomandeVBox.getChildren().add(rowWrapper);
    }

    /**
     * Ricalcola dinamicamente domande e punteggio leggendo l'UI.
     */
    private void aggiornaTotali() {
        int totaleDomande = listaDomandeVBox.getChildren().size();
        int punteggioTotale = 0;

        for (Node node : listaDomandeVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox wrapper = (VBox) node;
                GridPane grid = (GridPane) wrapper.getChildren().get(0);
                Label puntLabel = (Label) grid.getChildren().get(1); // La colonna 2

                int punti = (int) puntLabel.getProperties().get("valore");
                punteggioTotale += punti;
            }
        }

        lblTotaleDomande.setText("Totale domande aggiunte: " + totaleDomande);
        lblPunteggioTotale.setText("Punteggio totale test: " + punteggioTotale);
    }

    // --- AZIONI BOTTONI PRINCIPALI ---

    @FXML
    void handleModificaDettagli(ActionEvent event) {
        System.out.println("Torno alla schermata Dettagli Test...");
        // Logica per caricare crea_test_1.fxml
    }

    @FXML
    void handleAggiungiDomanda(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("popup_scelta_tipo_domanda.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(root));

            popupStage.showAndWait();

            // Dopo la chiusura del popup, qui puoi intercettare la scelta
            // per navigare verso la schermata Aperta o Multipla
            System.out.println("Popup chiuso.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSalvaPubblica(ActionEvent event) {
        System.out.println("Salvataggio finale e pubblicazione del test sul Database...");
    }
}

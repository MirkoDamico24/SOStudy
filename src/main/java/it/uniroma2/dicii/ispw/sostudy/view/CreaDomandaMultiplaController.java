package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CreaDomandaMultiplaController {
    @FXML private ImageView profAvatar;
    @FXML private Label profNameLabel;

    @FXML private TextArea testoDomandaArea;
    @FXML private ComboBox<Integer> punteggioComboBox;

    // Il VBox vuoto in cui inietteremo le righe delle opzioni
    @FXML private VBox optionsListVBox;

    // Gruppo per far sì che si possa selezionare una sola risposta esatta
    private ToggleGroup solutionToggleGroup;

    // Contatore per generare un testo di default sensato
    private int opzioniCount = 0;

    @FXML
    public void initialize() {
        // Inizializza punteggio 1-20
        ObservableList<Integer> punteggi = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) { punteggi.add(i); }
        punteggioComboBox.setItems(punteggi);

        // Inizializza il ToggleGroup per i radio button
        solutionToggleGroup = new ToggleGroup();

        // Crea le 3 opzioni di default richieste
        addOptionRow("Inserire il testo della prima opzione");
        addOptionRow("Inserire il testo della seconda opzione");
        addOptionRow("Inserire il testo della terza opzione");
    }

    /**
     * Metodo chiamato dal bottone blu "Aggiungi opzione"
     */
    @FXML
    void handleAggiungiOpzione(ActionEvent event) {
        addOptionRow("Inserire il testo della nuova opzione");
    }

    /**
     * Cuore della logica dinamica: costruisce una riga intera e la aggiunge alla UI.
     */
    private void addOptionRow(String promptTesto) {
        opzioniCount++;

        // Contenitore della riga
        HBox row = new HBox(20); // Spazio tra gli elementi
        row.setAlignment(Pos.CENTER_LEFT);

        // 1. Radio Button (per scegliere la soluzione)
        RadioButton radio = new RadioButton();
        radio.setToggleGroup(solutionToggleGroup);
        // Seleziona il primo di default
        if (opzioniCount == 1) { radio.setSelected(true); }

        // 2. TextField (per inserire il testo dell'opzione)
        TextField textField = new TextField();
        textField.setPromptText(promptTesto);
        textField.setPrefHeight(45.0);
        textField.setStyle("-fx-background-color: white; -fx-border-color: #8CB8F5; -fx-border-radius: 3; -fx-font-size: 16px;");
        // Fa espandere la textfield per occupare tutto lo spazio disponibile
        HBox.setHgrow(textField, Priority.ALWAYS);

        // 3. Bottone Cestino (Elimina)
        Button btnDelete = new Button("🗑");
        btnDelete.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-cursor: hand; -fx-font-size: 24px;");

        // Azione per rimuovere la riga
        btnDelete.setOnAction(e -> {
            // Impedisce di cancellare se ci sono 2 o meno opzioni (logica base dei test)
            if (optionsListVBox.getChildren().size() > 2) {
                optionsListVBox.getChildren().remove(row);
            }
        });

        // Aggiunge gli elementi alla riga
        row.getChildren().addAll(radio, textField, btnDelete);

        // Aggiunge la riga completa al VBox della UI
        optionsListVBox.getChildren().add(row);
    }

    @FXML
    void handleSalvaDomanda(ActionEvent event) {


        // Cicla su tutte le righe create dinamicamente
        for (Node node : optionsListVBox.getChildren()) {
            if (node instanceof HBox row) {
                RadioButton rb = (RadioButton) row.getChildren().get(0);
                TextField tf = (TextField) row.getChildren().get(1);

                String corretta = rb.isSelected() ? "[ESATTA] " : "[ERRATA] ";
            }
        }
    }
}

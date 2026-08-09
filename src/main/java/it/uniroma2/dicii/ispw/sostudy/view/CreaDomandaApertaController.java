package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CreaDomandaApertaController {
    @FXML private Button btnHome;
    @FXML private Button btnCreaTest;
    @FXML private Button btnClassiVirtuali;
    @FXML private Button btnNotifiche;

    // --- Profilo Dinamico ---
    @FXML private Label profNameLabel;
    @FXML private ImageView profAvatar;

    // --- Elementi del Form e Footer ---
    @FXML private TextArea testoDomandaArea;
    @FXML private ComboBox<Integer> punteggioComboBox;
    @FXML private Button btnSalvaDomanda;
    @FXML private Button btnIndietro;

    /**
     * Inizializzazione automatica per popolare la ComboBox
     */
    @FXML
    public void initialize() {
        // Popola la ComboBox con i numeri interi da 1 a 20
        ObservableList<Integer> punteggi = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) {
            punteggi.add(i);
        }
        punteggioComboBox.setItems(punteggi);
    }

    /**
     * Imposta i dati dell'utente loggato
     */
    public void setDatiProfessore(String nomeProfessore, Image immagineAvatar) {
        profNameLabel.setText(nomeProfessore);
        if (immagineAvatar != null) {
            profAvatar.setImage(immagineAvatar);
        }
    }

    // --- Azioni dei Bottoni ---

    @FXML
    void handleSalvaDomanda(ActionEvent event) {
        // Lettura dei campi
        String testo = testoDomandaArea.getText();
        Integer punteggio = punteggioComboBox.getValue();

        System.out.println("--- SALVATAGGIO DOMANDA APERTA ---");
        System.out.println("Testo: " + testo);
        System.out.println("Punteggio Assegnato: " + punteggio);

        // Logica per aggiungere la domanda al database del test in creazione...
    }

    @FXML
    void handleIndietro(ActionEvent event) {
        System.out.println("Navigazione: Indietro (Ritorno alla schermata del Test)");
        // Logica per tornare alla schermata precedente
    }

    @FXML
    void handleNavHome(ActionEvent event) {
        System.out.println("Navigazione: Home");
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        System.out.println("Navigazione: Classi Virtuali");
    }
}

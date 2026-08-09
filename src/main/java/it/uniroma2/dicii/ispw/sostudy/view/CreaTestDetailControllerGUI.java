package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class CreaTestDetailControllerGUI {
    @FXML private Button btnHome;
    @FXML private Button btnCreaTest;
    @FXML private Button btnClassiVirtuali;
    @FXML private Button btnNotifiche;

    // --- Profilo Dinamico ---
    @FXML private Label profNameLabel;
    @FXML private ImageView profAvatar;

    // --- Elementi del Form ---
    @FXML private TextField nomeTestField;
    @FXML private ComboBox<String> classeComboBox;
    @FXML private DatePicker dataConsegnaPicker;
    @FXML private ComboBox<String> orarioComboBox;
    @FXML private TextField durataField;
    @FXML private Button btnSalva;

    /**
     * Questo metodo viene chiamato automaticamente da JavaFX dopo
     * aver caricato il file FXML. Ideale per configurare i componenti.
     */
    @FXML
    public void initialize() {
        // 1. Popola la ComboBox delle Classi (da sostituire con dati dal DB)
        ObservableList<String> classi = FXCollections.observableArrayList(
                "Classe 1A - Informatica",
                "Classe 2B - Matematica",
                "Classe 3C - Sistemi"
        );
        classeComboBox.setItems(classi);

        // 2. Popola la ComboBox degli Orari (Intervalli di 30 minuti)
        ObservableList<String> orari = FXCollections.observableArrayList(
                "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "11:00", "11:30", "12:00", "12:30", "14:00", "14:30",
                "15:00", "15:30", "16:00", "16:30"
        );
        orarioComboBox.setItems(orari);
    }

    /**
     * Imposta i dati del professore nell'header (da richiamare post-login)
     */
    public void setDatiProfessore(String nomeProfessore, Image immagineAvatar) {
        profNameLabel.setText(nomeProfessore);
        if (immagineAvatar != null) {
            profAvatar.setImage(immagineAvatar);
        }
    }

    // --- Azioni dei Bottoni ---

    @FXML
    void handleSalvaTest(ActionEvent event) {
        // Lettura dei dati inseriti
        String nomeTest = nomeTestField.getText();
        String classeAssegnata = classeComboBox.getValue();
        LocalDate dataConsegna = dataConsegnaPicker.getValue();
        String orario = orarioComboBox.getValue();
        String durata = durataField.getText();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/dicii/ispw/sostudy/PopupSceltaTipoDomanda.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            // Rimuove la barra di sistema in alto (minimizza, ingrandisci, chiudi) per farlo sembrare un vero popup interno
            popupStage.initStyle(StageStyle.UNDECORATED);
            // Blocca la finestra sottostante finché non viene fatta una scelta
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Imposta la scena con lo sfondo trasparente se vuoi un effetto più elegante
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            // Mostra il popup e aspetta
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleNavHome(ActionEvent event) {

    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        
    }
}

package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class HomeControllerGUI {

    @FXML
    private Button btnHome;
    @FXML
    private Button btnCreaTest;
    @FXML
    private Button btnClassiVirtuali;
    @FXML
    private Button btnLogout;

    // Nomi generalizzati per adattarsi sia al prof che allo studente
    @FXML
    private Label userNameLabel;
    @FXML
    private ImageView userAvatar;
    @FXML
    private VBox listaComunicazioni;

    /**
     * Adatta l'interfaccia in base al tipo di utente loggato.
     * @param isProfessore true se è prof, false se è studente
     */
    public void configuraVistaPerRuolo(boolean isProfessore) {
        if (!isProfessore) {
            // Nasconde il tab "Crea Test" nella barra di navigazione
            btnCreaTest.setVisible(false);

            // Rimuove l'ingombro del bottone, permettendo all'HBox
            // di ricentrare automaticamente "Home" e "Classi Virtuali"
            btnCreaTest.setManaged(false);
        }
    }

    /**
     * Imposta i dati visivi dell'utente loggato.
     */
    public void setDatiUtente(String nomeUtente, Image immagineAvatar) {
        userNameLabel.setText(nomeUtente);
        if (immagineAvatar != null) {
            userAvatar.setImage(immagineAvatar);
        }
    }

    /**
     * Popola la lista centrale con le comunicazioni o le notifiche dei test.
     */
    public void popolaComunicazioni(List<String> notifiche) {
        listaComunicazioni.getChildren().clear();

        for (int i = 0; i < notifiche.size(); i++) {
            Label notificaLabel = new Label(notifiche.get(i));
            notificaLabel.setFont(new Font("Serif", 26));
            // Uso setStyle per forzare la priorità del colore grigio scuro
            notificaLabel.setStyle("-fx-text-fill: #555555;");
            notificaLabel.setMaxWidth(Double.MAX_VALUE);
            notificaLabel.setPadding(new Insets(15, 0, 15, 0));

            listaComunicazioni.getChildren().add(notificaLabel);

            if (i < notifiche.size() - 1) {
                Separator separator = new Separator();
                listaComunicazioni.getChildren().add(separator);
            }
        }
    }

    // --- AZIONI DEI BOTTONI ---

    @FXML
    void handleNavCreaTest(ActionEvent event) {
        // Logica per caricare la schermata "Crea Test"
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        // Logica per caricare la schermata "Classi Virtuali"
    }

    @FXML
    void handleLogout(ActionEvent event) {
        // Logica per chiudere la sessione e tornare al login
    }
}

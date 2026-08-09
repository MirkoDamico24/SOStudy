package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.List;
import java.util.Arrays;

public class InsideClassViewControllerGUI {
    @FXML private Button btnNotifiche;
    @FXML private Button btnHome;
    @FXML private Button btnCreaTest;
    @FXML private Button btnNomeClasseNav;

    // --- Profilo ---
    @FXML private ImageView userAvatar;
    @FXML private Label userNameLabel;

    // --- Contenuto Centrale ---
    @FXML private Button btnInvitaStudente;
    @FXML private VBox listaTestVBox;

    @FXML
    public void initialize() {
        // Esempio di utilizzo: (cambia a 'false' per vedere la versione Studente)
        configuraVista(false, "IPSWvirtualclass", "Prof. Mario Rossi", null);

        // Simulazione caricamento ultimi 3 test
        List<String> testAssegnati = Arrays.asList(
                "Test requisiti funzionali",
                "Test applicazione BCE",
                "Test activity diagram"
        );
        popolaTestRecenti(testAssegnati);
    }

    /**
     * Adatta la vista base, nascondendo gli elementi se l'utente è uno Studente,
     * e imposta i testi della navbar e del profilo.
     */
    public void configuraVista(boolean isProfessore, String nomeClasse, String nomeUtente, Image avatar) {
        // Imposta i nomi nella UI
        btnNomeClasseNav.setText(nomeClasse);
        userNameLabel.setText(nomeUtente);
        if (avatar != null) {
            userAvatar.setImage(avatar);
        }

        // Se l'utente NON è un professore, nasconde le funzionalità extra
        if (!isProfessore) {
            btnCreaTest.setVisible(false);
            btnCreaTest.setManaged(false); // Riautomaticamente la Navbar al centro

            btnInvitaStudente.setVisible(false);
            btnInvitaStudente.setManaged(false);
        }
    }

    /**
     * Costruisce le righe dei test dinamicamente, aggiungendo una linea di
     * separazione (stile underline esteso) sotto ciascuna di esse.
     */
    public void popolaTestRecenti(List<String> nomiTest) {
        listaTestVBox.getChildren().clear();

        for (String nomeTest : nomiTest) {
            // Contenitore per il singolo test e la sua linea
            VBox testItemBox = new VBox(10);

            Label lblTest = new Label(nomeTest);
            lblTest.setFont(new Font("Serif", 28));
            // Stile grigio, con cursore cliccabile
            lblTest.setStyle("-fx-text-fill: #555555; -fx-cursor: hand;");

            // Aggiungo evento click sul test per andare (es.) al dettaglio del test
            lblTest.setOnMouseClicked(event ->{});

            // Linea di divisione grigio chiaro sotto il test
            Region underline = new Region();
            underline.setPrefHeight(1.5);
            underline.setMinHeight(1.5);
            underline.setStyle("-fx-background-color: #CCCCCC;"); // Grigio tenue

            testItemBox.getChildren().addAll(lblTest, underline);
            testItemBox.setPadding(new Insets(10, 0, 10, 0));

            listaTestVBox.getChildren().add(testItemBox);
        }
    }

    // --- AZIONI BOTTONI ---

    @FXML
    void handleNavHome(ActionEvent event) {

    }

    @FXML
    void handleNavCreaTest(ActionEvent event) {

    }

    @FXML
    void handleInvitaStudente(ActionEvent event) {

    }
}

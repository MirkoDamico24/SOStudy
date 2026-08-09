package it.uniroma2.dicii.ispw.sostudy.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PopupControllerSceltaTipoDomanda {
    @FXML
    private Button btnRispostaAperta;

    @FXML
    private Button btnRispostaMultipla;

    @FXML
    void handleRispostaAperta(ActionEvent event) {
        System.out.println("Scelta registrata: Domanda a Risposta Aperta");

        // 1. Qui andrà la logica per aprire la schermata di creazione "Risposta Aperta"

        // 2. Chiudi il popup
        chiudiPopup(event);
    }

    @FXML
    void handleRispostaMultipla(ActionEvent event) {
        System.out.println("Scelta registrata: Domanda a Risposta Multipla");

        // 1. Qui andrà la logica per aprire la schermata di creazione "Risposta Multipla"

        // 2. Chiudi il popup
        chiudiPopup(event);
    }

    /**
     * Metodo di utilità per chiudere la finestra attuale recuperando lo Stage dall'evento.
     */
    private void chiudiPopup(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

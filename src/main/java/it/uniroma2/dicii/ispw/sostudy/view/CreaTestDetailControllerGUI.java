package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.ProfessorBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.time.Duration;
import java.time.LocalDate;
import javafx.scene.Parent;
import java.time.LocalTime;
import java.util.Optional;

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

    private Parent root;
    private NavigatorGUI navigatorGUI;

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) {
        this.navigatorGUI = navigatorGUI;
    }

    public void setView(Parent root) {
        this.root = root;
    }

    public Parent getView() {
        return root;
    }

    /**
     * Questo metodo viene chiamato automaticamente da JavaFX dopo
     * aver caricato il file FXML. Ideale per configurare i componenti.
     */
    public void prepare() {
        setUsernameBundle();

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
                "15:00", "15:30", "16:00", "16:30", "17:00"
        );
        orarioComboBox.setItems(orari);
    }


    public void setUsernameBundle() {
        String nameToPrint = "Unavailable";
        ProfessorBean pb = navigatorGUI.getContext().getSession().getProfessor();
        if(pb != null) {
            nameToPrint = pb.getName() + pb.getSurname();
        }
        profNameLabel.setText(nameToPrint);
    }

    // --- Azioni dei Bottoni ---

    @FXML
    void handleSaveTest(ActionEvent event) {
        String testName = nomeTestField.getText();
        String virtualClass = classeComboBox.getValue();
        LocalDate dueDate = dataConsegnaPicker.getValue();
        String time = orarioComboBox.getValue();
        String duration = durataField.getText();

        long durationLong = Long.parseLong(duration);
        Duration finalDuration = Duration.ofMinutes(durationLong);
        TestBean test = new TestBean(testName, dueDate, LocalTime.parse(time), finalDuration, virtualClass);

        navigatorGUI.getContext().setTest(test);

        if(NavigatorGUI.showPopUp() == QuestionType.OPENQUESTION){
            navigatorGUI.goToOpenQuestionView();
        }
        else{
            navigatorGUI.goToCloseQuestionView();
        }

    }


    @FXML
    void handleNavHome(ActionEvent event) {

    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        
    }

    private void showAlert(String title, String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

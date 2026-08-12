package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

    private NavigatorGUI navigatorGUI;
    private Parent root;

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) {
        this.navigatorGUI = navigatorGUI;
    }

    public void setView(Parent root) {
        this.root = root;
    }

    public NavigatorGUI getNavigatorGUI() {
        return navigatorGUI;
    }

    public Parent getView() {
        return root;
    }


    public void prepare() {
        testoDomandaArea.clear();
        punteggioComboBox.getSelectionModel().clearSelection();
        setUsernameBundle();
        ObservableList<Integer> scores = FXCollections.observableArrayList();
        for (int i = 1; i <= 100; i++) {
            scores.add(i);
        }
        punteggioComboBox.setItems(scores);
    }

    public void setUsernameBundle() {
        String username = navigatorGUI.getContext().getSession().getProfessor().getName() + " " + navigatorGUI.getContext().getSession().getProfessor().getSurname();
        profNameLabel.setText(username);
    }

    // --- Azioni dei Bottoni ---

    @FXML
    void handleSalvaDomanda(ActionEvent event) {
        String header = testoDomandaArea.getText();
        Integer maxScore = punteggioComboBox.getValue();

        QuestionBean qb = new QuestionBean(header, maxScore);
        navigatorGUI.getContext().setQuestions(qb);

        navigatorGUI.setPreviousView(Views.OPENQUESTIONVIEW);
        navigatorGUI.goToRecapView();
    }

    @FXML
    void handleIndietro(ActionEvent event) {
        // Logica per tornare alla schermata precedente
    }

    @FXML
    void handleNavHome(ActionEvent event) {

    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {

    }
}

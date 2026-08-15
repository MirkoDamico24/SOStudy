package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.ProfessorBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.controller.CreateTestController;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.Views;
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
import java.util.List;

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

    public void prepare() {
        setUsernameBundle();

        try {
            CreateTestController createTestController = new CreateTestController();
            String profEmail = navigatorGUI.getContext().getSession().getProfessor().getEmail();
            List<VirtualClassBean> professorClass = createTestController.getProfessorClasses(profEmail);
            ObservableList<String> classes = FXCollections.observableArrayList();
            for (VirtualClassBean vClassBean : professorClass) {
                classes.add(vClassBean.getClassName());
            }

            classeComboBox.setItems(classes);

        } catch (ControllerException e) {
            showAlert("Errore di risorsa", e.getMessage(), "Riprovare");
        }

        ObservableList<String> orari = FXCollections.observableArrayList(
                "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00",
                "04:30", "05:00", "05:30", "06:00", "06:30", "07:00", "07:30",
                "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "11:00", "11:30", "12:00", "12:30", "14:00", "14:30",
                "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
                "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00",
                "22:30",  "23:00", "23:30"
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

        if(navigatorGUI.getPreviousView() ==  Views.RECAP) {
            navigatorGUI.setPreviousView(Views.CREATETEST);
            navigatorGUI.goToRecapView();
        }
        else {
            navigatorGUI.setPreviousView(Views.CREATETEST);
            if (NavigatorGUI.showPopUp() == QuestionType.OPENQUESTION) {
                navigatorGUI.goToOpenQuestionView();
            } else {
                navigatorGUI.goToCloseQuestionView();
            }
        }

    }


    @FXML
    void handleNavHome(ActionEvent event) {
        navigatorGUI.setPreviousView(Views.CREATETEST);
        navigatorGUI.goToHomeView();
    }

    @FXML
    void handleNavClassiVirtuali(ActionEvent event) {
        navigatorGUI.setPreviousView(Views.CREATETEST);
        navigatorGUI.goToClassesView();
    }


    private void showAlert(String title, String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}

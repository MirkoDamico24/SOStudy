package it.uniroma2.dicii.ispw.sostudy.view.gui;

import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;


public class VirtualClassesViewControllerGUI extends BaseControllerGUI{
    @FXML private Button btnCreaClasse;
    @FXML private Button btnNotifiche;

    // --- Nav Bar ---
    @FXML private Button btnCreaTest;

    // --- Header ---
    @FXML private ImageView profAvatar;
    @FXML private Label profNameLabel;

    // --- Contenitore Classi ---
    @FXML private FlowPane classiFlowPane;

    public void prepare() {
        configureViewByRole(navigatorGUI.getContext().getSession().getCurrentRole() == UserRole.PROFESSOR);
        setUsernameBundle();

        KnowledgeEvaluationController ctrl = new KnowledgeEvaluationController();
        List<VirtualClassBean> classes = null;

        try{
            classes = ctrl.getUserClasses(navigatorGUI.getSession());
        }
        catch(ControllerException e){
            this.showAlert("Errore.", e.getMessage(), "Riprovare");
        }

        populateClasses(classes);
    }


    public void configureViewByRole(boolean isProfessore) {
        if (!isProfessore) {
            btnCreaClasse.setVisible(false);
            btnCreaTest.setVisible(false);
            btnCreaTest.setManaged(false);
        }
    }

    public void setUsernameBundle() {
        profNameLabel.setText(getFormattedUsername());
    }


    public void populateClasses(List<VirtualClassBean> classes) {
        classiFlowPane.getChildren().clear();
        for (VirtualClassBean vcls : classes) {
            VBox card = createClassCard(vcls);
            classiFlowPane.getChildren().add(card);
        }
    }

    private VBox createClassCard(VirtualClassBean virtualClassBean) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(320);
        card.setPrefHeight(280);
        card.setStyle("-fx-background-color: #F5FCFF; -fx-border-color: #8CB8F5; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12;");
        card.setPadding(new Insets(20));

        ImageView imageView = new ImageView();
        imageView.setFitWidth(280);
        imageView.setFitHeight(150);

        VBox imagePlaceholder = new VBox();
        imagePlaceholder.setPrefSize(280, 150);
        imagePlaceholder.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 8;");

        Label classNameLabel = new Label(virtualClassBean.getClassName());
        classNameLabel.setFont(new Font("Serif Bold", 24));
        classNameLabel.setStyle("-fx-text-fill: #555555;");
        classNameLabel.setUnderline(true);
        classNameLabel.setCursor(Cursor.HAND);

        classNameLabel.setOnMouseClicked(event -> {
            getNavigatorGUI().getSession().setCurrentClass(virtualClassBean);
            getNavigatorGUI().goToInsideClassView();
        });

        Label professorLabel = new Label(virtualClassBean.getProfessor().getName() + " " + virtualClassBean.getProfessor().getSurname());
        professorLabel.setFont(new Font("System Regular", 18));
        professorLabel.setStyle("-fx-text-fill: #777777;");

        card.getChildren().addAll(imagePlaceholder, classNameLabel, professorLabel);

        return card;
    }

    // --- AZIONI BOTTONI ---

    @FXML
    void handleCreaClasse(ActionEvent event) {
        //not yet implemented, different use case
    }
}

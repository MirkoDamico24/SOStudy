package it.uniroma2.dicii.ispw.sostudy.view;

import it.uniroma2.dicii.ispw.sostudy.bean.VirtualClassBean;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class VirtualClassesViewControllerGUI {
    @FXML private Button btnCreaClasse;
    @FXML private Button btnNotifiche;

    // --- Nav Bar ---
    @FXML private Button btnCreaTest;

    // --- Header ---
    @FXML private ImageView profAvatar;
    @FXML private Label profNameLabel;

    // --- Contenitore Classi ---
    @FXML private FlowPane classiFlowPane;

    private NavigatorGUI navigatorGUI;
    private Parent root;

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) {
        this.navigatorGUI = navigatorGUI;
    }

    public void setView(Parent root) {
        this.root = root;
    }

    public Parent getView(){
        return root;
    }


    public void prepare() {
        configureViewByRole(navigatorGUI.getContext().getSession().getCurrentRole() == UserRole.PROFESSOR);

        /*// 2. Creiamo una lista fittizia di classi
        List<VirtualClassBean> mieClassi = Arrays.asList(
                new ClasseMock("C1", "Informatica - 5A", "Prof. Mario Rossi", null),
                new ClasseMock("C2", "Matematica - 3B", "Prof. Luigi Verdi", null),
                new ClasseMock("C3", "Sistemi e Reti - 5A", "Prof. Mario Rossi", null),
                new ClasseMock("C4", "Informatica - 4A", "Prof. Mario Rossi", null)
        );

        // 3. Popoliamo la schermata
        popolaClassi(mieClassi);*/
    }

    /**
     * Adatta l'interfaccia in base al tipo di utente loggato.
     * @param isProfessore true se è prof, false se è studente
     */
    public void configureViewByRole(boolean isProfessore) {
        if (!isProfessore) {
            btnCreaClasse.setVisible(false);
            btnCreaTest.setVisible(false);
            btnCreaTest.setManaged(false);
        }
    }

    /**
     * Pulisce la vista e inserisce tutte le card delle classi.
     */
    /*public void popolaClassi(List<ClasseMock> classi) {
        classiFlowPane.getChildren().clear();
        for (ClasseMock classe : classi) {
            VBox card = creaCardClasse(classe);
            classiFlowPane.getChildren().add(card);
        }
    }*/

    /**
     * Costruisce graficamente la singola Card della classe.
     */
    private VBox creaCardClasse(VirtualClassBean cls) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(320);
        card.setPrefHeight(280);
        card.setStyle("-fx-background-color: #F5FCFF; -fx-border-color: #8CB8F5; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12;");
        card.setPadding(new Insets(20));

        ImageView imgView = new ImageView();
        imgView.setFitWidth(280);
        imgView.setFitHeight(150);

        VBox imagePlaceholder = new VBox();
        imagePlaceholder.setPrefSize(280, 150);
        imagePlaceholder.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 8;");

        // --- COLORE NOME CLASSE ---
        Label lblNomeClasse = new Label(cls.getClassName());
        lblNomeClasse.setFont(new Font("Serif Bold", 24));
        lblNomeClasse.setStyle("-fx-text-fill: #555555;");
        lblNomeClasse.setUnderline(true);
        lblNomeClasse.setCursor(Cursor.HAND);

        lblNomeClasse.setOnMouseClicked(event -> {
        });

        // --- COLORE NOME PROFESSORE ---
        Label lblProfessore = new Label(cls.getProfessor().getName());
        lblProfessore.setFont(new Font("System Regular", 18));
        lblProfessore.setStyle("-fx-text-fill: #777777;");

        card.getChildren().addAll(imagePlaceholder, lblNomeClasse, lblProfessore);

        return card;
    }

    // --- AZIONI BOTTONI ---

    @FXML
    void handleCreaClasse(ActionEvent event) {

    }
}

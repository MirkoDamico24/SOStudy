module it.uniroma2.dicii.ispw.sostudy {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.uniroma2.dicii.ispw.sostudy to javafx.fxml;
    exports it.uniroma2.dicii.ispw.sostudy;
    exports it.uniroma2.dicii.ispw.sostudy.controller;
    opens it.uniroma2.dicii.ispw.sostudy.controller to javafx.fxml;
    opens it.uniroma2.dicii.ispw.sostudy.view to javafx.fxml;
}
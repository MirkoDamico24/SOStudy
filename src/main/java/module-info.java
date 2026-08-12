module it.uniroma2.dicii.ispw.sostudy {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens it.uniroma2.dicii.ispw.sostudy to javafx.fxml;
    exports it.uniroma2.dicii.ispw.sostudy.controller;
    opens it.uniroma2.dicii.ispw.sostudy.controller to javafx.fxml;
    opens it.uniroma2.dicii.ispw.sostudy.view to javafx.fxml;
    exports it.uniroma2.dicii.ispw.sostudy.application;
    exports it.uniroma2.dicii.ispw.sostudy.exception;
    exports it.uniroma2.dicii.ispw.sostudy.bean;
    exports it.uniroma2.dicii.ispw.sostudy.model;
    opens it.uniroma2.dicii.ispw.sostudy.application to javafx.fxml;
}
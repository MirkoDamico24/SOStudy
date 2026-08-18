module it.uniroma2.dicii.ispw.sostudy {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;
    requires org.mariadb.jdbc;
    requires jbcrypt;
    requires jdk.compiler;
    requires org.checkerframework.checker.qual;
    requires com.sun.jna.platform;

    opens it.uniroma2.dicii.ispw.sostudy to javafx.fxml;
    exports it.uniroma2.dicii.ispw.sostudy.controller;
    opens it.uniroma2.dicii.ispw.sostudy.controller to javafx.fxml;
    opens it.uniroma2.dicii.ispw.sostudy.view to javafx.fxml;
    exports it.uniroma2.dicii.ispw.sostudy.application;
    exports it.uniroma2.dicii.ispw.sostudy.exception;
    exports it.uniroma2.dicii.ispw.sostudy.bean;
    exports it.uniroma2.dicii.ispw.sostudy.model;
    exports it.uniroma2.dicii.ispw.sostudy.eng.observer;
    opens it.uniroma2.dicii.ispw.sostudy.application to javafx.fxml;
}
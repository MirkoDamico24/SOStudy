package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;
import it.uniroma2.dicii.ispw.sostudy.view.HomeControllerCLI;
import it.uniroma2.dicii.ispw.sostudy.view.LoginControllerCLI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class NavigatorCLI extends Navigator{
    private LoginControllerCLI login;
    private HomeControllerCLI home;

    @Override
    public void startup(){
        goToLoginView();
    }

    @Override
    public void createLoginView(){
        if(this.login == null){
            login = new LoginControllerCLI();
            this.login.setNavigator(this);
        }
        this.login.handleLogin();
    }

    @Override
    public void createHomeView(){
        if(this.home == null){
            this.home = new HomeControllerCLI();
            this.home.setNavigator(this);
        }
        this.home.start();
    }
}

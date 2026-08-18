package it.uniroma2.dicii.ispw.sostudy.application;

import it.uniroma2.dicii.ispw.sostudy.view.navigator.Navigator;
import it.uniroma2.dicii.ispw.sostudy.view.navigator.NavigatorFactory;
import javafx.application.Platform;

public class Launcher {
    public static void main(String[] args) {

       Navigator nav = NavigatorFactory.getInstance().createNavigator();
       nav.startup();
    }

}

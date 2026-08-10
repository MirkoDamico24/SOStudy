package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.exception.ViewException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class NavigatorFactory {
    private static NavigatorFactory instance;

    public static synchronized NavigatorFactory getInstance() {
        if(instance == null) {
            instance = new NavigatorFactory();
        }
        return instance;
    }

    public Navigator createNavigator() throws ViewException {
            Navigator nav = new NavigatorCLI();

            try(InputStream input = new FileInputStream("src/main/resources/config.properties")) {
                Properties properties = new Properties();
                properties.load(input);

                String ui =  properties.getProperty("UI_TYPE");

                if(ui.equals("GUI")) nav = new NavigatorGUI();
            }
            catch(IOException e){
                throw new ViewException("Impossibile caricare correttamente la UI " + e.getMessage(), e.getCause());
            }

            return nav;
    }
}

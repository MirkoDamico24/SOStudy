package it.uniroma2.dicii.ispw.sostudy.application;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionFactory {
    private static Connection connection = null;

    public static Connection getConnection() throws DAOException {
        if(connection == null){
            try (InputStream input = new FileInputStream("src/main/resources/config.properties");) {
                Properties prop = new Properties();
                prop.load(input);

                String conn_url = prop.getProperty("CONNECTION_URL");
                String uname = prop.getProperty("USER_DB");
                String passwd = prop.getProperty("USER_PASSWD");

                connection = DriverManager.getConnection(conn_url, uname, passwd);
            }
            catch(IOException | SQLException e){
                e.printStackTrace();
                throw new DAOException("Unable to connect to database.");
            }
        }

        return connection;
    }
}

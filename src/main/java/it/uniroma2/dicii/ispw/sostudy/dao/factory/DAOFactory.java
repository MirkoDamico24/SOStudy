package it.uniroma2.dicii.ispw.sostudy.dao.factory;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.authentication.AuthenticationDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.message.MessageDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class DAOFactory {
    private static DAOFactory instance;

    public static synchronized DAOFactory getInstance() {
        if(instance == null) {
            try(InputStream input = new FileInputStream("src/main/resources/config.properties")){
                Properties prop = new Properties();
                prop.load(input);

                String persistency =  prop.getProperty("PERSISTENCY");
                instance = switch (persistency){
                    case "FS" -> new FSDAOFactory();
                    case "demo" -> new DemoDAOFactory();
                    default -> new DBDAOFactory();      //anyways create DBDAO
                };
            }
            catch(IOException e){
                throw new DAOException("Couldn't read properties file");
            }
        }

        return instance;
    }

    public abstract AuthenticationDAO getAuthenticationDAO();
    public abstract ProfessorDAO getProfessorDAO();
    public abstract StudentDAO getStudentDAO();
    public abstract VirtualClassDAO getVirtualClassDAO();
    public abstract TestDAO getTestDAO();
    public abstract TestAttemptDAO getTestAttemptDAO();
    public abstract QuestionDAO getQuestionDAO();
    public abstract ChoiceDAO getChoiceDAO();
    public abstract MessageDAO getMessageDAO();
}

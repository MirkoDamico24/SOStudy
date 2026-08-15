package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttemptAnswer;

public class TestAttemptDemoDAO extends TestAttemptDAO {
    @Override
    public TestAttempt getTestAttemptById(int testID) throws DAOException{
        if(this.containsKey(testID)){
            return this.getTestAttemptById(testID);
        }

        throw new DAOException("No test with the provided id exists");
    }

    @Override
    public void saveTestAttempt(TestAttempt testAttempt) throws DAOException{
        //nothing to do, test has to remain in RAM
    }

    @Override
    public void addAnswerToAttempt(TestAttemptAnswer  answer, int testID) throws DAOException{
        //no need to update TestAttempt on persistency. Answer add by application controller to model
    }
}

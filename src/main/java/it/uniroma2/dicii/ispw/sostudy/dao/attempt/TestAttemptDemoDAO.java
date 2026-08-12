package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;

public class TestAttemptDemoDAO extends TestAttemptDAO {
    @Override
    public TestAttempt getTestAttemptById(int testID) throws DAOException{
        return null;
    }

    @Override
    public void saveTestAttempt(TestAttempt testAttempt) throws DAOException{

    }

    @Override
    public int assignId(TestAttempt testAttempt){
        return 0;
    }
}

package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;

import java.util.ArrayList;
import java.util.List;

public class TestAttemptDemoDAO extends TestAttemptDAO {
    @Override
    public void saveTestAttempt(TestAttempt testAttempt) throws DAOException{
        //yet to implement
    }

    @Override
    public List<TestAttempt> getTestAttempt(int testId) throws DAOException{
        //yet to implement
        return new ArrayList<>();
    }
}

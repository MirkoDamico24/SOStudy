package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;

public abstract class TestAttemptDAO extends CacheDAO<Integer, TestAttempt> {
    public abstract TestAttempt getTestAttemptById(int testID) throws DAOException;
    public abstract void saveTestAttempt(TestAttempt testAttempt) throws DAOException;
    public abstract int assignId(TestAttempt testAttempt) throws DAOException;

}

package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TestAttemptDAO{
    /*
        contains tests that have already required attempts loading.
        Allows to determine if a test has no attempts or its attempts
        have not been loaded yet.
     */
    protected Set<Integer> loadedAttempts = new HashSet<>();

    public abstract void saveTestAttempt(TestAttempt testAttempt) throws DAOException;
    public abstract List<TestAttempt> getTestAttempt(int testId) throws DAOException;
}

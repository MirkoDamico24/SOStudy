package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;

public abstract class TestDAO extends CacheDAO<Integer, Test> {
    public abstract Test getTestById(int testId);
    public abstract void saveTest(Test test) throws DAOException;
}

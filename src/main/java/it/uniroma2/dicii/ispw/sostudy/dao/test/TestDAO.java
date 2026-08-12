package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;

public abstract class TestDAO extends CacheDAO<String, Test> {
    public abstract Test getTestByName(String testName);
    public abstract boolean testExists(String testName);
    public abstract void saveTest(Test test) throws DAOException;
}

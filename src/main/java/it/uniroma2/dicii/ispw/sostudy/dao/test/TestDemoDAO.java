package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;

public class TestDemoDAO extends TestDAO {
    @Override
    public Test getTestByName(String name){
        if(containsKey(name)){
            return getFromCache(name);
        }

        return null;
    }

    @Override
    public boolean testExists(String name){
        return containsKey(name);
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        if(testExists(test.getName())) throw new DAOException("Test already exists");
        addToCache(test.getName(), test);
    }
}

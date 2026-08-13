package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;

import java.util.Collection;
import java.util.Collections;

public class TestDemoDAO extends TestDAO {
    @Override
    public Test getTestById(int id){
        if(containsKey(id)){
            return getFromCache(id);
        }

        return null;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        int id = Collections.max(this.getKeys());
        addToCache(id, test);
    }
}

package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.List;
import java.util.Set;

public abstract class TestDAO extends CacheDAO<Integer, Test> {
    public abstract Test getTestById(int testId);
    public abstract void saveTest(Test test) throws DAOException;
    public abstract List<Test> getTestByClassId(int classId, VirtualClass virtualClass) throws DAOException;
    public abstract List<TestAttempt> getTestAttempt(Test test) throws DAOException;

    public int getTestId(String testName, String className, String profEmail) throws DAOException{
        Set<Integer> keys = this.getKeys();

        for(Integer key : keys){
            Test tmp = this.getFromCache(key);
            if(tmp.getName().equals(testName) &&
                    tmp.getVirtualClass().getName().equals(className) &&
                    tmp.getVirtualClass().getProf().getEmail().equals(profEmail)) return key;
        }

        throw new DAOException("The test " + testName + " has not been loaded yet.");
    }
}

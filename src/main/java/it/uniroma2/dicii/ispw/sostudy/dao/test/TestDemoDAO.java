package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        int id = 1;
        if(!this.getKeys().isEmpty()) id = Collections.max(this.getKeys());
        addToCache(id, test);
    }


    @Override
    public List<Test> getTestByClassId(int classId, VirtualClass virtualClass) throws DAOException{
        //TODO: implement
        return new ArrayList<>();
    }

    @Override
    public List<TestAttempt> getTestAttempt(Test test) throws DAOException{
        //TODO: implement
        return new ArrayList<>();
    }

}

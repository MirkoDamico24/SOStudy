package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.model.Test;

public class TestFSDAO extends TestDAO {
    @Override
    public Test getTestByName(String name){
        return null;
    }

    @Override
    public boolean testExists(String name){
        return false;
    }

    @Override
    public void saveTest(Test test){

    }
}

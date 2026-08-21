package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.List;

public abstract class TestDAO extends CacheDAO<Integer, Test> {
    public abstract Test getTestById(int testId);
    public abstract void saveTest(Test test) throws DAOException;
    public abstract List<Test> getTestByClassId(int classId, VirtualClass virtualClass) throws DAOException;
    public abstract int getTestId(String testName, String className) throws DAOException;
}

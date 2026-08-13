package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.List;

public abstract class VirtualClassDAO extends CacheDAO<Integer, VirtualClass> {
    public abstract VirtualClass getVirtualClassById(int id) throws DAOException;
    public abstract List<Test> getClassTests(int classId) throws DAOException;
    public abstract List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException;
}

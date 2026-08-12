package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.List;

public abstract class VirtualClassDAO extends CacheDAO<String, VirtualClass> {
    public abstract VirtualClass getVirtualClassByName(String name) throws DAOException;
    public abstract List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException;
}

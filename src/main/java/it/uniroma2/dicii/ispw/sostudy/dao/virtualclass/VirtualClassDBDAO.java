package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.List;

public class VirtualClassDBDAO extends VirtualClassDAO{
    @Override
    public VirtualClass getVirtualClassByName(String name) throws DAOException {
        return null;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail){
        return null;
    }
}

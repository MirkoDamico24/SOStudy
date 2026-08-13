package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.List;

public class VirtualClassDemoDAO extends VirtualClassDAO {
    @Override
    public VirtualClass getVirtualClassById(int id) throws DAOException {
        if(containsKey(id)){
            return getFromCache(id);
        }

        return new VirtualClass("ISPWvirtualclass", id, new Professor("Mario", "Rossi", "mario.rossi@gmail.com"),
                new Student(1234, "Gisueppe", "Bianchi", "giuseppe.bianchi@gmail.com"));
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail){
        return null;
    }

    @Override
    public List<Test> getClassTests(int classId){
        return null;
    }
}

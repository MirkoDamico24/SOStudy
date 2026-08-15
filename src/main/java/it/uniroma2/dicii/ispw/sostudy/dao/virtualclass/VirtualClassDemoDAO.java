package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VirtualClassDemoDAO extends VirtualClassDAO {
    @Override
    public VirtualClass getVirtualClassById(int id) throws DAOException {
        if(containsKey(id)){
            return getFromCache(id);
        }

        VirtualClass vcls = new VirtualClass("ISPWvirtualclass", id, new Professor("Mario", "Rossi", "mario.rossi@gmail.com"),
                new Student(1234, "Gisueppe", "Bianchi", "giuseppe.bianchi@gmail.com"));

        this.addToCache(id, vcls);
        return vcls;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail){
        List<VirtualClass> vcls = new ArrayList<>();
        Set<Integer> classKeys = this.getKeys();        //classes exist only in RAM
        for(int classId : classKeys){
            VirtualClass virtualClass = this.getFromCache(classId);
            if(virtualClass.getProf().getEmail().equals(profEmail)){
                vcls.add(virtualClass);
            }
        }
        return vcls;
    }

    @Override
    public void getClassTests(int classId){
        //nothing to do: all tests already in RAM
    }

    @Override
    public void getClassStudents(int classId) {
        //nothing to do: all students already in RAM
    }
}

package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

public class VirtualClassDemoDAO extends VirtualClassDAO {
    @Override
    public VirtualClass getVirtualClassByName(String name) throws DAOException {
        if(containsKey(name)){
            return getFromCache(name);
        }

        return new VirtualClass("ISPWvirtualclass", new Professor("Mario", "Rossi", "mario.rossi@gmail.com"),
                new Student(1234, "Gisueppe", "Bianchi", "giuseppe.bianchi@gmail.com"));
    }
}

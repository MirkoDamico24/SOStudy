package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VirtualClassDemoDAO extends VirtualClassDAO {

    private void populateClasses(){
        Professor prof = DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail("mario.rossi@gmail.com");
        Student stud = DAOFactory.getInstance().getStudentDAO().getStudentByEmail("giuseppe.bianchi@gmail.com");

        VirtualClass v1 = new VirtualClass("ISPWvirtualClass", prof, stud);
        this.addToCache(1, v1);
    }

    @Override
    public VirtualClass getVirtualClassById(int id) throws DAOException {
        if(containsKey(id)){
            return getFromCache(id);
        }

        VirtualClass vcls = new VirtualClass("ISPWvirtualclass", new Professor("Mario", "Rossi", "mario.rossi@gmail.com"),
                new Student("Gisueppe", "Bianchi", "giuseppe.bianchi@gmail.com"));

        this.addToCache(id, vcls);
        return vcls;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail){
        List<VirtualClass> vcls = new ArrayList<>();
        Set<Integer> classKeys = this.getKeys();        //classes exist only in RAM
        if(classKeys == null || classKeys.isEmpty()){
            populateClasses();
            classKeys = this.getKeys();
        }

        for(int classId : classKeys){
            VirtualClass virtualClass = this.getFromCache(classId);
            if(virtualClass.getProf().getEmail().equals(profEmail)){
                vcls.add(virtualClass);
            }
        }
        return vcls;
    }

    @Override
    public int getClassID(String className, String profEmail) throws DAOException{
        //TODO: implement
        return 0;
    }

    @Override
    public List<VirtualClass> getClassesByStudent(String studentEmail) throws DAOException{
        //TODO: implement
        return new ArrayList<>();
    }
}

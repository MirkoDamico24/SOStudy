package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.dao.CacheDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.List;

public abstract class VirtualClassDAO extends CacheDAO<Integer, VirtualClass> {
    public abstract VirtualClass getVirtualClassById(int id) throws DAOException;
    public abstract List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException;
    public abstract List<VirtualClass> getClassesByStudent(String studentEmail) throws DAOException;
    public abstract int getClassID(String className, String profEmail) throws DAOException;

    public void modelWiring(VirtualClass virtualClass, Professor professor, List<Student> students) {
        professor.addClass(virtualClass);
        for(Student student : students){
            student.addClass(virtualClass);
        }
    }
}

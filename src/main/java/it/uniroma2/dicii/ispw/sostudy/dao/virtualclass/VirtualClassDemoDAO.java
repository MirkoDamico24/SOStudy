package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VirtualClassDemoDAO extends VirtualClassDAO {
    private DAOFactory factory = DAOFactory.getInstance();
    private TestDAO testDAO = factory.getTestDAO();

    private void populateClasses(){
        Professor prof = factory.getProfessorDAO().getProfessorByEmail("mario.rossi@gmail.com");
        Student stud = factory.getStudentDAO().getStudentByEmail("giuseppe.bianchi@gmail.com");

        List<Student> students = new ArrayList<>();
        students.add(stud);

        VirtualClass v1 = new VirtualClass("ISPWvirtualClass", prof, students);

        List<Test> tests = testDAO.getTestByClassId(1, v1);
        v1.setAssignedTests(tests);

        this.modelWiring(v1, prof, students);

        this.addToCache(1, v1);
    }

    @Override
    public VirtualClass getVirtualClassById(int id) throws DAOException {
        if(containsKey(id)){
            return getFromCache(id);
        }

        Set<Integer> classKeys = this.getKeys();
        if(classKeys == null || classKeys.isEmpty()){
            populateClasses();
        }

        if(containsKey(id)){
            return getFromCache(id);
        }

        return null;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail){
        List<VirtualClass> vcls = new ArrayList<>();
        Set<Integer> classKeys = this.getKeys();
        if(classKeys == null || classKeys.isEmpty()){
            populateClasses();
            classKeys = this.getKeys();
        }

        for(int classId : classKeys){
            VirtualClass virtualClass = this.getVirtualClassById(classId);
            if(virtualClass.getProf().getEmail().equals(profEmail)){
                vcls.add(virtualClass);
            }
        }
        return vcls;
    }


    @Override
    public List<VirtualClass> getClassesByStudent(String studentEmail) throws DAOException{
        List<VirtualClass> vcls = new ArrayList<>();
        Set<Integer> classKeys = this.getKeys();
        if(classKeys == null || classKeys.isEmpty()){
            populateClasses();
            classKeys = this.getKeys();
        }

        for(int classId : classKeys){
            VirtualClass virtualClass = this.getVirtualClassById(classId);
            for(Student student : virtualClass.getStudents()){
                if(student.getEmail().equals(studentEmail)){
                    vcls.add(virtualClass);
                    break;
                }
            }
        }
        return vcls;
    }
}
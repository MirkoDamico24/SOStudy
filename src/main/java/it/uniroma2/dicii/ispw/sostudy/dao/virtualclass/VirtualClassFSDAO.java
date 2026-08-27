package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirtualClassFSDAO extends VirtualClassDAO {

    private static final String FILE_PATH = "data/VirtualClass.JSON";
    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "classId";
    private static final String KEY_PROF = "professor";
    private static final String KEY_STUDENTS = "students";
    private static final String KEY_EMAIL = "email";

    private DAOFactory factory = DAOFactory.getInstance();
    private ProfessorDAO profDAO = factory.getProfessorDAO();
    private StudentDAO studentDAO = factory.getStudentDAO();
    private TestDAO testDAO = factory.getTestDAO();

    @Override
    public VirtualClass getVirtualClassById(int id) throws DAOException {
        if (this.containsKey(id)) {
            return this.getFromCache(id);
        }

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(KEY_ID) && jsonObject.getInt(KEY_ID) == id) {
                    return buildVirtualClassFromJson(jsonObject, id);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error reading virtual class data by id. " + e.getMessage());
        }

        return null;
    }

    private List<Student> extractClassStudents(JSONObject jsonObject) throws DAOException {
        List<Student> students = new ArrayList<>();

        if (!jsonObject.has(KEY_STUDENTS)) {
            return students;
        }

        JSONArray studentsArray = jsonObject.getJSONArray(KEY_STUDENTS);
        for (int j = 0; j < studentsArray.length(); j++) {
            JSONObject studentObj = studentsArray.getJSONObject(j);
            Student student = studentDAO.getStudentByEmail(studentObj.getString(KEY_EMAIL));
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }

    private VirtualClass buildVirtualClassFromJson(JSONObject jsonObject, int id) throws DAOException {
        String name = jsonObject.getString(KEY_NAME);
        String professorEmail = jsonObject.getString(KEY_PROF);

        Professor professor = profDAO.getProfessorByEmail(professorEmail);
        List<Student> students = extractClassStudents(jsonObject);

        VirtualClass virtualClass = new VirtualClass(name, professor, students);
        List<Test> tests = testDAO.getTestByClassId(id, virtualClass);
        virtualClass.setAssignedTests(tests);

        this.modelWiring(virtualClass, professor, students);

        this.addToCache(id, virtualClass);
        return virtualClass;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException {
        List<VirtualClass> virtualClasses = new ArrayList<>();

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(KEY_PROF) && jsonObject.getString(KEY_PROF).equals(profEmail)) {
                    int classId = jsonObject.getInt(KEY_ID);
                    virtualClasses.add(this.getVirtualClassById(classId));
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error reading virtual classes by professor email. " + e.getMessage());
        }

        return virtualClasses;
    }

    @Override
    public List<VirtualClass> getClassesByStudent(String studentEmail) throws DAOException {
        List<VirtualClass> virtualClasses = new ArrayList<>();

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(KEY_STUDENTS) && isStudentInClass(jsonObject, studentEmail)) {
                    int classId = jsonObject.getInt(KEY_ID);
                    virtualClasses.add(this.getVirtualClassById(classId));
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error reading virtual classes by student email. " + e.getMessage());
        }

        return virtualClasses;
    }

    private boolean isStudentInClass(JSONObject jsonObject, String studentEmail) {
        JSONArray studentsArray = jsonObject.getJSONArray(KEY_STUDENTS);
        for (int j = 0; j < studentsArray.length(); j++) {
            JSONObject studentObj = studentsArray.getJSONObject(j);
            if (studentObj.getString(KEY_EMAIL).equals(studentEmail)) {
                return true;
            }
        }
        return false;
    }
}
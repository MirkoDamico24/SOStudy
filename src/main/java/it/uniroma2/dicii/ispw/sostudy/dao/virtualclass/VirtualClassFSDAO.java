package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
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
    private static final String KEY_ASSIGNED_TESTS = "tests";
    private static final String KEY_EMAIL = "email";

    private List<Student> extractAssignedStudents(JSONObject jsonObject) throws DAOException {
        List<Student> students = new ArrayList<>();

        if (!jsonObject.has(KEY_STUDENTS)) {
            return students;
        }

        JSONArray studentsArray = jsonObject.getJSONArray(KEY_STUDENTS);
        for (int j = 0; j < studentsArray.length(); j++) {
            JSONObject studentObj = studentsArray.getJSONObject(j);
            Student student = DAOFactory.getInstance().getStudentDAO()
                    .getStudentByEmail(studentObj.getString(KEY_EMAIL));
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }

    @Override
    public void getClassTests(int classId) throws DAOException {
        if (!this.containsKey(classId)) {
            throw new DAOException("Virtual class not present in cache");
        }

        VirtualClass virtualClass = this.getFromCache(classId);
        List<Test> tests = new ArrayList<>();
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(KEY_ID) && jsonObject.getInt(KEY_ID) == classId) {
                    tests = extractAssignedTests(jsonObject);
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error reading virtual class data by id for tests");
        }

        virtualClass.setAssignedTests(tests);
    }

    private List<Test> extractAssignedTests(JSONObject jsonObject) throws DAOException {
        List<Test> tests = new ArrayList<>();

        if (!jsonObject.has(KEY_ASSIGNED_TESTS)) {
            return tests;
        }

        JSONArray testsArray = jsonObject.getJSONArray(KEY_ASSIGNED_TESTS);
        for (int j = 0; j < testsArray.length(); j++) {
            JSONObject testObj = testsArray.getJSONObject(j);
            Test test = DAOFactory.getInstance().getTestDAO().getTestById(testObj.getInt("testId"));
            if (test != null) {
                tests.add(test);
            }
        }

        return tests;
    }

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
        } catch (Exception e) {
            throw new DAOException("Error reading virtual class data by id");
        }
        return null;
    }

    private VirtualClass buildVirtualClassFromJson(JSONObject jsonObject, int id) throws DAOException {
        String name = jsonObject.getString(KEY_NAME);
        Professor professor = DAOFactory.getInstance().getProfessorDAO()
                .getProfessorByEmail(jsonObject.getString(KEY_PROF));

        List<Student> students = jsonObject.has(KEY_STUDENTS)
                ? extractAssignedStudents(jsonObject) : null;

        VirtualClass virtualClass = new VirtualClass(name, id, professor, students);
        this.addToCache(id, virtualClass);
        return virtualClass;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException {
        List<VirtualClass> classes = new ArrayList<>();
        VirtualClass virtualClass = null;
        try {

            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(KEY_PROF) && jsonObject.getString(KEY_PROF).equals(profEmail)) {
                    int classId = jsonObject.getInt(KEY_ID);
                    if(!this.containsKey(classId)) virtualClass = buildVirtualClassFromJson(jsonObject, classId);
                    else virtualClass = this.getFromCache(classId);

                    if (virtualClass != null) classes.add(virtualClass);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error reading virtual classes by professor");
        }
        return classes;
    }

    private int generateNextId() throws DAOException {
        int maxId = 0;
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(KEY_ID)) {
                    int currentId = jsonObject.getInt(KEY_ID);
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                }
            }
        } catch (IOException e) {
            return 1;
        }
        return maxId + 1;
    }
}
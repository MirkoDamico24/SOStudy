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
    private static final String KEY_PROF = "professor";
    private static final String KEY_STUDENTS = "students";
    private static final String KEY_ASSIGNED_TESTS = "tests";
    private static final String KEY_EMAIL = "email";

    private List<Student> getClassStudents(JSONArray studentsArray) throws DAOException {
        List<Student> students = new ArrayList<>();
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

    private List<Test> getClassTests(JSONArray testsArray) throws DAOException {
        List<Test> tests = new ArrayList<>();
        for (int j = 0; j < testsArray.length(); j++) {
            JSONObject testObj = testsArray.getJSONObject(j);
            Test test = DAOFactory.getInstance().getTestDAO()
                    .getTestByName(testObj.getString(KEY_NAME));
            if (test != null) {
                tests.add(test);
            }
        }
        return tests;
    }

    @Override
    public VirtualClass getVirtualClassByName(String name) throws DAOException {
        if (this.containsKey(name)) {
            return this.getFromCache(name);
        }

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString(KEY_NAME).equals(name)) {
                    return buildVirtualClassFromJson(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Error reading virtual class data by name");
        }
        return null;
    }

    private VirtualClass buildVirtualClassFromJson(JSONObject jsonObject) throws DAOException {
        String name = jsonObject.getString(KEY_NAME);
        Professor professor = DAOFactory.getInstance().getProfessorDAO()
                .getProfessorByEmail(jsonObject.getString(KEY_PROF));

        List<Student> students = jsonObject.has(KEY_STUDENTS)
                ? getClassStudents(jsonObject.getJSONArray(KEY_STUDENTS)) : null;

        List<Test> tests = jsonObject.has(KEY_ASSIGNED_TESTS)
                ? getClassTests(jsonObject.getJSONArray(KEY_ASSIGNED_TESTS)) : null;

        VirtualClass virtualClass = new VirtualClass(name, professor, students, tests);
        this.addToCache(name, virtualClass);
        return virtualClass;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException {
        List<VirtualClass> classes = new ArrayList<>();
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString(KEY_PROF).equals(profEmail)) {
                    VirtualClass virtualClass = this.getVirtualClassByName(jsonObject.getString(KEY_NAME));
                    if (virtualClass != null) {
                        classes.add(virtualClass);
                    }
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error reading virtual classes by professor");
        }
        return classes;
    }
}
package it.uniroma2.dicii.ispw.sostudy.dao.student;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentFSDAO extends StudentDAO {
    private static final String FILE_PATH = "data/Student.JSON";

    @Override
    public Student getStudentByEmail(String email) throws DAOException {
        if (this.containsKey(email)) {
            return this.getFromCache(email);
        }

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("email") && jsonObject.getString("email").equals(email)) {
                    return buildStudentFromJson(jsonObject, email);
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error reading professor data");
        }
        return null;
    }

    private Student buildStudentFromJson(JSONObject jsonObject, String email) throws DAOException {
        String name = jsonObject.getString("name");
        String surname = jsonObject.getString("surname");

        Student student = new Student(name, surname, email);
        this.addToCache(email, student);
        return student;
    }
}

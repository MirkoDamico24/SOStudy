package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestFSDAO extends TestDAO {

    private static final String FILE_PATH = "data/Test.JSON";
    private static final String KEY_NAME = "name";
    private static final String KEY_CLASS = "class";
    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_DUE_TIME = "dueTime";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_ID = "id";

    @Override
    public Test getTestById(int testId) throws DAOException {
        if (this.containsKey(testId)) {
            return this.getFromCache(testId);
        }

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(KEY_ID) && jsonObject.getInt(KEY_ID) == testId) {
                    return buildTest(jsonObject, testId);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while getting test by id from file system. " + e.getMessage());
        }

        return null;
    }

    private Test buildTest(JSONObject jsonObject, int testId) throws DAOException {
        String name = jsonObject.getString(KEY_NAME);
        LocalDate dueDate = LocalDate.parse(jsonObject.getString(KEY_DUE_DATE));
        LocalTime dueTime = LocalTime.parse(jsonObject.getString(KEY_DUE_TIME));
        Duration duration = extractDuration(jsonObject);

        VirtualClass virtualClass = DAOFactory.getInstance().getVirtualClassDAO()
                .getVirtualClassById(jsonObject.getInt(KEY_CLASS));

        List<Question> questions = DAOFactory.getInstance().getQuestionDAO().getQuestionsByTestId(testId);

        Test test = new Test(name, dueDate, dueTime, duration, questions, virtualClass);
        this.addToCache(testId, test);
        return test;
    }

    private Duration extractDuration(JSONObject jsonObject) {
        if (!jsonObject.has(KEY_DURATION)) {
            return null;
        }
        return Duration.parse(jsonObject.getString(KEY_DURATION));
    }

    @Override
    public List<Test> getTestByClassId(int classId, VirtualClass virtualClass) throws DAOException {
        record TestRawData(int id, String name, LocalDate dueDate, LocalTime dueTime, Duration duration) {}

        List<TestRawData> rawDataList = new ArrayList<>();
        List<Integer> testIds = new ArrayList<>();

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!jsonObject.has(KEY_ID) || jsonObject.getInt(KEY_CLASS) != classId) {
                    continue;
                }

                int id = jsonObject.getInt(KEY_ID);
                rawDataList.add(new TestRawData(
                        id,
                        jsonObject.getString(KEY_NAME),
                        LocalDate.parse(jsonObject.getString(KEY_DUE_DATE)),
                        LocalTime.parse(jsonObject.getString(KEY_DUE_TIME)),
                        extractDuration(jsonObject)
                ));
                testIds.add(id);
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while reading tests by class id from file system. " + e.getMessage());
        }

        if (testIds.isEmpty()) return new ArrayList<>();

        Map<Integer, List<Question>> questionsMap = DAOFactory.getInstance().getQuestionDAO().getQuestionsByTestIds(testIds);

        List<Test> finalTests = new ArrayList<>();
        for (TestRawData data : rawDataList) {
            if (this.containsKey(data.id())) {
                finalTests.add(this.getFromCache(data.id()));
                continue;
            }

            List<Question> testQuestions = questionsMap.getOrDefault(data.id(), new ArrayList<>());
            Test test = new Test(data.name(), data.dueDate(), data.dueTime(), data.duration(), testQuestions, virtualClass);

            this.addToCache(data.id(), test);
            finalTests.add(test);
        }

        return finalTests;
    }

    private JSONArray getJSONArray() {
        JSONArray jsonArray;
        try {
            jsonArray = JSONHelper.readJsonFile(FILE_PATH);
        } catch (IOException e) {
            jsonArray = new JSONArray();
        }
        return jsonArray;
    }

    private int generateNextId(JSONArray jsonArray) {
        int maxId = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has(KEY_ID)) {
                int currentId = jsonObject.getInt(KEY_ID);
                if (currentId > maxId) {
                    maxId = currentId;
                }
            }
        }
        return maxId + 1;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        try {
            JSONArray jsonArray = getJSONArray();
            int newId = generateNextId(jsonArray);

            int classId = DAOFactory.getInstance().getVirtualClassDAO()
                    .getClassID(test.getVirtualClass().getName(), test.getVirtualClass().getProf().getEmail());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_ID, newId);
            jsonObject.put(KEY_NAME, test.getName());
            jsonObject.put(KEY_DUE_DATE, test.getDueDate().toString());
            jsonObject.put(KEY_DUE_TIME, test.getDueTime().toString());

            if (test.getDuration() != null) {
                jsonObject.put(KEY_DURATION, test.getDuration().toString());
            }

            jsonObject.put(KEY_CLASS, classId);

            jsonArray.put(jsonObject);
            JSONHelper.writeJsonFile(FILE_PATH, jsonArray);

            this.addToCache(newId, test);

            DAOFactory.getInstance().getQuestionDAO().saveTestQuestion(newId, test.getQuestions());

        } catch (IOException | DAOException e) {
            throw new DAOException("Error occurred while saving test to file system. " + e.getMessage());
        }
    }

    @Override
    public int getTestId(String testName, String className) throws DAOException {
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (!jsonObject.has(KEY_ID) || !jsonObject.getString(KEY_NAME).equals(testName)) {
                    continue;
                }

                int classId = jsonObject.getInt(KEY_CLASS);
                VirtualClass virtualClass = DAOFactory.getInstance().getVirtualClassDAO().getVirtualClassById(classId);

                if (virtualClass != null && virtualClass.getName().equals(className)) {
                    return jsonObject.getInt(KEY_ID);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while getting test id from file system. " + e.getMessage());
        }

        return 0;
    }
}
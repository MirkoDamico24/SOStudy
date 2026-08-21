package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;
import it.uniroma2.dicii.ispw.sostudy.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestFSDAO extends TestDAO {

    private static final String FILE_PATH = "data/Test.JSON";
    private static final String KEY_NAME = "name";
    private static final String KEY_CLASS = "class";
    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_DUE_TIME = "dueTime";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_HEADER = "header";
    private static final String KEY_MAX_SCORE = "maxScore";
    private static final String KEY_OPTIONS = "options";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_IS_SOLUTION = "isSolution";
    private static final String KEY_ATTEMPTS = "testAttempts";
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
                    return this.buildTest(jsonObject, testId);
                }
            }
        } catch (Exception e) {
            throw new DAOException("File system error occurred while getting test by ID");
        }
        return null;
    }

    public Test buildTest(JSONObject jsonObject, int testId) throws DAOException {
        String name = jsonObject.getString(KEY_NAME);
        LocalDate dueDate = LocalDate.parse(jsonObject.getString(KEY_DUE_DATE));
        LocalTime dueTime = LocalTime.parse(jsonObject.getString(KEY_DUE_TIME));
        Duration duration = extractDuration(jsonObject);

        VirtualClass virtualClass = DAOFactory.getInstance().getVirtualClassDAO()
                .getVirtualClassById(jsonObject.getInt(KEY_CLASS));

        List<Question> questions = extractQuestions(jsonObject.getJSONArray(KEY_QUESTIONS));

        Test test = new Test(name, dueDate, dueTime, duration, questions, virtualClass);

        if (jsonObject.has(KEY_ATTEMPTS)) {
            attachTestAttempts(test, jsonObject.getJSONArray(KEY_ATTEMPTS));
        }

        this.addToCache(testId, test);
        return test;
    }

    private Duration extractDuration(JSONObject jsonObject) {
        if (!jsonObject.has(KEY_DURATION)) {
            return null;
        }
        return Duration.parse(jsonObject.getString(KEY_DURATION));
    }

    private List<Question> extractQuestions(JSONArray questionsArray) throws DAOException {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < questionsArray.length(); i++) {
            questions.add(parseSingleQuestion(questionsArray.getJSONObject(i)));
        }
        return questions;
    }

    private Question parseSingleQuestion(JSONObject questionObject) throws DAOException {
        String header = questionObject.getString(KEY_HEADER);
        int maxScore = questionObject.getInt(KEY_MAX_SCORE);

        if (questionObject.has(KEY_OPTIONS)) {
            return buildCloseQuestion(header, maxScore, questionObject.getJSONArray(KEY_OPTIONS));
        }

        return new OpenQuestion(header, maxScore);
    }

    private CloseQuestion buildCloseQuestion(String header, int maxScore, JSONArray optionsArray) throws DAOException {
        CloseQuestion closeQuestion = new CloseQuestion(header, maxScore);
        List<Choice> choices = new ArrayList<>();
        Choice solution = null;

        for (int j = 0; j < optionsArray.length(); j++) {
            JSONObject optionObject = optionsArray.getJSONObject(j);
            Choice choice = new Choice(optionObject.getString(KEY_CONTENT));
            choices.add(choice);

            if (optionObject.has(KEY_IS_SOLUTION) && optionObject.getBoolean(KEY_IS_SOLUTION)) {
                solution = choice;
            }
        }

        closeQuestion.addChoice(choices);
        setQuestionSolution(closeQuestion, solution);

        return closeQuestion;
    }

    private void setQuestionSolution(CloseQuestion closeQuestion, Choice solution) throws DAOException {
        if (solution == null) {
            return;
        }

        try {
            closeQuestion.addSolution(solution);
        } catch (ModelException e) {
            throw new DAOException("La soluzione deve essere un'opzione di risposta!!!");
        }
    }

    private void attachTestAttempts(Test test, JSONArray attemptsArray) throws DAOException {
        /*TestAttemptDAO testAttemptDAO = DAOFactory.getInstance().getTestAttemptDAO();
        List<TestAttempt> testAttempts = new ArrayList<>();

        for (int i = 0; i < attemptsArray.length(); i++) {
            JSONObject attemptObj = attemptsArray.getJSONObject(i);
            TestAttempt attempt = testAttemptDAO.getTestAttemptById(attemptObj.getInt(KEY_ID));

            if (attempt != null) {
                testAttempts.add(attempt);
            }
        }
        test.setTests(testAttempts);*/
    }


    public JSONArray serializeQuestions(List<Question> questions) {
        JSONArray questionsArray = new JSONArray();
        for (Question question : questions) {
            JSONObject questionObject = new JSONObject();
            questionObject.put(KEY_HEADER, question.getHeader());
            questionObject.put(KEY_MAX_SCORE, question.getMaxScore());

            if (question instanceof CloseQuestion closeQuestion) {
                questionObject.put(KEY_OPTIONS, serializeChoices(closeQuestion));
            }
            questionsArray.put(questionObject);
        }
        return questionsArray;
    }

    private JSONArray serializeChoices(CloseQuestion closeQuestion) {
        JSONArray optionsArray = new JSONArray();
        Choice solution = closeQuestion.getSolution();

        for (Choice choice : closeQuestion.getChoices()) {
            JSONObject optionObject = new JSONObject();
            optionObject.put(KEY_CONTENT, choice.getContent());

            if (choice.equals(solution)) {
                optionObject.put(KEY_IS_SOLUTION, true);
            }
            optionsArray.put(optionObject);
        }
        return optionsArray;
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

    private JSONArray getJSONArray(){
        JSONArray jsonArray;
        try {
            jsonArray = JSONHelper.readJsonFile(FILE_PATH);
        } catch (IOException e) {
            jsonArray = new JSONArray();
        }
        return jsonArray;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        /*try {
            JSONArray jsonArray = getJSONArray();

            int newId = generateNextId(jsonArray);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_ID, newId);
            jsonObject.put(KEY_NAME, test.getName());
            jsonObject.put(KEY_DUE_DATE, test.getDueDate().toString());
            jsonObject.put(KEY_DUE_TIME, test.getDueTime().toString());

            if (test.getDuration() != null) {
                jsonObject.put(KEY_DURATION, test.getDuration().toString());
            }

            jsonObject.put(KEY_CLASS, test.getVirtualClass().getClassId());
            jsonObject.put(KEY_QUESTIONS, serializeQuestions(test.getQuestions()));

            if (test.getTests() != null) {
                jsonObject.put(KEY_ATTEMPTS, saveAndSerializeTestAttempts(test.getTests()));
            }

            jsonArray.put(jsonObject);
            JSONHelper.writeJsonFile(FILE_PATH, jsonArray);

            this.addToCache(newId, test);

        } catch (Exception e) {
            throw new DAOException("Error saving test");
        }*/
    }

    private JSONArray saveAndSerializeTestAttempts(List<TestAttempt> attempts) throws DAOException {
        /*TestAttemptDAO testAttemptDAO = DAOFactory.getInstance().getTestAttemptDAO();
        JSONArray testAttemptsArray = new JSONArray();

        for (TestAttempt attempt : attempts) {
            testAttemptDAO.saveTestAttempt(attempt);
            JSONObject attemptObj = new JSONObject();
            attemptObj.put(KEY_ID, attempt.getTestId());
            testAttemptsArray.put(attemptObj);
        }
        return testAttemptsArray;*/
        return null;
    }

    @Override
    public int getTestId(String testName, String className) throws DAOException{
        //TODO: implement
        return 0;
    }

    @Override
    public List<Test> getTestByClassId(int classId, VirtualClass virtualClass) throws DAOException{
        //TODO: implement
        return null;
    }
}
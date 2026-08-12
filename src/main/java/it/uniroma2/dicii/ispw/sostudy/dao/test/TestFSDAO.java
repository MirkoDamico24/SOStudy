package it.uniroma2.dicii.ispw.sostudy.dao.test;

import it.uniroma2.dicii.ispw.sostudy.dao.attempt.TestAttemptDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;
import it.uniroma2.dicii.ispw.sostudy.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestFSDAO extends TestDAO {

    private static final String FILE_PATH = "Test.JSON";

    @Override
    public Test getTestByName(String testName) {
        if (this.containsKey(testName)) {
            return this.getFromCache(testName);
        }

        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return null;
            }
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("nome").equals(testName)) {
                    return this.buildTest(jsonObject, testName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Test buildTest(JSONObject jsonObject, String testName) throws DAOException {
        String name = jsonObject.getString("nome");
        LocalDate dueDate = LocalDate.parse(jsonObject.getString("dueDate"));
        LocalTime dueTime = LocalTime.parse(jsonObject.getString("dueTime"));

        Duration duration = null;
        if (jsonObject.has("duration")) {
            duration = Duration.parse(jsonObject.getString("duration"));
        }

        VirtualClass virtualClass = DAOFactory.getInstance().getVirtualClassDAO().getVirtualClassByName(jsonObject.getString("class"));

        List<Question> questions = new ArrayList<>();
        JSONArray questionsArray = jsonObject.getJSONArray("questions");

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObject = questionsArray.getJSONObject(i);
            String header = questionObject.getString("header");
            int maxScore = questionObject.getInt("maxScore");

            if (questionObject.has("options")) {
                JSONArray optionsArray = questionObject.getJSONArray("options");
                List<Choice> choices = new ArrayList<>();
                Choice solution = null;

                for (int j = 0; j < optionsArray.length(); j++) {
                    JSONObject optionObject = optionsArray.getJSONObject(j);
                    Choice choice = new Choice(optionObject.getString("content"));
                    choices.add(choice);

                    if (optionObject.has("isSolution") && optionObject.getBoolean("isSolution")) {
                        solution = choice;
                    }
                }

                CloseQuestion closeQuestion = new CloseQuestion(header, maxScore);
                closeQuestion.addChoice(choices);
                if (solution != null) {
                    try {
                        closeQuestion.addSolution(solution);
                    } catch (ModelException e) {
                        throw new DAOException("La soluzione deve essere un'opzione di risposta!!!");
                    }
                }
                questions.add(closeQuestion);
            } else {
                OpenQuestion openQuestion = new OpenQuestion(header, maxScore);
                questions.add(openQuestion);
            }
        }

        Test test = new Test(name, dueDate, dueTime, duration, questions, virtualClass);

        if (jsonObject.has("testAttempts")) {
            TestAttemptDAO testAttemptDAO = DAOFactory.getInstance().getTestAttemptDAO();
            JSONArray attemptsArray = jsonObject.getJSONArray("testAttempts");
            List<TestAttempt> testAttempts = new ArrayList<>();

            for (int i = 0; i < attemptsArray.length(); i++) {
                JSONObject attemptObj = attemptsArray.getJSONObject(i);
                int attemptId = attemptObj.getInt("id");

                TestAttempt attempt = testAttemptDAO.getTestAttemptById(attemptId);
                if (attempt != null) {
                    testAttempts.add(attempt);
                }
            }
            test.setTests(testAttempts);
        }

        this.addToCache(testName, test);
        return test;
    }

    @Override
    public boolean testExists(String testName) {
        if (this.containsKey(testName)) {
            return true;
        }

        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return false;
            }
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("nome").equals(testName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONArray serializeQuestions(List<Question> questions) {
        JSONArray questionsArray = new JSONArray();
        for (Question question : questions) {
            JSONObject questionObject = new JSONObject();
            questionObject.put("header", question.getHeader());
            questionObject.put("maxScore", question.getMaxScore());

            if (question instanceof CloseQuestion closeQuestion) {
                JSONArray optionsArray = new JSONArray();
                for (Choice choice : closeQuestion.getChoices()) {
                    JSONObject optionObject = new JSONObject();
                    optionObject.put("content", choice.getContent());

                    Choice solution = closeQuestion.getSolution();
                    if (solution != null && choice == solution) {
                        optionObject.put("isSolution", true);
                    }
                    optionsArray.put(optionObject);
                }
                questionObject.put("options", optionsArray);
            }
            questionsArray.put(questionObject);
        }
        return questionsArray;
    }

    @Override
    public void saveTest(Test test) throws DAOException {
        Path path = Paths.get(FILE_PATH);
        try {
            JSONArray jsonArray = new JSONArray();
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                if (!content.trim().isEmpty()) {
                    jsonArray = new JSONArray(content);
                }
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).has("nome") && jsonArray.getJSONObject(i).getString("nome").equals(test.getName())) {
                    jsonArray.remove(i);
                    break;
                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nome", test.getName());
            jsonObject.put("dueDate", test.getDueDate().toString());
            jsonObject.put("dueTime", test.getDueTime().toString());

            if (test.getDuration() != null) {
                jsonObject.put("duration", test.getDuration().toString());
            }

            jsonObject.put("class", test.getVirtualClass().getName());

            JSONArray questionsArray = serializeQuestions(test.getQuestions());
            jsonObject.put("questions", questionsArray);

            if (test.getTests() != null) {
                TestAttemptDAO testAttemptDAO = DAOFactory.getInstance().getTestAttemptDAO();
                JSONArray testAttemptsArray = new JSONArray();

                for (TestAttempt attempt : test.getTests()) {
                    testAttemptDAO.saveTestAttempt(attempt);

                    JSONObject attemptObj = new JSONObject();
                    attemptObj.put("id", attempt.getTestId());
                    testAttemptsArray.put(attemptObj);
                }

                jsonObject.put("testAttempts", testAttemptsArray);
            }

            jsonArray.put(jsonObject);

            Files.write(path, jsonArray.toString(4).getBytes());

            this.addToCache(test.getName(), test);

        } catch (Exception e) {
            throw new DAOException("Error saving test");
        }
    }
}
package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestAttemptFSDAO extends TestAttemptDAO {

    private static final String FILE_PATH = "data/Attempt.JSON";
    private static final String KEY_STUDENT = "student";
    private static final String KEY_TEST = "test";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_GRADING_STATUS = "gradingStatus";
    private static final String KEY_HAND_IN_TIME = "handInTime";
    private static final String KEY_HAND_IN_DATE = "handInDate";
    private static final String KEY_ANSWERS = "answers";
    private static final String KEY_ANSWER_CODE = "code";
    private static final String KEY_TEXTUAL_CONTENT = "textualContent";
    private static final String KEY_INTEGER_CONTENT = "integerContent";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_SCORE = "score";

    private DAOFactory factory = DAOFactory.getInstance();
    private QuestionDAO questionDAO = factory.getQuestionDAO();
    private ChoiceDAO choiceDAO = factory.getChoiceDAO();
    private TestDAO testDAO = factory.getTestDAO();

    private LocalTime parseTime(String s) {
        return (s == null || s.isBlank()) ? null : LocalTime.parse(s);
    }

    private LocalDate parseDate(String s) {
        return (s == null || s.isBlank()) ? null : LocalDate.parse(s);
    }

    private Answer<?> buildAnswer(JSONObject answerObject) {
        int score = answerObject.has(KEY_SCORE) ? answerObject.getInt(KEY_SCORE) : 0;
        int questionId = answerObject.getInt(KEY_QUESTION);
        Question question = questionDAO.getQuestionById(questionId);

        if (answerObject.has(KEY_INTEGER_CONTENT)) {
            int choiceId = answerObject.getInt(KEY_INTEGER_CONTENT);
            Choice choice = choiceDAO.getChoiceById(choiceId);
            return new Answer<>(score, choice, question);
        } else {
            String textualContent = answerObject.optString(KEY_TEXTUAL_CONTENT, null);
            return new Answer<>(score, textualContent, question);
        }
    }

    @Override
    public List<TestAttempt> getTestAttempt(int testId) throws DAOException {
        List<TestAttempt> attempts = new ArrayList<>();

        if (this.loadedAttempts.contains(testId)) {
            return attempts;
        }

        Test test = testDAO.getTestById(testId);

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject attemptObject = jsonArray.getJSONObject(i);
                if (!attemptObject.has(KEY_TEST) || attemptObject.getInt(KEY_TEST) != testId) {
                    continue;
                }

                Student student = factory.getStudentDAO().getStudentByEmail(attemptObject.getString(KEY_STUDENT));
                int grade = attemptObject.has(KEY_GRADE) ? attemptObject.getInt(KEY_GRADE) : 0;
                TestGradingStatus gradingStatus = TestGradingStatus.valueOf(attemptObject.getString(KEY_GRADING_STATUS));
                LocalTime handInTime = parseTime(attemptObject.optString(KEY_HAND_IN_TIME, null));
                LocalDate handInDate = parseDate(attemptObject.optString(KEY_HAND_IN_DATE, null));

                List<Answer<?>> answers = new ArrayList<>();
                if (attemptObject.has(KEY_ANSWERS)) {
                    JSONArray answersArray = attemptObject.getJSONArray(KEY_ANSWERS);
                    for (int j = 0; j < answersArray.length(); j++) {
                        answers.add(buildAnswer(answersArray.getJSONObject(j)));
                    }
                }

                attempts.add(new TestAttempt(test, answers, student, grade, gradingStatus, handInTime, handInDate));
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while fetching test attempts from file system. " + e.getMessage());
        }

        test.setTests(attempts);
        loadedAttempts.add(testId);
        return attempts;
    }

    private JSONArray serializeAnswers(List<Answer<?>> answers, int testId) {
        JSONArray answersArray = new JSONArray();
        int answerCode = 1;

        for (Answer<?> answer : answers) {
            int questionId = questionDAO.getQuestionId(answer.getQuestion(), testId);

            JSONObject answerObject = new JSONObject();
            answerObject.put(KEY_ANSWER_CODE, answerCode++);
            answerObject.put(KEY_QUESTION, questionId);
            answerObject.put(KEY_SCORE, answer.getScore());

            Object content = answer.getContent();
            if (content instanceof Choice selected) {
                int choiceId = choiceDAO.getChoiceId(selected, questionId);
                answerObject.put(KEY_INTEGER_CONTENT, choiceId);
            } else if (content instanceof String textualContent) {
                answerObject.put(KEY_TEXTUAL_CONTENT, textualContent);
            }

            answersArray.put(answerObject);
        }

        return answersArray;
    }

    @Override
    public void saveTestAttempt(TestAttempt testAttempt) throws DAOException {
        Test test = testAttempt.getTest();
        int testId = testDAO.getTestId(test.getName(), test.getVirtualClass().getName(), test.getVirtualClass().getProf().getEmail());

        JSONObject attemptObject = new JSONObject();
        attemptObject.put(KEY_STUDENT, testAttempt.getStudent().getEmail());
        attemptObject.put(KEY_TEST, testId);
        attemptObject.put(KEY_GRADE, testAttempt.getGrade());
        attemptObject.put(KEY_GRADING_STATUS, testAttempt.getTestGradingStatus().name());
        attemptObject.put(KEY_HAND_IN_TIME, testAttempt.getHandInTime() != null ? testAttempt.getHandInTime().toString() : "");
        attemptObject.put(KEY_HAND_IN_DATE, testAttempt.getHandInDate() != null ? testAttempt.getHandInDate().toString() : "");
        attemptObject.put(KEY_ANSWERS, serializeAnswers(testAttempt.getAnswers(), testId));

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            jsonArray.put(attemptObject);
            JSONHelper.writeJsonFile(FILE_PATH, jsonArray);

            loadedAttempts.add(testId);
        } catch (IOException e) {
            throw new DAOException("Error occurred while saving attempt data to file system. " + e.getMessage());
        }
    }

    private JSONObject findAttemptObject(JSONArray jsonArray, int testId, String studentEmail) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject candidate = jsonArray.getJSONObject(i);
            if (candidate.has(KEY_TEST) && candidate.getInt(KEY_TEST) == testId
                    && candidate.has(KEY_STUDENT) && candidate.getString(KEY_STUDENT).equals(studentEmail)) {
                return candidate;
            }
        }
        return null;
    }

    private void updateAnswersScore(JSONObject attemptObject, TestAttempt testAttempt, int testId) {
        if (!attemptObject.has(KEY_ANSWERS)) {
            return;
        }

        JSONArray answersArray = attemptObject.getJSONArray(KEY_ANSWERS);

        for (Answer<?> answer : testAttempt.getAnswers()) {
            Integer questionId = questionDAO.getQuestionId(answer.getQuestion(), testId);
            if (questionId == null) {
                continue;
            }

            for (int i = 0; i < answersArray.length(); i++) {
                JSONObject answerObject = answersArray.getJSONObject(i);
                if (answerObject.has(KEY_QUESTION) && answerObject.getInt(KEY_QUESTION) == questionId) {
                    answerObject.put(KEY_SCORE, answer.getScore());
                    break;
                }
            }
        }
    }

    @Override
    public void updateTestAttempt(TestAttempt testAttempt) throws DAOException {
        Test test = testAttempt.getTest();
        int testId = testDAO.getTestId(test.getName(), test.getVirtualClass().getName(), test.getVirtualClass().getProf().getEmail());
        String studentEmail = testAttempt.getStudent().getEmail();

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject attemptObject = findAttemptObject(jsonArray, testId, studentEmail);

            if (attemptObject == null) {
                throw new DAOException("Attempt to update was not found in the file system.");
            }

            attemptObject.put(KEY_GRADING_STATUS, TestGradingStatus.FULLYGRADED.name());
            attemptObject.put(KEY_GRADE, testAttempt.getGrade());

            updateAnswersScore(attemptObject, testAttempt, testId);

            JSONHelper.writeJsonFile(FILE_PATH, jsonArray);
        } catch (IOException e) {
            throw new DAOException("Error occurred while updating attempt data to file system. " + e.getMessage());
        }
    }

    @Override
    public boolean checkAlreadyDone(Test test, Student student) throws DAOException {
        int testId = testDAO.getTestId(test.getName(), test.getVirtualClass().getName(), test.getVirtualClass().getProf().getEmail());

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            return findAttemptObject(jsonArray, testId, student.getEmail()) != null;
        } catch (IOException e) {
            throw new DAOException("Error occurred while checking attempt existence in file system. " + e.getMessage());
        }
    }
}
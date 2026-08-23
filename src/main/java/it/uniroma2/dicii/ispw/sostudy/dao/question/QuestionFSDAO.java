package it.uniroma2.dicii.ispw.sostudy.dao.question;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.choice.ChoiceDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.ChoiceDTO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionDTO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.QuestionMapper;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Question;
import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class QuestionFSDAO extends QuestionDAO {

    private static final String FILE_PATH = "data/Test.JSON";
    private static final String KEY_TEST_ID = "id";
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_HEADER = "header";
    private static final String KEY_MAX_SCORE = "maxScore";
    private static final String KEY_OPTIONS = "options";

    private record CloseIndexes(QuestionDTO q, int index) {}

    //needed to compute artificial question id
    public static final int ID_MULTIPLIER = 1000;

    public static int computeQuestionId(int testId, int index) {
        return testId * ID_MULTIPLIER + index;
    }

    private int extractTestId(int questionId) {
        return questionId / ID_MULTIPLIER;
    }

    private int extractIndex(int questionId) {
        return questionId % ID_MULTIPLIER;
    }

    private Question buildQuestion(JSONObject questionObject, int questionId) {
        String header = questionObject.getString(KEY_HEADER);
        int maxScore = questionObject.getInt(KEY_MAX_SCORE);

        QuestionType type = questionObject.has(KEY_OPTIONS) ? QuestionType.CLOSEQUESTION : QuestionType.OPENQUESTION;

        QuestionDTO dto;
        switch (type) {
            case CLOSEQUESTION -> {
                ChoiceDAO choiceDAO = DAOFactory.getInstance().getChoiceDAO();
                ChoiceDTO choices = choiceDAO.getChoicesByQuestionId(questionId);
                dto = new QuestionDTO(header, maxScore, QuestionType.CLOSEQUESTION, choices.options(), choices.solution());
            }
            case OPENQUESTION -> dto = new QuestionDTO(header, maxScore, QuestionType.OPENQUESTION, null, null);
            default -> throw new DAOException("Invalid question type!!!");
        }

        return QuestionMapper.dtoToQuestion(dto);
    }

    @Override
    public Question getQuestionById(int id) {
        if (this.containsKey(id)) {
            return this.getFromCache(id);
        }

        int testId = extractTestId(id);
        int index = extractIndex(id);

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject testObject = findTestObject(jsonArray, testId);
            if (testObject == null || !testObject.has(KEY_QUESTIONS)) {
                return null;
            }

            JSONArray questionsArray = testObject.getJSONArray(KEY_QUESTIONS);
            if (index < 0 || index >= questionsArray.length()) {
                return null;
            }

            Question question = buildQuestion(questionsArray.getJSONObject(index), id);
            this.addToCache(id, question);
            return question;
        } catch (IOException e) {
            throw new DAOException("Error occurred while fetching question by id from file system. " + e.getMessage());
        }
    }

    @Override
    public List<Question> getQuestionsByTestId(int testID) {
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject testObject = findTestObject(jsonArray, testID);
            if (testObject == null) {
                return new ArrayList<>();
            }
            return extractQuestionsFromTestObject(testObject, testID);
        } catch (IOException e) {
            throw new DAOException("Error occurred while fetching questions by test id from file system. " + e.getMessage());
        }
    }

    private List<Question> extractQuestionsFromTestObject(JSONObject testObject, int testID) {
        List<Question> questions = new ArrayList<>();
        if (!testObject.has(KEY_QUESTIONS)) {
            return questions;
        }

        JSONArray questionsArray = testObject.getJSONArray(KEY_QUESTIONS);
        for (int index = 0; index < questionsArray.length(); index++) {
            int questionId = computeQuestionId(testID, index);

            if (this.containsKey(questionId)) {
                questions.add(this.getFromCache(questionId));
                continue;
            }

            Question question = buildQuestion(questionsArray.getJSONObject(index), questionId);
            this.addToCache(questionId, question);
            questions.add(question);
        }
        return questions;
    }

    @Override
    public Map<Integer, List<Question>> getQuestionsByTestIds(List<Integer> testIDs) {
        Map<Integer, List<Question>> questionsByTest = new HashMap<>();
        if (testIDs == null || testIDs.isEmpty()) {
            return questionsByTest;
        }

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject testObject = jsonArray.getJSONObject(i);
                if (!testObject.has(KEY_TEST_ID) || !testIDs.contains(testObject.getInt(KEY_TEST_ID))) continue;

                int testID = testObject.getInt(KEY_TEST_ID);

                questionsByTest.put(testID, extractQuestionsFromTestObject(testObject, testID));
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while fetching questions for multiple tests from file system. " + e.getMessage());
        }

        return questionsByTest;
    }

    @Override
    public void saveTestQuestion(int testID, List<Question> questions) {
        List<CloseIndexes> completeSaving = new ArrayList<>();

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject testObject = findTestObject(jsonArray, testID);
            if (testObject == null) {
                throw new DAOException("Cannot save questions: test " + testID + " not found in file system");
            }

            JSONArray questionsArray = new JSONArray();
            for (int index = 0; index < questions.size(); index++) {
                QuestionDTO dto = QuestionMapper.questionToDTO(questions.get(index));

                JSONObject qObj = new JSONObject();
                qObj.put(KEY_HEADER, dto.header());
                qObj.put(KEY_MAX_SCORE, dto.score());
                questionsArray.put(qObj);

                if (dto.type() == QuestionType.CLOSEQUESTION) {
                    completeSaving.add(new CloseIndexes(dto, index));
                }
            }

            testObject.put(KEY_QUESTIONS, questionsArray);
            JSONHelper.writeJsonFile(FILE_PATH, jsonArray);

            for (int index = 0; index < questions.size(); index++) {
                this.addToCache(computeQuestionId(testID, index), questions.get(index));
            }

            finalizeQuestionSaving(testID, completeSaving);
        } catch (IOException e) {
            throw new DAOException("Error occurred while saving questions to file system. " + e.getMessage());
        }
    }

    private void finalizeQuestionSaving(int testID, List<CloseIndexes> questions) {
        List<ChoiceDTO> choices = new ArrayList<>();
        for (CloseIndexes closeIndexes : questions) {
            int questionId = computeQuestionId(testID, closeIndexes.index());
            choices.add(new ChoiceDTO(closeIndexes.q().options(), closeIndexes.q().solution(), questionId));
        }

        try {
            DAOFactory.getInstance().getChoiceDAO().saveChoices(choices);
        } catch (DAOException e) {
            throw new DAOException("Error occurred while saving question data to file system.");
        }
    }

    private JSONObject findTestObject(JSONArray jsonArray, int testID) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject candidate = jsonArray.getJSONObject(i);
            if (candidate.has(KEY_TEST_ID) && candidate.getInt(KEY_TEST_ID) == testID) {
                return candidate;
            }
        }
        return null;
    }

    @Override
    public Integer getQuestionId(Question question, int testID) {
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject testObject = findTestObject(jsonArray, testID);
            if (testObject == null || !testObject.has(KEY_QUESTIONS)) {
                return null;
            }

            JSONArray questionsArray = testObject.getJSONArray(KEY_QUESTIONS);
            for (int index = 0; index < questionsArray.length(); index++) {
                JSONObject questionObject = questionsArray.getJSONObject(index);
                if (questionObject.getString(KEY_HEADER).equals(question.getHeader())) {
                    return computeQuestionId(testID, index);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while getting question id from file system. " + e.getMessage());
        }
        return null;
    }
}
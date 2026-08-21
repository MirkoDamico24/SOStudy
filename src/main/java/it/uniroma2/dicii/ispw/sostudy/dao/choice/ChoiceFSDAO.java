package it.uniroma2.dicii.ispw.sostudy.dao.choice;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.question.QuestionFSDAO;
import it.uniroma2.dicii.ispw.sostudy.eng.functional.ChoiceDTO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Choice;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChoiceFSDAO extends ChoiceDAO {

    private static final String FILE_PATH = "data/Test.JSON";
    private static final String KEY_TEST_ID = "id";
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_OPTIONS = "options";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_IS_SOLUTION = "isSolution";

    private int computeChoiceId(int questionId, int choiceIndex) {
        return questionId * QuestionFSDAO.ID_MULTIPLIER + choiceIndex;
    }

    private int extractQuestionId(int choiceId) {
        return choiceId / QuestionFSDAO.ID_MULTIPLIER;
    }

    private int extractChoiceIndex(int choiceId) {
        return choiceId % QuestionFSDAO.ID_MULTIPLIER;
    }

    @Override
    public Choice getChoiceById(int choiceID) {
        int questionId = extractQuestionId(choiceID);
        int choiceIndex = extractChoiceIndex(choiceID);
        int testId = questionId / QuestionFSDAO.ID_MULTIPLIER;
        int questionIndex = questionId % QuestionFSDAO.ID_MULTIPLIER;

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject questionObject = findQuestionObject(jsonArray, testId, questionIndex);
            if (questionObject == null || !questionObject.has(KEY_OPTIONS)) {
                return null;
            }

            JSONArray optionsArray = questionObject.getJSONArray(KEY_OPTIONS);
            if (choiceIndex < 0 || choiceIndex >= optionsArray.length()) {
                return null;
            }

            return new Choice(optionsArray.getJSONObject(choiceIndex).getString(KEY_CONTENT));
        } catch (IOException e) {
            throw new DAOException("Error occurred while fetching choice from file system. " + e.getMessage());
        }
    }

    @Override
    public ChoiceDTO getChoicesByQuestionId(int questionID) {
        int testId = questionID / QuestionFSDAO.ID_MULTIPLIER;
        int questionIndex = questionID % QuestionFSDAO.ID_MULTIPLIER;

        List<Choice> choicesList = new ArrayList<>();
        Choice solution = null;

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject questionObject = findQuestionObject(jsonArray, testId, questionIndex);

            if (questionObject != null && questionObject.has(KEY_OPTIONS)) {
                JSONArray optionsArray = questionObject.getJSONArray(KEY_OPTIONS);
                for (int j = 0; j < optionsArray.length(); j++) {
                    JSONObject optionObject = optionsArray.getJSONObject(j);
                    Choice choice = new Choice(optionObject.getString(KEY_CONTENT));
                    choicesList.add(choice);
                    if (optionObject.has(KEY_IS_SOLUTION) && optionObject.getBoolean(KEY_IS_SOLUTION)) {
                        solution = choice;
                    }
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while fetching choices from file system. " + e.getMessage());
        }

        return new ChoiceDTO(choicesList, solution, questionID);
    }

    @Override
    public void saveChoices(List<ChoiceDTO> choices) {
        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);

            for (ChoiceDTO dto : choices) {
                int questionID = dto.questionID();
                int testId = questionID / QuestionFSDAO.ID_MULTIPLIER;
                int questionIndex = questionID % QuestionFSDAO.ID_MULTIPLIER;

                JSONObject questionObject = findQuestionObject(jsonArray, testId, questionIndex);
                if (questionObject == null) {
                    continue;
                }

                JSONArray optionsArray = new JSONArray();
                for (Choice c : dto.options()) {
                    JSONObject optObj = new JSONObject();
                    optObj.put(KEY_CONTENT, c.getContent());
                    if (c == dto.solution()) {
                        optObj.put(KEY_IS_SOLUTION, true);
                    }
                    optionsArray.put(optObj);
                }
                questionObject.put(KEY_OPTIONS, optionsArray);
            }

            JSONHelper.writeJsonFile(FILE_PATH, jsonArray);
        } catch (IOException e) {
            throw new DAOException("Error occurred while saving choices to file system. " + e.getMessage());
        }
    }

    @Override
    public int getChoiceId(Choice choice, int questionId) {
        int testId = questionId / QuestionFSDAO.ID_MULTIPLIER;
        int questionIndex = questionId % QuestionFSDAO.ID_MULTIPLIER;

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);
            JSONObject questionObject = findQuestionObject(jsonArray, testId, questionIndex);
            if (questionObject == null || !questionObject.has(KEY_OPTIONS)) {
                return 0;
            }

            JSONArray optionsArray = questionObject.getJSONArray(KEY_OPTIONS);
            for (int j = 0; j < optionsArray.length(); j++) {
                if (optionsArray.getJSONObject(j).getString(KEY_CONTENT).equals(choice.getContent())) {
                    return computeChoiceId(questionId, j);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error occurred while getting choice id from file system. " + e.getMessage());
        }

        return 0;
    }

    private JSONObject findQuestionObject(JSONArray jsonArray, int testId, int questionIndex) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject testObject = jsonArray.getJSONObject(i);
            if (testObject.has(KEY_TEST_ID) && testObject.getInt(KEY_TEST_ID) == testId
                    && testObject.has(KEY_QUESTIONS)) {
                JSONArray questionsArray = testObject.getJSONArray(KEY_QUESTIONS);
                if (questionIndex >= 0 && questionIndex < questionsArray.length()) {
                    return questionsArray.getJSONObject(questionIndex);
                }
            }
        }
        return null;
    }
}
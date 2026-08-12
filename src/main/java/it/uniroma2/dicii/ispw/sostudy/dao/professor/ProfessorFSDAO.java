package it.uniroma2.dicii.ispw.sostudy.dao.professor;

import it.uniroma2.dicii.ispw.sostudy.application.JSONHelper;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.virtualclass.VirtualClassDAO;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProfessorFSDAO extends ProfessorDAO {

    private static final String FILE_PATH = "data/Professor.JSON";

    public List<VirtualClass> getProfessorClasses(JSONArray jsonArray) throws DAOException {
        List<VirtualClass> classes = new ArrayList<>();
        VirtualClassDAO virtualClassDAO = DAOFactory.getInstance().getVirtualClassDAO();

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject classObj = jsonArray.getJSONObject(j);
            String className = classObj.getString("className");
            VirtualClass virtualClass = virtualClassDAO.getVirtualClassByName(className);
            if (virtualClass != null) {
                classes.add(virtualClass);
            }
        }
        return classes;
    }

    @Override
    public Professor getProfessorByEmail(String email) throws DAOException {
        if (this.containsKey(email)) {
            return this.getFromCache(email);
        }

        try {
            JSONArray jsonArray = JSONHelper.readJsonFile(FILE_PATH);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("email").equals(email)) {
                    return buildProfessorFromJson(jsonObject, email);
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error reading professor data");
        }
        return null;
    }

    private Professor buildProfessorFromJson(JSONObject jsonObject, String email) throws DAOException {
        String name = jsonObject.getString("name");
        String surname = jsonObject.getString("surname");

        List<VirtualClass> classes = jsonObject.has("classes")
                ? this.getProfessorClasses(jsonObject.getJSONArray("classes")) : null;

        Professor professor = new Professor(name, surname, email, classes);
        this.addToCache(email, professor);
        return professor;
    }

}
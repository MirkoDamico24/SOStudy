package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.VirtualClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VirtualClassDBDAO extends VirtualClassDAO {

    @Override
    public VirtualClass getVirtualClassById(int id) throws DAOException {
        if (this.containsKey(id)) {
            return this.getFromCache(id);
        }

        String sqlQuery = "SELECT code, name, professor FROM Class WHERE code = ?";

        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String professorEmail = rs.getString("professor");

                    Professor professor = DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail(professorEmail);
                    //TODO:VirtualClass virtualClass = new VirtualClass(name, id, professor);
                    VirtualClass virtualClass = null;
                    this.addToCache(id, virtualClass);
                    return virtualClass;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Database error occurred while retrieving virtual class by id");
        }

        return null;
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException {
        List<VirtualClass> virtualClasses = new ArrayList<>();
        String sqlQuery = "SELECT code, name FROM Class WHERE professor = ?";

        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)) {
            ps.setString(1, profEmail);

            try (ResultSet rs = ps.executeQuery()) {
                Professor professor = DAOFactory.getInstance().getProfessorDAO().getProfessorByEmail(profEmail);

                while (rs.next()) {
                    int classId = rs.getInt("code");

                    if (this.containsKey(classId)) {
                        virtualClasses.add(this.getFromCache(classId));
                    } else {
                        String name = rs.getString("name");
                        //TODO:VirtualClass virtualClass = new VirtualClass(name, classId, professor);
                        VirtualClass virtualClass = null;
                        this.addToCache(classId, virtualClass);
                        virtualClasses.add(virtualClass);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Database error occurred while retrieving classes by professor email");
        }

       return virtualClasses;
    }

    @Override
    public List<Test> getClassTests(int classId){
        return null;
    }
}
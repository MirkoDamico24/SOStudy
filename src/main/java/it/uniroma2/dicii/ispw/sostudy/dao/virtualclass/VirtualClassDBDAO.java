package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnectionFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Professor;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
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
                    VirtualClass virtualClass = new VirtualClass(name, id, professor);
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
                        VirtualClass virtualClass = new VirtualClass(name, classId, professor);
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
    public void getClassTests(int classId) {
        if (!this.containsKey(classId)) {
            throw new DAOException("Virtual class not present in cache");
        }

        VirtualClass virtualClass = this.getFromCache(classId);
        List<Test> tests = new ArrayList<>();

        String sqlQuery = "SELECT code FROM Test WHERE classId = ?";
        try (PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Test test = DAOFactory.getInstance().getTestDAO().getTestById(rs.getInt("code"));
                tests.add(test);
            }
        } catch (SQLException e) {
            throw new DAOException("Database error occurred while retrieving tests by id");
        }
        virtualClass.setAssignedTests(tests);
    }

    @Override
    public void getClassStudents(int classId){
        if (!this.containsKey(classId)) {
            throw new DAOException("Virtual class not present in cache");
        }

        VirtualClass virtualClass = this.getFromCache(classId);
        List<Student> students = new ArrayList<>();

        String sqlQuery = "SELECT email FROM ClassStudent join Student on student = email WHERE class = ?";
        try(PreparedStatement ps = DBConnectionFactory.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Student student = DAOFactory.getInstance().getStudentDAO().getStudentByEmail(rs.getString("email"));
                students.add(student);
            }
        }
        catch (SQLException e){
            throw new DAOException("Database error occurred while retrieving class' students");
        }
        virtualClass.setStudent(students);
    }
}
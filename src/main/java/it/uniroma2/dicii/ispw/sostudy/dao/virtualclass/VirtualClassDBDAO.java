package it.uniroma2.dicii.ispw.sostudy.dao.virtualclass;

import it.uniroma2.dicii.ispw.sostudy.application.DBConnection;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.dao.professor.ProfessorDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.student.StudentDAO;
import it.uniroma2.dicii.ispw.sostudy.dao.test.TestDAO;
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
    private DAOFactory factory = DAOFactory.getInstance();
    private ProfessorDAO profDAO = factory.getProfessorDAO();
    private StudentDAO studentDAO = factory.getStudentDAO();
    private TestDAO testDAO = factory.getTestDAO();

    @Override
    public VirtualClass getVirtualClassById(int id) throws DAOException {
        if (this.containsKey(id)) {
            return this.getFromCache(id);
        }

        String sqlQuery = "SELECT name, professor FROM Class WHERE code = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQuery)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String professorEmail = rs.getString("professor");

                    Professor professor = profDAO.getProfessorByEmail(professorEmail);
                    List<Student> students = getClassStudents(id);

                    VirtualClass virtualClass = new VirtualClass(name, professor, students);
                    List<Test> tests = loadClassTests(id, virtualClass);
                    virtualClass.setAssignedTests(tests);

                    this.modelWiring(virtualClass, professor, students);

                    this.addToCache(id, virtualClass);
                    return virtualClass;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Database error occurred while retrieving virtual class by id");
        }

        return null;
    }

    private List<Student> getClassStudents(int classID) throws DAOException {
        List<Student> students = new ArrayList<>();
        String sqlQuery = "SELECT email FROM ClassStudents join Student on student = email WHERE class = ?";

        try(PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQuery)){
            ps.setInt(1, classID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Student student = studentDAO.getStudentByEmail(rs.getString("email"));
                students.add(student);
            }
        }
        catch (SQLException | DAOException e){
            throw new DAOException("Database error occurred while retrieving class' students. " +  e.getMessage());
        }
        return students;
    }

    private List<Test> loadClassTests(int classId, VirtualClass virtualClass) throws DAOException {
        return testDAO.getTestByClassId(classId, virtualClass);
    }

    @Override
    public List<VirtualClass> getClassesByProfessor(String profEmail) throws DAOException {
        List<VirtualClass> virtualClasses = new ArrayList<>();
        String sqlQuery = "SELECT code FROM Class WHERE professor = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQuery)) {
            ps.setString(1, profEmail);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                virtualClasses.add(this.getVirtualClassById(rs.getInt("code")));
            }
        } catch (SQLException e) {
            throw new DAOException("Database error occurred while retrieving classes by professor email.");
        }

       return virtualClasses;
    }

    @Override
    public List<VirtualClass> getClassesByStudent(String studentEmail) throws DAOException {       
        List<VirtualClass> virtualClasses = new ArrayList<>();
        String sqlQuery = "SELECT class FROM ClassStudents WHERE student = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQuery)) {
            ps.setString(1, studentEmail);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                virtualClasses.add(this.getVirtualClassById(rs.getInt("class")));
            }
        }
        catch (SQLException e) {
            throw new DAOException("Database error occurred while retrieving classes by student email.");
        }

        return virtualClasses;
    }
}

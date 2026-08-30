package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.bean.QuestionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.TestBean;
import it.uniroma2.dicii.ispw.sostudy.controller.CreateTestController;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreationTest {
    private DAOFactory factory = DAOFactory.getInstance();
    private VirtualClass vcls;
    private int sessionId;

    private TestBean createTest(LocalDate dueDate, LocalTime dueTime, Duration duration, String className){
        List<QuestionBean> questions = new ArrayList<>();
        QuestionBean q1 = new QuestionBean("Intestazione domanda aperta", 10);
        questions.add(q1);
        List<String> choices = List.of("Opzione 1", "Opzione 2", "Opzione 3");
        QuestionBean q2 = new QuestionBean("Intestazione domanda a risposta multipla", 5, choices, choices.indexOf("Opzione 1"));
        questions.add(q2);

        TestBean toCreate = new TestBean("Test di prova",
                dueDate,
                dueTime,
                duration,
                className);

        toCreate.setQuestions(questions);
        return toCreate;
    }

    @BeforeEach
    public void setup() {
        Professor prof = new Professor("Mario", "Rossi", "mario.rossi@gmail.com");
        factory.getProfessorDAO().addToCache("mario.rossi@gmail.com", prof);
        sessionId = SessionManager.getInstance().createSession(prof).getSessionID();

        Student s1 = new Student("Giuseppe", "Bianchi", "giuseppe.bianchi@gmail.com");
        Student s2 = new Student("Alessio", "Neri", "a.neri@gmail.com");
        factory.getStudentDAO().addToCache("giuseppe.bianchi@gmail.com", s1);
        factory.getStudentDAO().addToCache("a.neri@gmail.com", s2);
        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);

        vcls = new VirtualClass("Ispw", prof, students);
        factory.getVirtualClassDAO().addToCache(1, vcls);
    }

    @Test
    public void testCreation() {
        TestBean toCreate = createTest(LocalDate.now(ZoneId.systemDefault()).plusDays(1),
                LocalTime.of(17, 00),
                Duration.ofMinutes(30),
                vcls.getName());

        //test creation request to controller
        CreateTestController  controller = new CreateTestController();
        controller.createTest(sessionId, toCreate);

        List<it.uniroma2.dicii.ispw.sostudy.model.Test> classTests = vcls.getAvailableTests();
        it.uniroma2.dicii.ispw.sostudy.model.Test newTest = classTests.getFirst();

        assertEquals(toCreate.getName(), newTest.getName());
        assertEquals(toCreate.getDueDate(), newTest.getDueDate());
        assertEquals(toCreate.getDueTime(), newTest.getDueTime());
        assertEquals(toCreate.getDuration(), newTest.getDuration());

        List<Question> testQuestions = newTest.getQuestions();
        for(int i = 0; i < testQuestions.size(); i++) {
            Question q = testQuestions.get(i);
            QuestionBean equivalent = toCreate.getQuestions().get(i);
            assertEquals(equivalent.getHeader(), q.getHeader());
            assertEquals(equivalent.getMaxScore(), q.getMaxScore());
        }
    }

    @Test
    public void testStudentNotification(){
        //creating test questions
        TestBean toCreate = createTest(LocalDate.now(ZoneId.systemDefault()).plusDays(1),
                LocalTime.of(17, 00),
                Duration.ofMinutes(30),
                vcls.getName());

        //test creation request to controller
        CreateTestController  controller = new CreateTestController();
        controller.createTest(0, toCreate);

        String message = "Nuovo test assegnato: " + toCreate.getVirtualClass() + ", " + toCreate.getName() + ", " + toCreate.getDueDate();
        List<Student> classStudents = vcls.getStudents();
        for(Student student : classStudents) {
            assertEquals(message, student.getMessages().getFirst().getMessage());
        }
    }

    @Test
    public void testAssignTestNonExistingClass(){
        TestBean toCreate = createTest(LocalDate.now(ZoneId.systemDefault()).plusDays(1),
                LocalTime.of(17, 00),
                Duration.ofMinutes(30),
                "Sistemi operativi");
        CreateTestController  controller = new CreateTestController();
        ControllerException e = assertThrows(ControllerException.class, () -> controller.createTest(sessionId, toCreate));
        assertEquals("Class not found", e.getMessage());
    }

    @Test
    public void testDateTimeValidation(){
        TestBean toTest = createTest(LocalDate.now(ZoneId.systemDefault()).minusDays(1),
                LocalTime.of(17, 00),
                Duration.ofMinutes(30),
                vcls.getName());

        CreateTestController  controller = new CreateTestController();
        ControllerException e = assertThrows(ControllerException.class, () -> controller.validateDueDate(toTest));
        assertEquals("La data e l'ora di consegna devono essere successivi a quelli attuali", e.getMessage());
    }
}

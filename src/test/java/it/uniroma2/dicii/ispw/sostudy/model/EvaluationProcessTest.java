package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.bean.*;
import it.uniroma2.dicii.ispw.sostudy.controller.KnowledgeEvaluationController;
import it.uniroma2.dicii.ispw.sostudy.dao.factory.DAOFactory;
import it.uniroma2.dicii.ispw.sostudy.exception.ControllerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluationProcessTest {
    private DAOFactory factory =  DAOFactory.getInstance();
    private it.uniroma2.dicii.ispw.sostudy.model.Test demoTest;
    private List<Question> questions;
    private VirtualClass vcls;
    private SessionBean sessionBean;
    private Session session;
    private Student s1;

    private TestAttempt createAttempt(it.uniroma2.dicii.ispw.sostudy.model.Test test, Student student, LocalDate handInDate, LocalTime handInTime){
        Answer answerFirstQuestion = new Answer(1, "Risposta domanda 1", demoTest.getQuestions().getFirst());
        Answer answerLast = new Answer(5, new Choice("Prima opzione"),  demoTest.getQuestions().getLast());
        List<Answer<?>> answers = List.of(answerFirstQuestion, answerLast);

        TestAttempt attempt = new TestAttempt(test,
                answers,
                student,
                answerFirstQuestion.getScore() + answerLast.getScore(),
                TestGradingStatus.FULLYGRADED,
                handInTime,
                handInDate);

        test.addTestAttempt(attempt);

        return attempt;
    }

    private void createTest(LocalDate dueDate, LocalTime dueTime, List<Question> testQuestions){
        demoTest = new it.uniroma2.dicii.ispw.sostudy.model.Test("Test di prova",
                dueDate,
                dueTime,
                Duration.ofMinutes(30),
                testQuestions,
                vcls);
        factory.getTestDAO().saveTest(demoTest);
        vcls.addTest(demoTest);
    }

    @BeforeEach
    public void setup() {
        Question q1 = new OpenQuestion("Domanda 1", 10);
        Choice option1 = new Choice("Prima opzione");
        Choice option2 = new Choice("Seconda opzione");
        Choice option3 = new Choice("Terza opzione");
        List<Choice> choices = List.of(option1, option2, option3);
        Question q2 = new CloseQuestion("Domanda 2", 5, choices, choices.getFirst());
        questions = List.of(q1, q2);

        Professor prof = new Professor("Mario", "Rossi", "mario.rossi@gmail.com");
        factory.getProfessorDAO().addToCache("mario.rossi@gmail.com", prof);

        s1 = new Student("Giuseppe", "Bianchi", "giuseppe.bianchi@gmail.com");
        factory.getStudentDAO().addToCache("giuseppe.bianchi@gmail.com", s1);

        session = SessionManager.getInstance().createSession(s1);
        sessionBean = new SessionBean(new StudentBean("Giuseppe", "Bianchi", "giuseppe.bianchi@gmail.com"), session.getSessionID());

        vcls = new VirtualClass("Ispw", prof, s1);
        factory.getVirtualClassDAO().addToCache(1, vcls);
    }

    @Test
    public void testRequireAlreadyDoneTest(){
        createTest(LocalDate.now(ZoneId.systemDefault()).plusDays(1), LocalTime.of(15, 30), questions);

        TestBean requiredTest = new TestBean("Test di prova",
                LocalDate.now(ZoneId.systemDefault()).plusDays(1),
                LocalTime.of(15, 30),
                Duration.ofMinutes(30),
                vcls.getName());

        createAttempt(demoTest, s1, LocalDate.now(ZoneId.systemDefault()), LocalTime.of(15, 30));

        KnowledgeEvaluationController controller = new KnowledgeEvaluationController();
        List<QuestionBean> questions = controller.loadRequiredTest(sessionBean, requiredTest);
        assertTrue(questions.isEmpty(), "Il metodo deve restituire una lista vuota se il test è già stato svolto.");
    }

    @Test
    public void testRequireExpiredTest(){
        createTest(LocalDate.now(ZoneId.systemDefault()).minusDays(1), LocalTime.of(15, 30), questions);

        TestBean requiredTest = new TestBean("Test di prova",
                LocalDate.now(ZoneId.systemDefault()).minusDays(1),
                LocalTime.of(15, 30),
                Duration.ofMinutes(30),
                vcls.getName());

        KnowledgeEvaluationController controller = new KnowledgeEvaluationController();
        ControllerException e = assertThrows(ControllerException.class, () -> controller.loadRequiredTest(sessionBean, requiredTest));
        assertEquals("Termini di consegna del test scaduti.", e.getMessage());
    }

    @Test
    public void testAutomaticEvaluation(){
        createTest(LocalDate.now(ZoneId.systemDefault()).plusDays(1), LocalTime.of(15, 30), questions);
        TestAttempt attempt = createAttempt(demoTest, s1, LocalDate.now(ZoneId.systemDefault()), LocalTime.of(15, 30));
        session.setCurrentAttempt(attempt);

        KnowledgeEvaluationController controller = new KnowledgeEvaluationController();
        controller.submitAttempt(sessionBean);

        assertEquals(demoTest.getQuestions().getLast().getMaxScore(), attempt.getAnswers().getLast().getScore(),
                "Lo score assegnato dovrebbe essere pari a quello massimo previsto dalla domanda.");
        assertEquals(TestGradingStatus.INCOMPLETE, attempt.getTestGradingStatus());
    }
}

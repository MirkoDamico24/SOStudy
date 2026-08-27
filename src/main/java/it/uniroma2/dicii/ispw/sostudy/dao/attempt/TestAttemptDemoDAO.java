package it.uniroma2.dicii.ispw.sostudy.dao.attempt;

import it.uniroma2.dicii.ispw.sostudy.exception.DAOException;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.Test;
import it.uniroma2.dicii.ispw.sostudy.model.TestAttempt;

import java.util.ArrayList;
import java.util.List;

public class TestAttemptDemoDAO extends TestAttemptDAO {
    @Override
    public void saveTestAttempt(TestAttempt testAttempt) throws DAOException{
       //attempts not cached
    }

    @Override
    public List<TestAttempt> getTestAttempt(int testId) throws DAOException{
        //attempts accessed only through Test objects
        return new ArrayList<>();
    }

    public void updateTestAttempt(TestAttempt testAttempt) throws DAOException{
        //no need to update
    }

    @Override
    public boolean checkAlreadyDone(Test test, Student student) throws DAOException{
        List<TestAttempt> attempts = test.getTests();
        if(attempts == null || attempts.isEmpty()) return false;

        for(TestAttempt attempt : attempts){
            if(attempt.getStudent().getEmail().equals(student.getEmail())) return true;
        }
        return false;
    }
}

package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.exception.ModelException;

import java.util.HashMap;

public class SessionManager {
    private final HashMap<Integer, Session> sessions;

    private int ids;

    public SessionManager() {
        sessions = new HashMap<>();
    }

    private static class Container{
        private static final SessionManager instance = new SessionManager();
    }

    public static SessionManager getInstance(){
        return Container.instance;
    }

    public Session createSession(Student student){
        Session s = new Session(this.ids, student);
        this.sessions.put(this.ids, s);
        this.ids++;
        return s;
    }

    public Session createSession(Professor currentProfessor){
        Session s = new Session(this.ids, currentProfessor);
        this.sessions.put(this.ids, s);
        this.ids++;
        return s;
    }

    public Session getSession(int sessionID){
        return this.sessions.get(sessionID);
    }

    public void deleteSession(int sessionID){
        if(this.sessions.containsKey(sessionID))  this.sessions.remove(sessionID);
        else{
            throw new ModelException("The provided session id does not exist");
        }
    }
}

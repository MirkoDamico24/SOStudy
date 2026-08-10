package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;

public class ContextContainer {
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
    }

    public SessionBean getSession() {
        return session;
    }
}

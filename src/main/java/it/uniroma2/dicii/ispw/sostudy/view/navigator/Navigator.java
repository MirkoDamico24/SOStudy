package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.bean.ProfessorBean;
import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.bean.StudentBean;
import it.uniroma2.dicii.ispw.sostudy.bean.UserBean;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;

public abstract class Navigator {
    private Views currentView;
    private Views previousView;
    private ContextContainer contex;

    protected Navigator(){
        this.contex = new ContextContainer();
    }

    public void setCurrentView(Views currentView) {
        this.currentView = currentView;
    }

    public void setPreviousView(Views previousView) {
        this.previousView = previousView;
    }

    public void setContext(ContextContainer contex) {
        this.contex = contex;
    }

    public ContextContainer getContext() {
        return this.contex;
    }

    public void setSession(SessionBean session) {
        if(session == null) return;
        this.contex.setSession(session);
    }

    public SessionBean getSession() {
        return this.contex.getSession();
    }

    public Views getPreviousView() {
        return previousView;
    }

    public void nextView(){
        if(currentView == null) return;
        switch(currentView){
            case LOGIN -> createLoginView();
            case HOME -> createHomeView();
            case CREATETEST -> creatTestView();
            case CLASSVIEW -> createClassesView();
            case OPENQUESTIONVIEW -> createOpenQuestionView();
            case CLOSEQUESTIONVIEW -> createCloseQuestionView();
            case RECAP -> createRecapView();
            case INSIDECLASSVIEW -> createInsideClassView();
            case OPENANSWERVIEW ->  createOpenAnswerView();
            case CLOSEANSWERVIEW ->  createCloseAnswerView();
        }
    }

    public void goToLoginView(){
        setCurrentView(Views.LOGIN);
        nextView();
    }

    public void goToHomeView(){
        setCurrentView(Views.HOME);
        nextView();
    }

    public void goToClassesView(){
        setCurrentView(Views.CLASSVIEW);
        nextView();
    }

    public void goToInsideClassView(){
        setCurrentView(Views.INSIDECLASSVIEW);
        nextView();
    }

    public void goToOpenQuestionView(){
        setCurrentView(Views.OPENQUESTIONVIEW);
        nextView();
    }

    public void goToCloseQuestionView(){
        setCurrentView(Views.CLOSEQUESTIONVIEW);
        nextView();
    }
    public void goToOpenAnswerView(){
        setCurrentView(Views.OPENANSWERVIEW);
        nextView();
    }

    public void goToCloseAnswerView(){
        setCurrentView(Views.CLOSEANSWERVIEW);
        nextView();
    }

    public void goToCreateTestView(){
        setCurrentView(Views.CREATETEST);
        nextView();
    }

    public void goToRecapView(){
        setCurrentView(Views.RECAP);
        nextView();
    }

    public UserBean getCorrectUserBean(){
        UserBean ub;
        SessionBean currentSession = this.getContext().getSession();
        UserRole currentRole = currentSession.getCurrentRole();
        if(currentRole == UserRole.STUDENT) {
            StudentBean sb = currentSession.getStudent();
            ub = new UserBean(sb.getEmail(), null);
        }
        else{
            ProfessorBean pr = currentSession.getProfessor();
            ub = new UserBean(pr.getEmail(), null);
        }
        return ub;
    }

    //Methods for student and professor
    public abstract void startup();
    public abstract void createLoginView();
    public abstract void createHomeView();
    public abstract void creatTestView();
    public abstract void createClassesView();
    public abstract void createInsideClassView();

    //professor only
    public abstract void createOpenQuestionView();
    public abstract void createCloseQuestionView();
    public abstract void createRecapView();

    //student only
    public abstract void createOpenAnswerView();
    public abstract void createCloseAnswerView();
}

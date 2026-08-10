package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.bean.SessionBean;
import it.uniroma2.dicii.ispw.sostudy.controller.UserRole;

public abstract class Navigator {
    private Views currentView;
    private ContextContainer contex;

    public Navigator(){
        this.contex = new ContextContainer();
    }

    public void setCurrentView(Views currentView) {
        this.currentView = currentView;
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

    public void nextView(){
        if(currentView == null) return;
        switch(currentView){
            case LOGIN -> createLoginView();
            case HOME -> createHomeView();
            /*  YET TO IMPLEMENT
                case CLASSVIEW -> createClassesView();
                case OPENQUESTIONVIEW -> createOpenQuestionView();
                case CLOSEQUESTIONVIEW -> createCloseQuestionView();
                case OPENANSWERVIEW ->  createOpenAnswerView();
                case CLOSEANSWERVIEW ->  createCloseAnswerView();
                case CREATETEST -> creatTestView();
            */
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


    //Methods for student and professor
    public abstract void startup();
    public abstract void createLoginView();
    public abstract void createHomeView();
    /*  YET TO IMPLEMENT
        public abstract void createClassesView();
        public abstract void creatTestView();

        //professor only
        public abstract void createOpenQuestionView();
        public abstract void createCloseQuestionView();

        //student only
        public abstract void createOpenAnswerView();
        public abstract void createCloseAnswerView();
   */
}

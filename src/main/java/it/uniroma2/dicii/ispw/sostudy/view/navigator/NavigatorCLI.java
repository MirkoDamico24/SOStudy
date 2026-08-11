package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.view.CreaTestDetailControllerCLI;
import it.uniroma2.dicii.ispw.sostudy.view.HomeControllerCLI;
import it.uniroma2.dicii.ispw.sostudy.view.LoginControllerCLI;
import it.uniroma2.dicii.ispw.sostudy.view.VirtualClassesViewControllerCLI;


public class NavigatorCLI extends Navigator{
    private LoginControllerCLI login;
    private HomeControllerCLI home;
    private CreaTestDetailControllerCLI creaTest;
    private VirtualClassesViewControllerCLI virtualClasses;

    @Override
    public void startup(){
        goToLoginView();
    }

    @Override
    public void createLoginView(){
        if(this.login == null){
            login = new LoginControllerCLI();
            this.login.setNavigator(this);
        }
        this.login.handleLogin();
    }

    @Override
    public void createHomeView(){
        if(this.home == null){
            this.home = new HomeControllerCLI();
            this.home.setNavigator(this);
        }
        this.home.start();
    }

    @Override
    public void creatTestView(){
        if(this.creaTest == null){
            this.creaTest = new CreaTestDetailControllerCLI();
            this.creaTest.setNavigator(this);
        }
        this.creaTest.start();
    }

    @Override
    public void createClassesView(){
        if(this.virtualClasses == null){
            this.virtualClasses = new VirtualClassesViewControllerCLI();
            this.virtualClasses.setNavigator(this);
        }
        this.virtualClasses.start();
    }

    @Override
    public void createOpenQuestionView(){

    }

    @Override
    public void createCloseQuestionView(){

    }

    @Override
    public void createRecapView(){

    }
}

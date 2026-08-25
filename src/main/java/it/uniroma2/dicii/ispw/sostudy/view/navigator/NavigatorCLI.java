package it.uniroma2.dicii.ispw.sostudy.view.navigator;

import it.uniroma2.dicii.ispw.sostudy.model.QuestionType;
import it.uniroma2.dicii.ispw.sostudy.view.cli.*;
import java.util.Scanner;


public class NavigatorCLI extends Navigator{
    private LoginControllerCLI login;
    private HomeControllerCLI home;
    private CreaTestDetailControllerCLI creaTest;
    private VirtualClassesViewControllerCLI virtualClasses;
    private CreaDomandaApertaControllerCLI openQuestion;
    private CreaDomandaMultiplaControllerCLI closeQuestion;
    private RiepilogoTestControllerCLI recapView;

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
    public void createInsideClassView(){

    }

    @Override
    public void createOpenQuestionView(){
        if(this.openQuestion == null){
            this.openQuestion = new CreaDomandaApertaControllerCLI();
            this.openQuestion.setNavigator(this);
        }
        this.openQuestion.start();
    }

    @Override
    public void createCloseQuestionView(){
        if(this.closeQuestion == null){
            this.closeQuestion = new CreaDomandaMultiplaControllerCLI();
            this.closeQuestion.setNavigator(this);
        }
        this.closeQuestion.start();
    }

    @Override
    public void createRecapView(){
        if(this.recapView == null){
            this.recapView = new RiepilogoTestControllerCLI();
            this.recapView.setNavigator(this);
        }
        this.recapView.start();
    }

    @Override
    public void createCloseAnswerView(){

    }

    @Override
    public void createOpenAnswerView(){

    }

    @Override
    public void createEvaluateOpenAnswerView(){

    }

    @Override
    public void createTestAttemptView(){

    }

    public static QuestionType selectQuestionType() {
        System.out.println("\nSeleziona la tipologia di domanda da aggiungere:");
        System.out.println("[1] Domanda a risposta aperta");
        System.out.println("[2] Domanda a risposta multipla");
        System.out.print("\nScegli un'opzione: ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                return QuestionType.OPENQUESTION;
            }
            case "2" -> {
                return QuestionType.CLOSEQUESTION;
            }
            default -> {
                System.out.println("\n--> Operazione non consentita!");
                return selectQuestionType();
            }
        }
    }
}

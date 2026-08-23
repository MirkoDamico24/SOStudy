package it.uniroma2.dicii.ispw.sostudy.eng.timer.observer;

import java.util.ArrayList;
import java.util.List;

public class TimerSubject {
    List<TimerObserver> observers = new ArrayList<>();

    public void attach(TimerObserver observer){
        this.observers.add(observer);
    }

    public void detach(TimerObserver observer){
        if(observers.contains(observer)){
            this.observers.remove(observer);
        }
    }

    public void notifyRemaningTime(){
        for(TimerObserver observer : observers){
            observer.update();
        }
    }

    public void notifyTimeExpired(){
        for(TimerObserver observer : observers){
            observer.conclude();
        }
    }
}

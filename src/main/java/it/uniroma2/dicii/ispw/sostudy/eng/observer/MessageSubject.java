package it.uniroma2.dicii.ispw.sostudy.eng.observer;

import java.util.ArrayList;
import java.util.List;

public class MessageSubject {
    List<MessageObserver> observers = new ArrayList<>();

    public void attach(MessageObserver observer) {
        observers.add(observer);
    }

    public void detach(MessageObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for(MessageObserver observer : observers) {
            observer.update();
        }
    }
}

package chap8;

import java.util.List;

public class Feed implements Subject {

    private List<Observer> observers;

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(String tweet) {
        observers.forEach(o -> o.notify(tweet));
    }
}

package chap8;

public interface Subject {

    void registerObserver(Observer observer);

    void notifyObservers(String tweet);
}

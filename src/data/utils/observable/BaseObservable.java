package data.utils.observable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseObservable<T> implements Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    @Override
    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(T value) {
        observers.forEach(observer -> observer.onNewValue(value));
    }
}

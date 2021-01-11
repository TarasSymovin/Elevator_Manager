package data.utils.observable;

public interface Observable<T> {

    void subscribe(Observer<T> observer);
    void unsubscribe(Observer<T> observer);

    void notifyObservers(T value);

}

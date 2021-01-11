package data.utils.observable;

public interface Observer<T> {

    void onNewValue(T value);

}

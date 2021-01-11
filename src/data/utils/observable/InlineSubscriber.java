package data.utils.observable;

import data.utils.Action;

public class InlineSubscriber<T> implements Observer<T> {

    private final Action<T> callback;

    public InlineSubscriber(Action<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onNewValue(T value) {
        callback.call(value);
    }
}

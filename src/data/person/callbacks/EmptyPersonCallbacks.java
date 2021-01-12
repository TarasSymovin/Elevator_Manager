package data.person.callbacks;

import data.elevator.Elevator;
import data.person.Person;

public class EmptyPersonCallbacks implements PersonCallbacks {

    private static EmptyPersonCallbacks instance = null;

    public static EmptyPersonCallbacks getInstance() {
        if (instance == null) instance = new EmptyPersonCallbacks();
        return instance;
    }

    private EmptyPersonCallbacks() {
    }

    @Override
    public void onPersonQueueEntered(Person person, int floor, int elevator) {
    }

    @Override
    public void onPersonElevatorEntered(Person person, Elevator elevator) {
    }

    @Override
    public void onPersonElevatorLeft(Person person, Elevator elevator) {
    }
}

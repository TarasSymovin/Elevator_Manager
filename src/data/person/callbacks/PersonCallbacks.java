package data.person.callbacks;

import data.elevator.Elevator;
import data.person.Person;

public interface PersonCallbacks {

    void onPersonQueueEntered(Person person, int floor, int elevator);

    void onPersonElevatorEntered(Person person, Elevator elevator);

    void onPersonElevatorLeft(Person person, Elevator elevator);

}

package presentation.sample.presenter.elevators;

import data.building.Building;
import data.logger.Logger;
import data.person.Person;
import data.person.PersonThread;

import java.util.Random;

public class PersonThreadCreator {

    private final Building building;

    public PersonThreadCreator(Building building) {
        this.building = building;
    }

    public PersonThread create(Person person) {
        int from = randomFloorFrom();
        int to = randomFloorTo(from);
        PersonThread personThread = new PersonThread(
                person,
                building,
                from, to
        );
        Logger.getInstance().log(person + " goes from " + from + " to " + to + "." + "Thread created with name " + personThread.getName());
        return personThread;
    }

    private int randomFloorFrom() {
        return new Random().nextInt(building.floorsCount());
    }

    private int randomFloorTo(int from) {
        int to = new Random().nextInt(building.floorsCount());
        if (to != from) return to;

        if (to == building.floorsCount() - 1) return to - 1;
        else return to + 1;
    }
}

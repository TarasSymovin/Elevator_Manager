package data;

import data.building.Building;
import data.building.BuildingFloor;
import data.building.BuildingImpl;
import data.elevator.Elevator;
import data.elevator.strategy.PersonArrivalFirstStrategy;
import data.logger.Logger;
import data.person.Person;
import data.spawner.ElevatorsCreator;
import data.spawner.FloorsCreator;
import data.spawner.PersonSpawner;
import data.spawner.PersonThreadCreator;

import java.util.List;

public class DataTest {

    private static final int FLOORS_COUNT = 12;
    private static final int ELEVATORS_COUNT = 4;
    private static final float ELEVATOR_WEIGHT = 600f;
    private static final int ELEVATOR_SIZE = 6;

    private static final int PERSON_SPAWN_RATE = 1000;

    public static void main(String[] args) {
        Logger.getInstance().logTitle("App started");

        Building building = createBuilding();
        Logger.getInstance().logTitle("Building created successfully");

        PersonSpawner spawner = new PersonSpawner(
                PERSON_SPAWN_RATE,
                person -> startPersonThread(building, person)
        );
        spawner.startSpawn();

        Logger.getInstance().logTitle("App initialized successfully");
    }

    private static Building createBuilding() {
        List<BuildingFloor> floors = new FloorsCreator(ELEVATORS_COUNT).create(FLOORS_COUNT);
        Logger.getInstance().log("Floors created successfully");

        List<Elevator> elevators = new ElevatorsCreator(
                ELEVATOR_WEIGHT,
                ELEVATOR_SIZE,
                new PersonArrivalFirstStrategy()
        ).create(ELEVATORS_COUNT);
        Logger.getInstance().log("Elevators created successfully");

        return new BuildingImpl(floors, elevators);
    }

    private static void startPersonThread(Building building, Person person) {
        new PersonThreadCreator(building).create(person).start();
    }
}

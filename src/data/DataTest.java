package data;

import data.building.Building;
import data.building.BuildingFloor;
import data.building.BuildingImpl;
import data.elevator.Elevator;
import data.elevator.ElevatorControllable;
import data.elevator.ElevatorImpl;
import data.elevator.ElevatorThread;
import data.elevator.strategy.DumbElevatorStrategy;
import data.floor.FloorImpl;
import data.logger.Logger;
import data.spawner.PersonSpawner;

import java.util.ArrayList;
import java.util.List;

public class DataTest {

    private static final int FLOORS_COUNT = 12;
    private static final int ELEVATORS_COUNT = 4;
    private static final float ELEVATOR_WEIGHT = 600f;
    private static final int ELEVATOR_SIZE = 6;

    public static void main(String[] args) {
        Logger.getInstance().logTitle("App started");

        Building building = createBuilding();
        Logger.getInstance().logTitle("Building created successfully");

        PersonSpawner spawner = new PersonSpawner(building);
        spawner.startSpawn();

        Logger.getInstance().logTitle("App initialized successfully");
    }

    private static Building createBuilding() {
        List<BuildingFloor> floors = createFloors(FLOORS_COUNT);
        Logger.getInstance().log("Floors created successfully");

        List<Elevator> elevators = createElevators(ELEVATORS_COUNT);
        Logger.getInstance().log("Elevators created successfully");

        return new BuildingImpl(floors, elevators);
    }

    private static List<BuildingFloor> createFloors(int count) {
        List<BuildingFloor> floors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BuildingFloor floor = createFloor();
            Logger.getInstance().log("Created floor " + i);
            floors.add(floor);
        }
        return floors;
    }

    private static FloorImpl createFloor() {
        return new FloorImpl(ELEVATORS_COUNT);
    }

    private static List<Elevator> createElevators(int count) {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ElevatorControllable elevator = createElevator(i);
            Logger.getInstance().log(elevator + " created. Index: " + i);

            ElevatorThread elevatorThread = createElevatorThread(elevator);
            elevators.add(elevatorThread);
            elevatorThread.start();
        }
        return elevators;
    }

    private static ElevatorThread createElevatorThread(ElevatorControllable elevator) {
        return new ElevatorThread(elevator);
    }

    private static ElevatorImpl createElevator(int index) {
        return new ElevatorImpl(String.valueOf(index), ELEVATOR_WEIGHT, ELEVATOR_SIZE, new DumbElevatorStrategy());
    }
}

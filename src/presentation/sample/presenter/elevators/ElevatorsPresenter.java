package presentation.sample.presenter.elevators;

import data.spawner.PersonThreadCreator;
import data.building.Building;
import data.building.BuildingFloor;
import data.building.BuildingImpl;
import data.elevator.listener.ElevatorMovementListener;
import data.logger.Logger;
import data.person.Person;
import data.person.PersonThread;
import data.person.callbacks.PersonCallbacks;
import data.spawner.ElevatorsCreator;
import data.spawner.FloorsCreator;
import data.spawner.PersonSpawner;
import presentation.sample.enums.ElevatorState;
import presentation.sample.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElevatorsPresenter implements IElevatorsPresenter,
        IElevatorsProgressListener, IPassengerProgressListener,
        PersonCallbacks, ElevatorMovementListener {

    private static final float ELEVATOR_WEIGHT = 600f;
    private static final int ELEVATOR_SIZE = 6;

    private static final int FLOORS_COUNT = 10;
    private static final int ELEVATORS_COUNT = 4;

    private IElevatorsScene view;

    private final List<Elevator> elevators;
    private final List<Passenger> passengers = new ArrayList<>();

    private final PersonSpawner personSpawner;

    public ElevatorsPresenter() {
        List<BuildingFloor> floors = new FloorsCreator(ELEVATORS_COUNT).create(FLOORS_COUNT);

        List<data.elevator.Elevator> elevators = new ElevatorsCreator(ELEVATOR_WEIGHT, ELEVATOR_SIZE)
                .create(ELEVATORS_COUNT);
        Logger.getInstance().log("Elevators created successfully");

        Building building = new BuildingImpl(floors, elevators);

        this.elevators = Collections.unmodifiableList(parseElevators(elevators));

        personSpawner = new PersonSpawner(person -> startPersonThread(building, person));
        personSpawner.startSpawn();
    }

    @Override
    public void setView(IElevatorsScene view) {
        this.view = view;
    }

    @Override
    public void onElevatorFloorChanged(int elevatorID, int newFloor) {
        Elevator elevator = findElevator(elevatorID);
        if (elevator != null) {
            elevator.setFloor(newFloor);
        }
    }

    @Override
     public void onElevatorDeparted(int elevatorID) {
        Elevator elevator = findElevator(elevatorID);
        if (elevator != null) {
            elevator.setState(ElevatorState.MOVING);
        }
    }

    @Override
    public void onElevatorArrived(int elevatorID) {
        Elevator elevator = findElevator(elevatorID);
        if (elevator != null) {
            elevator.setState(ElevatorState.WAITING);
        }

        // TODO notify elevator

        // to continue animations
        // call move elevator
        // or
        // call move passenger into elevator
        // call move passenger from elevator
    }

    @Override
    public void onPassengerSpawned(int passengerID) {

    }

    @Override
    public void onPassengerEnteredElevator(int passengerID, int elevatorID) {
        // TODO find elevator and reset delayed move timer
    }

    @Override
    public void onPassengerExitedElevator(int passengerID, int elevatorID) {
        // TODO find elevator and reset delayed move timer
    }

    @Override
    public void onPassengerDeleted(int passengerID) {
    }

    @Override
    public void onElevatorMovingToFloor(data.elevator.Elevator elevator, int floor) {
        view.moveElevatorToFloor(elevator.getElevatorId(), floor);
    }

    @Override
    public void onPersonQueueEntered(Person person, int elevator) {
        // TODO VIEW SIDE!!!!
    }

    @Override
    public void onPersonElevatorEntered(Person person, data.elevator.Elevator elevator) {
        view.movePassengerIntoElevator(person.getPersonId(), elevator.getElevatorId());
    }

    @Override
    public void onPersonElevatorLeft(Person person, data.elevator.Elevator elevator) {
        view.movePassengerFromElevator(person.getPersonId(), elevator.getElevatorId());
    }

    private void startPersonThread(Building building, Person person) {
        PersonThread thread = new PersonThreadCreator(building).create(person);
        passengers.add(parsePassenger(thread, thread.sourceFloor()));
        thread.start();
    }

    private static Passenger parsePassenger(PersonThread person, int id) {
        return new Passenger(id, person.sourceFloor());
    }

    private static List<Elevator> parseElevators(List<data.elevator.Elevator> elevators) {
        List<Elevator> parsedElevators = new ArrayList<>();
        for (int i = 0; i < elevators.size(); i++) {
            Elevator elevator = new Elevator(i, 0);
            parsedElevators.add(elevator);
        }
        return parsedElevators;
    }

    public int getFloorsCount() {
        return FLOORS_COUNT;
    }

    public int getElevatorsCount() {
        return ELEVATORS_COUNT;
    }

    public Passenger findPassenger(int passengerID) {
        for (Passenger passenger : passengers){
            if (passenger.getId() == passengerID)
                return passenger;
        }

        return null;
    }

    public Elevator findElevator(int elevatorID) {
        for (Elevator elevator : elevators){
            if (elevator.getId() == elevatorID)
                return elevator;
        }

        return null;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}

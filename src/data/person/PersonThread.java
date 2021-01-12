package data.person;

import data.building.Building;
import data.building.BuildingConsumer;
import data.elevator.Elevator;
import data.logger.Logger;
import data.person.callbacks.EmptyPersonCallbacks;
import data.person.callbacks.PersonCallbacks;

import java.util.List;
import java.util.Random;

public class PersonThread extends Thread implements Person, BuildingConsumer {

    private static final int NO_INDEX = -1;
    private final Person person;

    private final Building building;

    private final int sourceFloor;
    private final int targetFloor;

    private PersonCallbacks callbacks = EmptyPersonCallbacks.getInstance();

    private int elevatorIndex = NO_INDEX;

    private final Object queueLock = new Object();
    private final Object elevatorLock = new Object();

    private boolean reachedQueueHead = false;
    private boolean reachedDestination = false;

    public PersonThread(Person person, Building building, int sourceFloor, int targetFloor) {
        this.person = person;
        this.building = building;
        this.sourceFloor = sourceFloor;
        this.targetFloor = targetFloor;
    }

    public void setCallbacks(PersonCallbacks callbacks) {
        if (callbacks == null) this.callbacks = EmptyPersonCallbacks.getInstance();
        else this.callbacks = callbacks;
    }

    @Override
    public void run() {
        Logger.getInstance().log("Started person thread " + getName() + " - " + person);

        List<Integer> smallestQueueIndicesOnSourceFloor = building.smallestQueueIndicesAtFloor(sourceFloor);
        elevatorIndex = resolveRandomSmallestQueueIndex(smallestQueueIndicesOnSourceFloor);
        Logger.getInstance().log(person + " decided to enter queue " + elevatorIndex);

        building.enterQueue(this);
        Logger.getInstance().log(person + " entered queue " + elevatorIndex);

        callbacks.onPersonQueueEntered(this, elevatorIndex);

        do {
            waitForReachingElevator();
        } while (!building.enterElevator(this));
        Logger.getInstance().log(person + " entered elevator " + elevatorIndex);

        do {
            waitForReachingTargetFloor();
        } while (!building.leaveElevator(this));
        Logger.getInstance().log(person + " reached the target floor " + targetFloor);

        building.leaveElevator(this);

        Logger.getInstance().log("");
    }

    @Override
    public String getPersonName() {
        return person.getPersonName();
    }

    @Override
    public float getWeight() {
        return person.getWeight();
    }

    @Override
    public int elevatorIndex() {
        return elevatorIndex;
    }

    @Override
    public void onQueueEntered() {
    }

    @Override
    public void onAvailableToEnterElevator() {
        handleReachedElevator();
    }

    @Override
    public int sourceFloor() {
        return sourceFloor;
    }

    @Override
    public int destinationFloor() {
        return targetFloor;
    }

    @Override
    public void onMovedFromQueueToElevator(Elevator elevator) {
        callbacks.onPersonElevatorEntered(this, elevator);
    }

    @Override
    public void onElevatorDockedToFloor(Elevator elevator, int floor) {
        if (floor == targetFloor) {
            handleReachedTargetFloor();
            callbacks.onPersonElevatorLeft(this, elevator);
        }
    }

    private int resolveRandomSmallestQueueIndex(List<Integer> smallestQueueIndices) {
        int randomIndexOfSmallestQueues = new Random().nextInt(smallestQueueIndices.size());
        return smallestQueueIndices.get(randomIndexOfSmallestQueues);
    }

    private void waitForReachingElevator() {
        synchronized (queueLock) {
            try {
                while (!reachedQueueHead) {
                    queueLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        reachedQueueHead = false;
    }

    private void handleReachedElevator() {
        synchronized (queueLock) {
            reachedQueueHead = true;
            queueLock.notify();
        }
    }

    private void waitForReachingTargetFloor() {
        synchronized (elevatorLock) {
            try {
                while (!reachedDestination) {
                    elevatorLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reachedDestination = false;
        }
    }

    private void handleReachedTargetFloor() {
        synchronized (elevatorLock) {
            reachedDestination = true;
            elevatorLock.notify();
        }
    }
}

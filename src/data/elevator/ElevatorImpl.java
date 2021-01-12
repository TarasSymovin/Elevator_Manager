package data.elevator;

import data.elevator.strategy.ElevatorStrategy;
import data.utils.observable.Observable;
import data.utils.observable.SimpleObservable;

import java.util.*;

public class ElevatorImpl implements ElevatorControllable {

    private final int id;

    private final String name;

    private final float maxWeight;
    private final int maxSize;

    private final ElevatorStrategy movementStrategy;

    private int currentFloor;

    private final Observable<Integer> floorObservable = new SimpleObservable<>();

    private boolean isOpened = true;

    private final Set<Integer> calledFloors = new TreeSet<>();

    private final List<ElevatorConsumer> consumers = new ArrayList<>();

    private final Observable<List<ElevatorConsumer>> consumersObservable = new SimpleObservable<>();

    public ElevatorImpl(int id, String name, float maxWeight, int maxSize, ElevatorStrategy strategy) {
        this(id, name, maxWeight, maxSize, strategy, 1);
    }

    public ElevatorImpl(int id, String name, float maxWeight, int maxSize, ElevatorStrategy strategy, int initialFloor) {
        this.id = id;
        this.name = name;
        this.maxWeight = maxWeight;
        this.maxSize = maxSize;
        this.movementStrategy = strategy;
        this.currentFloor = initialFloor;
    }

    @Override
    public int getElevatorId() {
        return id;
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public void setCurrentFloor(int floor) {
        currentFloor = floor;
    }

    @Override
    public Observable<Integer> getFloorObservable() {
        return floorObservable;
    }

    @Override
    public ElevatorStrategy getMovementStrategy() {
        return movementStrategy;
    }

    @Override
    public boolean isOpened() {
        return isOpened;
    }

    @Override
    public void setOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    public Set<Integer> getCalledFloors() {
        return Collections.unmodifiableSet(calledFloors);
    }

    @Override
    public boolean callAtFloor(int floor) {
        if (currentFloor == floor && isOpened) return false;
        calledFloors.add(floor);
        return true;
    }

    @Override
    public void removeCalledFloor(int floor) {
        calledFloors.remove(floor);
    }

    @Override
    public List<ElevatorConsumer> getConsumers() {
        return consumers;
    }

    @Override
    public boolean enter(ElevatorConsumer consumer) {
        if (!canEnter(consumer)) return false;
        consumers.add(consumer);
        notifyConsumersChanged();
        return true;
    }

    @Override
    public boolean leave(ElevatorConsumer consumer) {
        if (!canLeave(consumer)) return false;
        consumers.remove(consumer);
        notifyConsumersChanged();
        return true;
    }

    @Override
    public Observable<List<ElevatorConsumer>> getConsumersObservable() {
        return consumersObservable;
    }

    @Override
    public String toString() {
        return "Elevator [" + name + "]";
    }

    private boolean canEnter(ElevatorConsumer consumer) {
        return isOpened &&
                consumer.sourceFloor() == currentFloor &&
                currentWeight() + consumer.getWeight() <= maxWeight &&
                currentSize() + 1 <= maxSize;
    }

    private boolean canLeave(ElevatorConsumer consumer) {
        return isOpened &&
                consumer.destinationFloor() == currentFloor;
    }

    private float currentWeight() {
        return consumers.stream()
                .map(ElevatorConsumer::getWeight)
                .reduce(0f, Float::sum);
    }

    private float currentSize() {
        return consumers.size();
    }

    private void notifyConsumersChanged() {
        consumersObservable.notifyObservers(Collections.unmodifiableList(consumers));
    }
}

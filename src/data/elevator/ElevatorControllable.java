package data.elevator;

import data.elevator.strategy.ElevatorStrategy;

public interface ElevatorControllable extends Elevator {

    ElevatorStrategy getMovementStrategy();

    void setCurrentFloor(int floor);

    void removeCalledFloor(int floor);

    void setOpened(boolean isOpened);

    boolean isMovingUpwards();
    void setMovingUpwards(boolean movingUpwards);
}

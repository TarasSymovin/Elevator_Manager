package data.elevator.listener;

import data.elevator.Elevator;

public interface ElevatorMovementListener {

    void onElevatorMovingToFloor(Elevator elevator, int floor);

}

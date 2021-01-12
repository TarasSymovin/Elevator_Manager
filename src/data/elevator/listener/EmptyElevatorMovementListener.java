package data.elevator.listener;

import data.elevator.Elevator;

public class EmptyElevatorMovementListener implements ElevatorMovementListener {

    private static EmptyElevatorMovementListener instance = null;

    public static EmptyElevatorMovementListener getInstance() {
        if (instance == null) instance = new EmptyElevatorMovementListener();
        return instance;
    }

    private EmptyElevatorMovementListener() {
    }

    @Override
    public void onElevatorMovingToFloor(Elevator elevator, int floor) {
    }
}

package presentation.sample.types;

import presentation.sample.enums.ElevatorState;

public class Elevator {
    private int id;
    private int floor;
    private ElevatorState state;

    private data.elevator.Elevator elevator;

    public Elevator(int id, int floor, data.elevator.Elevator elevator) {
        this.id = id;
        this.floor = floor;
        this.state = ElevatorState.WAITING;
        this.elevator = elevator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public data.elevator.Elevator getElevator() {
        return elevator;
    }

    public ElevatorState getState() {
        return state;
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }
}

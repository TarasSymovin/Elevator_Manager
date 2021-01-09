package sample.types;

import sample.ElevatorsScene;
import sample.enums.ElevatorState;

public class Elevator {
    private int id;
    private ElevatorState state;
    private int floor;

    public Elevator(int id, int floor) {
        this.id = id;
        this.floor = floor;
        this.state = ElevatorState.WAITING;
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

    public ElevatorState getState() {
        return state;
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }
}

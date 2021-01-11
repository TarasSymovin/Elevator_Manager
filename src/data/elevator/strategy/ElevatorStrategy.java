package data.elevator.strategy;

import data.elevator.Elevator;

public interface ElevatorStrategy {

    boolean hasWhereToGo(Elevator elevator);

    int resolveFloorToGo(Elevator elevator);

}

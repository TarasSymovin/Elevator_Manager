package data.elevator.strategy;

import data.elevator.ElevatorControllable;

public interface ElevatorStrategy {

    boolean hasWhereToGo(ElevatorControllable elevator);

    int resolveFloorToGo(ElevatorControllable elevator);

}

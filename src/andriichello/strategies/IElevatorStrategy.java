package andriichello.strategies;

import andriichello.types.Elevator;

public interface IElevatorStrategy {

    boolean isThereDestinations(Elevator elevator);
    int findDestination(Elevator elevator) throws Exception;
}

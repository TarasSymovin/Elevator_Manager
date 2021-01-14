package andriichello.strategies;

import andriichello.types.Passenger;
import andriichello.types.Elevator;

import java.util.List;

public interface IElevatorChoosingStrategy {
    Elevator chooseElevator(List<Elevator> elevators, Passenger passenger) throws Exception;
}

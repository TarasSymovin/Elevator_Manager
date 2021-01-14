package andriichello.strategies;

import andriichello.types.Passenger;
import andriichello.types.Elevator;

import java.util.List;

public class SimpleElevatorChoosingStrategy implements IElevatorChoosingStrategy {

    @Override
    public Elevator chooseElevator(List<Elevator> elevators, Passenger passenger) throws Exception {
        if (elevators == null || elevators.isEmpty())
            throw new Exception(getClass().getName() + " there is no elevators to choose from");

        if (passenger == null)
            throw new Exception(getClass().getName() + " there is no passenger for which to choose");

        if (passenger.getDepartureFloor() == passenger.getDestinationFloor())
            throw new Exception(getClass().getName() + " passenger doesn't need an elevator. Departure floor and destination floor are equal");

        return elevators.stream().min((o1, o2) -> {
            int a1 = o1.getWaitingPassengersCount() + o1.getMovingPassengersCount();
            int a2 =  o2.getWaitingPassengersCount() + o2.getMovingPassengersCount();
            return a1 - a2;
        }).orElseThrow(() -> new Exception(getClass().getName() + " Error"));
    }
}

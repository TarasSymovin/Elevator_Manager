package andriichello.strategies;

import andriichello.types.Elevator;

public class OwnershipElevatorStrategy implements IElevatorStrategy {

    @Override
    public boolean isThereDestinations(Elevator elevator) {
        if (elevator == null)
            return false;

        return !elevator.getWaitingPassengers().isEmpty() || !elevator.getMovingPassengers().isEmpty();
    }

    @Override
    public int findDestination(Elevator elevator) throws Exception {
        if (!isThereDestinations(elevator))
            throw new Exception(getClass().getName() + " has no destinations to choose from");

        if (elevator.getMovingPassengers().isEmpty()) {
            // go by first passenger's departure floor (to pick him up)
            return elevator.getWaitingPassengers().get(0).getDepartureFloor();
        } else {
            // go by first passenger destination floor
            return elevator.getMovingPassengers().get(0).getDestinationFloor();
        }
    }
}

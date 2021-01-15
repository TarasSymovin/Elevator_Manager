package andriichello.strategies;

import andriichello.states.ElevatorState;
import andriichello.types.Elevator;
import andriichello.types.Passenger;

import java.util.ArrayList;
import java.util.List;

public class InterruptionElevatorStrategy implements IElevatorStrategy {

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
            if (elevator.getWaitingPassengers().isEmpty()) {
                // go by first passenger destination floor
                return elevator.getMovingPassengers().get(0).getDestinationFloor();
            } else {
                if (elevator.getState() == ElevatorState.waiting) {
                    // the elevator is waiting for signal to move it to other floor, so here we can choose the closest possible floor

                    Integer closest = null;
                    // finding closest floor to current from passengers, who is waiting for elevator
                    for (Passenger passenger : elevator.getWaitingPassengers()) {
                        if (closest == null || (Math.abs(elevator.getCurrentFloor() - passenger.getDepartureFloor()) < Math.abs(elevator.getCurrentFloor() - closest)))
                            closest = passenger.getDepartureFloor();
                    }

                    // finding closest floor to current from passengers, who is moving inside the elevator
                    for (Passenger passenger : elevator.getMovingPassengers()) {
                        if (closest == null || (Math.abs(elevator.getCurrentFloor() - passenger.getDestinationFloor()) < Math.abs(elevator.getCurrentFloor() - closest)))
                            closest = passenger.getDestinationFloor();
                    }

                    if (closest != null)
                        return closest;
                } else {
                    // the elevator is moving, so it is possible only to stop by the way of elevator movement

                    Integer closest = null;
                    // finding closest floor in current elevator movement way from passengers, who is waiting for elevator
                    for (Passenger passenger : elevator.getWaitingPassengers()) {
                        if (passenger.getDepartureFloor() < Math.max(elevator.getCurrentFloor(), elevator.getDestinationFloor()) && passenger.getDepartureFloor() > Math.min(elevator.getCurrentFloor(), elevator.getDestinationFloor())) {
                            if (closest == null || (Math.abs(elevator.getCurrentFloor() - passenger.getDepartureFloor()) < Math.abs(elevator.getCurrentFloor() - closest)))
                                closest = passenger.getDepartureFloor();
                        }
                    }

                    // finding closest floor in current elevator movement way from passengers, who is moving inside the elevator
                    for (Passenger passenger : elevator.getMovingPassengers()) {
                        if (passenger.getDepartureFloor() < Math.max(elevator.getCurrentFloor(), elevator.getDestinationFloor()) && passenger.getDepartureFloor() > Math.min(elevator.getCurrentFloor(), elevator.getDestinationFloor())) {
                            if (closest == null || (Math.abs(elevator.getCurrentFloor() - passenger.getDestinationFloor()) < Math.abs(elevator.getCurrentFloor() - closest)))
                                closest = passenger.getDestinationFloor();
                        }
                    }
                    if (closest != null)
                        return closest;
                }
            }
        }

        throw new Exception(getClass().getName() + " error");
    }
}

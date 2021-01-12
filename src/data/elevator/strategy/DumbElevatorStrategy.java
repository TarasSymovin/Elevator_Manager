package data.elevator.strategy;

import data.elevator.ElevatorControllable;

public class DumbElevatorStrategy implements ElevatorStrategy {

    @Override
    public boolean hasWhereToGo(ElevatorControllable elevator) {
        return !elevator.getConsumers().isEmpty() || !elevator.getCalledFloors().isEmpty();
    }

    @Override
    public int resolveFloorToGo(ElevatorControllable elevator) {
        if (!hasWhereToGo(elevator)) throw new IllegalStateException(
                "Cannot resolve elevator floor to go because there are no consumers and no called floors"
        );

        // Check consumers inside first
        if (!elevator.getConsumers().isEmpty()) {
            return elevator.getConsumers().get(0).destinationFloor();
        }

        // Check called floors otherwise
        if (!elevator.getCalledFloors().isEmpty()) {
            return elevator.getCalledFloors().stream().findFirst().get();
        }

        throw new IllegalStateException("Elevator strategy felt into error");
    }

}

package data.elevator.strategy;

import data.elevator.ElevatorControllable;

public class StrategyUtils {

    public static void validateElevatorDirection(ElevatorControllable elevator) {
        if (elevator.isMovingUpwards()) {
            boolean hasHigherCalledFloors = elevator.getCalledFloors().stream()
                    .anyMatch(floor -> floor > elevator.getCurrentFloor());
            boolean hasHigherTargetFloors = elevator.getConsumers().stream()
                    .anyMatch(consumer -> consumer.destinationFloor() > elevator.getCurrentFloor());

            if (!hasHigherCalledFloors && !hasHigherTargetFloors) {
                elevator.setMovingUpwards(false);
            }
        } else {
            boolean hasLowerCalledFloors = elevator.getCalledFloors().stream()
                    .anyMatch(floor -> floor < elevator.getCurrentFloor());
            boolean hasLowerTargetFloors = elevator.getConsumers().stream()
                    .anyMatch(consumer -> consumer.destinationFloor() < elevator.getCurrentFloor());

            if (!hasLowerTargetFloors && !hasLowerCalledFloors) {
                elevator.setMovingUpwards(true);
            }
        }
    }

}

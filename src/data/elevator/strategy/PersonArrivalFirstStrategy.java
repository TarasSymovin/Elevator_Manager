package data.elevator.strategy;

import data.elevator.Elevator;
import data.elevator.ElevatorConsumer;
import data.elevator.ElevatorControllable;

import java.util.Optional;
import java.util.function.Predicate;

public class PersonArrivalFirstStrategy implements ElevatorStrategy {

    @Override
    public boolean hasWhereToGo(ElevatorControllable elevator) {
        return !elevator.getCalledFloors().isEmpty() || !elevator.getConsumers().isEmpty();
    }

    @Override
    public int resolveFloorToGo(ElevatorControllable elevator) {
        if (elevator.isMovingUpwards()) {
            boolean hasHigherTargetFloors = elevator.getConsumers().stream()
                    .anyMatch(consumer -> consumer.destinationFloor() > elevator.getCurrentFloor());
            boolean hasHigherCalledFloors = elevator.getCalledFloors().stream()
                    .anyMatch(floor -> floor > elevator.getCurrentFloor());

            if (!hasHigherTargetFloors && !hasHigherCalledFloors) {
                elevator.setMovingUpwards(false);
                return resolveLowerFloor(elevator);
            }
            return resolveHigherFloor(elevator);

        } else {
            boolean hasLowerTargetFloors = elevator.getConsumers().stream()
                    .anyMatch(consumer -> consumer.destinationFloor() < elevator.getCurrentFloor());
            boolean hasLowerCalledFloors = elevator.getCalledFloors().stream()
                    .anyMatch(floor -> floor < elevator.getCurrentFloor());

            if (!hasLowerTargetFloors && !hasLowerCalledFloors) {
                elevator.setMovingUpwards(true);
                return resolveHigherFloor(elevator);
            }
            return resolveLowerFloor(elevator);
        }
    }

    private int resolveHigherFloor(Elevator elevator) {
        return resolveFloor(elevator, floor -> floor > elevator.getCurrentFloor());
    }

    private int resolveLowerFloor(Elevator elevator) {
        return resolveFloor(elevator, floor -> floor < elevator.getCurrentFloor());
    }

    private int resolveFloor(Elevator elevator, Predicate<Integer> floorComparator) {
        Optional<Integer> closestDestinationFloor = elevator.getConsumers().stream()
                .map(ElevatorConsumer::destinationFloor)
                .filter(floorComparator)
                .min(Integer::compareTo);

        if (closestDestinationFloor.isPresent()) {
            return closestDestinationFloor.get();
        }

        Optional<Integer> closestCalledFloor = elevator.getCalledFloors().stream()
                .filter(floorComparator)
                .min(Integer::compareTo);

        if (closestCalledFloor.isPresent()) {
            return closestCalledFloor.get();
        }

        throw new IllegalStateException("Stub");
    }
}

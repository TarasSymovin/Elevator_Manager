package data.elevator.strategy;

import data.elevator.Elevator;
import data.elevator.ElevatorConsumer;
import data.elevator.ElevatorControllable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return resolveFloors(elevator, floor -> floor > elevator.getCurrentFloor())
                .stream()
                .min(Integer::compareTo)
                .get();
    }

    private int resolveLowerFloor(Elevator elevator) {
        return resolveFloors(elevator, floor -> floor < elevator.getCurrentFloor())
                .stream()
                .max(Integer::compareTo)
                .get();
    }

    private List<Integer> resolveFloors(Elevator elevator, Predicate<Integer> floorComparator) {
        List<Integer> destinationFloors = elevator.getConsumers().stream()
                .map(ElevatorConsumer::destinationFloor)
                .filter(floorComparator)
                .collect(Collectors.toList());

        if (!destinationFloors.isEmpty()) {
            return destinationFloors;
        }

        List<Integer> closestCalledFloors = elevator.getCalledFloors().stream()
                .filter(floorComparator)
                .collect(Collectors.toList());

        if (!closestCalledFloors.isEmpty()) {
            return closestCalledFloors;
        }

        throw new IllegalStateException("Stub");
    }
}

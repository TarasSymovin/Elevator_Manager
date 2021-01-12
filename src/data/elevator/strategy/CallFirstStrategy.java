package data.elevator.strategy;

import data.elevator.Elevator;
import data.elevator.ElevatorConsumer;
import data.elevator.ElevatorControllable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CallFirstStrategy implements ElevatorStrategy {

    @Override
    public boolean hasWhereToGo(ElevatorControllable elevator) {
        return !elevator.getCalledFloors().isEmpty() || !elevator.getConsumers().isEmpty();
    }

    @Override
    public int resolveFloorToGo(ElevatorControllable elevator) {
        StrategyUtils.validateElevatorDirection(elevator);
        if (elevator.isMovingUpwards()) {
            return resolveHigherFloor(elevator);
        } else {
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
        List<Integer> closestCalledFloors = elevator.getCalledFloors().stream()
                .filter(floorComparator)
                .collect(Collectors.toList());

        if (!closestCalledFloors.isEmpty()) {
            return closestCalledFloors;
        }

        List<Integer> destinationFloors = elevator.getConsumers().stream()
                .map(ElevatorConsumer::destinationFloor)
                .filter(floorComparator)
                .collect(Collectors.toList());

        if (!destinationFloors.isEmpty()) {
            return destinationFloors;
        }

        throw new IllegalStateException("Stub");
    }
}

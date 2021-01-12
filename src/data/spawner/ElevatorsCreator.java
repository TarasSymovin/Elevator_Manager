package data.spawner;

import data.elevator.Elevator;
import data.elevator.ElevatorControllable;
import data.elevator.ElevatorImpl;
import data.elevator.ElevatorThread;
import data.elevator.strategy.DumbElevatorStrategy;
import data.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ElevatorsCreator {

    private final float maxWeight;
    private final int maxSize;

    public ElevatorsCreator(float maxWeight, int maxSize) {
        this.maxWeight = maxWeight;
        this.maxSize = maxSize;
    }

    public List<Elevator> create(int count) {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ElevatorControllable elevator = createSingle(i);

            Logger.getInstance().log(elevator + " created. Index: " + i);

            ElevatorThread elevatorThread = new ElevatorThread(elevator);
            elevators.add(elevatorThread);
            elevatorThread.start();
        }
        return elevators;
    }

    private ElevatorImpl createSingle(int index) {
        return new ElevatorImpl(
                index,
                String.valueOf(index),
                maxWeight,
                maxSize,
                new DumbElevatorStrategy()
        );
    }

}

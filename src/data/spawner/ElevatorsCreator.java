package data.spawner;

import data.elevator.Elevator;
import data.elevator.ElevatorControllable;
import data.elevator.ElevatorImpl;
import data.elevator.ElevatorThread;
import data.elevator.listener.ElevatorMovementListener;
import data.elevator.strategy.ElevatorStrategy;
import data.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ElevatorsCreator {

    private final float maxWeight;
    private final int maxSize;
    private final ElevatorStrategy strategy;

    private ElevatorMovementListener listener;

    public ElevatorsCreator(float maxWeight, int maxSize, ElevatorStrategy strategy) {
        this.maxWeight = maxWeight;
        this.maxSize = maxSize;
        this.strategy = strategy;
    }

    public ElevatorsCreator withListener(ElevatorMovementListener listener) {
        this.listener = listener;
        return this;
    }

    public List<Elevator> create(int count) {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ElevatorControllable elevator = createSingle(i);

            Logger.getInstance().log(elevator + " created. Index: " + i);

            ElevatorThread elevatorThread = new ElevatorThread(elevator);
            elevatorThread.setListener(listener);
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
                strategy
        );
    }

}

package data.building;

import data.elevator.Elevator;
import data.elevator.ElevatorConsumer;
import data.floor.QueueConsumer;

public interface BuildingConsumer extends QueueConsumer, ElevatorConsumer {

    void onMovedFromQueueToElevator(Elevator elevator);

}

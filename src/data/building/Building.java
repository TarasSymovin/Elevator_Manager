package data.building;

import java.util.List;

public interface Building {

    int floorsCount();
    int elevatorsCount();

    List<Integer> smallestQueueIndicesAtFloor(int floor);

    void enterQueue(BuildingConsumer consumer);
    boolean enterElevator(BuildingConsumer consumer);
    boolean leaveElevator(BuildingConsumer consumer);
}

package data.spawner;

import data.building.BuildingFloor;
import data.elevator.Elevator;
import data.elevator.ElevatorControllable;
import data.elevator.ElevatorImpl;
import data.elevator.ElevatorThread;
import data.elevator.strategy.DumbElevatorStrategy;
import data.floor.Floor;
import data.floor.FloorImpl;
import data.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class FloorsCreator {

    private final int elevatorsCount;

    public FloorsCreator(int elevatorsCount) {
        this.elevatorsCount = elevatorsCount;
    }

    public List<BuildingFloor> create(int count) {
        List<BuildingFloor> floors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BuildingFloor floor = createSingle();
            Logger.getInstance().log("Created floor " + i);
            floors.add(floor);
        }
        return floors;
    }

    private FloorImpl createSingle() {
        return new FloorImpl(elevatorsCount);
    }

}

package data.building;

import data.floor.Floor;
import data.floor.QueueConsumer;

public interface BuildingFloor extends Floor {

    QueueConsumer getQueueHead(int elevator);

}

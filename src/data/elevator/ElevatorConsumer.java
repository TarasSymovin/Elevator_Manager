package data.elevator;

public interface ElevatorConsumer {

    float getWeight();

    int sourceFloor();
    int destinationFloor();

    void onElevatorDockedToFloor(Elevator elevator, int floor);
}

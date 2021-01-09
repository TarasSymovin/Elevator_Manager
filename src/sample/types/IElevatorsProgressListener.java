package sample.types;

public interface IElevatorsProgressListener {
    void onElevatorFloorChanged(int id, int newFloor);
    void onElevatorDeparted(int id);
    void onElevatorArrived(int id);
}

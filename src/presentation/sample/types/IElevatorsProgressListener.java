package presentation.sample.types;

public interface IElevatorsProgressListener {
    void onElevatorFloorChanged(int elevatorID, int newFloor);
    void onElevatorDeparted(int elevatorID);
    void onElevatorArrived(int elevatorID);
}

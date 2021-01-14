package andriichello.listeners;

public interface ElevatorsProgressListener {
    void onElevatorFloorChanged(int elevatorID, int newFloor);
    void onElevatorDeparted(int elevatorID);
    void onElevatorArrived(int elevatorID);
}

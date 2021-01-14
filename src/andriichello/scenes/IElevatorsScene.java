package andriichello.scenes;

public interface IElevatorsScene {
    void moveElevatorToFloor(int elevatorID, int newFloor);

    void spawnPassenger(int personID, int floor);
    void movePassengerIntoElevator(int personID, int elevatorID);
    void movePassengerFromElevator(int personID, int elevatorID);
}

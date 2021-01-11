package presentation.sample.types;

public interface IElevatorsScene {

    void moveElevatorToFloor(int elevatorID, int newFloor);

    void spawnPerson(int personID, int floor);
    void movePersonIntoElevator(int personID, int elevatorID);
    void movePersonFromElevator(int personID, int elevatorID);
}

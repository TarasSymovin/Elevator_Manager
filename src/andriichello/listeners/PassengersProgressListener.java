package andriichello.listeners;

public interface PassengersProgressListener {
    void onPassengerSpawned(int passengerID);
    void onPassengerStartedMovingToElevator(int passengerID, int elevatorID);
    void onPassengerEnteredElevator(int passengerID, int elevatorID);

    void onPassengerStartedMovingFromElevator(int passengerID, int elevatorID);
    void onPassengerExitedElevator(int passengerID, int elevatorID);
    void onPassengerDeleted(int passengerID);
}

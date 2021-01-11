package presentation.sample.types;

public interface IPassengerProgressListener {
    void onPassengerSpawned(int passengerID);
    void onPassengerEnteredElevator(int passengerID, int elevatorID);
    void onPassengerExitedElevator(int passengerID, int elevatorID);
    void onPassengerDeleted(int passengerID);
}

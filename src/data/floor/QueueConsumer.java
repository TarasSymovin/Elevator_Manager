package data.floor;

public interface QueueConsumer {

    int elevatorIndex();

    void onQueueEntered();

    void onAvailableToEnterElevator();

}

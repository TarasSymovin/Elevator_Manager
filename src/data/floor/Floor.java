package data.floor;

public interface Floor {

    int getQueueSizeAtElevator(int elevator);

    /**
     * @return true if user should call for elevator
     */
    boolean enterQueue(QueueConsumer person);
    void leaveQueue(QueueConsumer person);

}

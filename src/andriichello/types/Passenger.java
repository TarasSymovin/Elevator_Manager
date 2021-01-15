package andriichello.types;

import andriichello.states.PassengerState;

public class Passenger {
    private int mID;

    private int mDepartureFloor = 1;
    private int mDestinationFloor = 1;

    private PassengerState mState = PassengerState.waiting;

    public Passenger(int mID) {
        this.mID = mID;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public int getDepartureFloor() {
        return mDepartureFloor;
    }

    public void setDepartureFloor(int mDepartureFloor) {
        this.mDepartureFloor = mDepartureFloor;
    }

    public int getDestinationFloor() {
        return mDestinationFloor;
    }

    public void setDestinationFloor(int mDestinationFloor) {
        this.mDestinationFloor = mDestinationFloor;
    }

    public PassengerState getState() {
        return mState;
    }

    public void setState(PassengerState mState) {
        this.mState = mState;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id: " + mID + ", " +
                "departure: " + mDepartureFloor + ", " +
                "destination: " + mDestinationFloor + ", " +
                "state: " + mState.name() +
                " }";
    }
}

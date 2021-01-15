package andriichello.types;

import andriichello.states.ElevatorState;
import andriichello.strategies.IElevatorStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Elevator {
    private int mID;

    private int mCurrentFloor = 1;
    private int mDepartureFloor = 1;
    private int mDestinationFloor = 1;

    private ElevatorState mState = ElevatorState.waiting;

    private List<Passenger> mWaitingPassengers = new ArrayList<>();
    private List<Passenger> mMovingPassengers = new ArrayList<>();

    private IElevatorStrategy mStrategy;

    public Elevator(int mID) {
        this.mID = mID;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public int getCurrentFloor() {
        return mCurrentFloor;
    }

    public void setCurrentFloor(int mCurrentFloor) {
        this.mCurrentFloor = mCurrentFloor;
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

    public void setFloors(int floor) {
        this.mCurrentFloor = this.mDepartureFloor = this.mDestinationFloor = floor;
    }

    public ElevatorState getState() {
        return mState;
    }

    public void setState(ElevatorState mState) {
        this.mState = mState;
    }

    public List<Passenger> getWaitingPassengers() {
        return mWaitingPassengers;
    }

    public void setWaitingPassengers(List<Passenger> mWaitingPassengers) {
        this.mWaitingPassengers.clear();

        if (mWaitingPassengers != null && !mWaitingPassengers.isEmpty()) {
            mWaitingPassengers = mWaitingPassengers.stream().distinct().collect(Collectors.toList());
            this.mWaitingPassengers.addAll(mWaitingPassengers);
        }
    }

    public int getWaitingPassengersCount() {
        return mWaitingPassengers.size();
    }

    public boolean appendToWaitingPassengers(Passenger passenger) {
        // add weight control

        mWaitingPassengers.add(passenger);
        return true;
    }

    public boolean removeFromWaitingPassengers(Passenger passenger) {
        // add weight control

        mWaitingPassengers.remove(passenger);
        return true;
    }

    public List<Passenger> getMovingPassengers() {
        return mMovingPassengers;
    }

    public void setMovingPassengers(List<Passenger> mPassengers) {
        this.mMovingPassengers.clear();

        if (mPassengers != null && !mPassengers.isEmpty()) {
            mPassengers = mPassengers.stream().distinct().collect(Collectors.toList());
            this.mMovingPassengers.addAll(mPassengers);
        }
    }

    public int getMovingPassengersCount() {
        return mMovingPassengers.size();
    }

    public boolean appendToMovingPassengers(Passenger passenger) {
        // add weight control

        mMovingPassengers.add(passenger);
        return true;
    }

    public boolean removeFromMovingPassengers(Passenger passenger) {
        // add weight control

        mMovingPassengers.remove(passenger);
        return true;
    }

    public IElevatorStrategy getStrategy() {
        return mStrategy;
    }

    public void setStrategy(IElevatorStrategy mStrategy) {
        this.mStrategy = mStrategy;
    }

    public Integer nextFloor() {
        if (mStrategy != null && mStrategy.isThereDestinations(this)) {
            try {
                return mStrategy.findDestination(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "id: " + mID + ", " +
                "departure: " + mDepartureFloor + ", " +
                "current: " + mCurrentFloor + ", " +
                "destination: " + mDestinationFloor + ", " +
                "state: " + mState.name() +
                " }";
    }
}

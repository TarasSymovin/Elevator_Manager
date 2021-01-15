package andriichello.types;

import andriichello.listeners.ElevatorsProgressListener;
import andriichello.listeners.PassengersProgressListener;
import andriichello.scenes.ElevatorsSceneArgs;
import andriichello.states.ElevatorState;
import andriichello.states.PassengerState;
import andriichello.strategies.IElevatorChoosingStrategy;
import andriichello.strategies.IElevatorStrategy;
import andriichello.strategies.OwnershipElevatorStrategy;
import andriichello.strategies.SimpleElevatorChoosingStrategy;
import andriichello.types.views.ElevatorView;
import javafx.application.Platform;
import presentation.sample.types.IElevatorsProgressListener;
import andriichello.scenes.IElevatorsScene;
import presentation.sample.types.IPassengerProgressListener;

import java.util.*;

public class ElevatorSceneImitator implements ElevatorsProgressListener, PassengersProgressListener {
    private ElevatorsSceneArgs mArgs = new ElevatorsSceneArgs();

    private static int ELEVATOR_ID_COUNTER = 0;
    private static int PASSENGER_ID_COUNTER = 0;


    private List<Elevator> mElevators = new ArrayList<>();
    private List<Passenger> mPassengers = new ArrayList<>();

    private IElevatorsScene mElevatorsScene;

    public ElevatorSceneImitator(ElevatorsSceneArgs args) {
        if (args != null)
            mArgs = args;

        initialize();
    }

    public ElevatorSceneImitator(ElevatorsSceneArgs args, IElevatorsScene scene) {
        if (args != null)
            mArgs = args;

        this.mElevatorsScene = scene;

        initialize();
    }


    public void initialize() {
        for (int i = 0; i < mArgs.getElevatorsCount(); i++) {
            Elevator elevator = new Elevator(ELEVATOR_ID_COUNTER++);
            elevator.setFloors(1);
            elevator.setState(ElevatorState.waiting);
            elevator.setStrategy(mArgs.getElevatorStrategy());

            mElevators.add(elevator);
        }


    }

    private Timer mTimer = new Timer();

    public void start(int delay) {
        if (mArgs.getPassengersInitialCount() > 0) {
            for (int i = 0; i < mArgs.getPassengersInitialCount(); i++) {
                Passenger passenger = spawn();
            }
            force();
        }

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < mArgs.getPassengersSpawnAmount(); i++) {
                    Passenger passenger = spawn();
                    if (passenger == null)
                        continue;

                    for (Elevator elevator : mElevators) {
                        if (elevator.getWaitingPassengers().contains(passenger)) {
                            force(elevator);
                            break;
                        }
                    }
                }
            }
        }, delay, mArgs.getPassengersSpawnRate());
    }

    public void stop() {
        mTimer.cancel();
    }

    public Passenger spawn() {
        if (mArgs.getMaxPassengersCount() <= mPassengers.size() || mArgs == null || mArgs.getElevatorStrategy() == null)
            return null;

        Random r = new Random();

        int departure = r.nextInt(mArgs.getFloorsCount()) + 1;
        int destination = r.nextInt(mArgs.getFloorsCount()) + 1;
        if (departure == destination)
            return null;

        Passenger passenger = new Passenger(PASSENGER_ID_COUNTER++);
        passenger.setDepartureFloor(departure);
        passenger.setDestinationFloor(destination);

        try {
            Elevator elevator = mArgs.getElevatorChoosingStrategy().chooseElevator(mElevators, passenger);
            if (elevator != null && elevator.appendToWaitingPassengers(passenger)) {
                mPassengers.add(passenger);
                if (mElevatorsScene != null)
                    Platform.runLater(() -> {
                        mElevatorsScene.spawnPassenger(passenger.getID(), passenger.getDepartureFloor());
                    });

                System.out.println("Spawned: " + passenger.toString());
                return passenger;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void force() {
        if (mElevatorsScene == null)
            return;

        for (Elevator elevator : mElevators) {
            force(elevator);
        }
    }

    public void force(Elevator elevator) {
        if (elevator == null || mElevatorsScene == null)
            return;

        if (elevator.getState() == ElevatorState.waiting) {
            boolean shouldWait = false;
            // checking if there are passengers moving INTO the elevator
            if (elevator.getWaitingPassengersCount() > 0) {
                for (Passenger passenger : elevator.getWaitingPassengers()) {
                    if (elevator.getCurrentFloor() == passenger.getDepartureFloor()) {
                        if (passenger.getState() == PassengerState.waiting) {
                            Platform.runLater(() -> {
                                mElevatorsScene.movePassengerIntoElevator(passenger.getID(), elevator.getID());
                                System.out.println("Move passenger into: " + passenger.toString() + ", " + elevator.toString());
                            });
                            shouldWait = true;
                        }
                    }
                }
            }

            // checking if there are passengers moving FROM the elevator
            if (elevator.getMovingPassengersCount() > 0) {
                for (Passenger passenger : elevator.getMovingPassengers()) {
                    if (elevator.getCurrentFloor() == passenger.getDestinationFloor()) {
                        if (passenger.getState() == PassengerState.waiting) {
                            Platform.runLater(() -> {
                                mElevatorsScene.movePassengerFromElevator(passenger.getID(), elevator.getID());
                                System.out.println("Move passenger from: " + passenger.toString() + ", " + elevator.toString());
                            });
                            shouldWait = true;
                        }
                    }
                }
            }

            // elevator needs to be moved
            if (!shouldWait) {
                Integer next = elevator.nextFloor();
                if (next != null && elevator.getDestinationFloor() != next) {
                    elevator.setDestinationFloor(next);
                    Platform.runLater(() -> {
                        mElevatorsScene.moveElevatorToFloor(elevator.getID(), next);
                        System.out.println("Move elevator: " + elevator.toString());
                    });
                }
            }
        } else {
            Integer next = elevator.nextFloor();
            if (next != null && elevator.getDestinationFloor() != next) {
                if (elevator.getWaitingPassengersCount() > 0 && elevator.getMovingPassengersCount() > 0) {
                    System.out.println("line");
                }

                elevator.setDestinationFloor(next);
                Platform.runLater(() -> {
                    mElevatorsScene.moveElevatorToFloor(elevator.getID(), next);
                    System.out.println("Move elevator: " + elevator.toString());
                });
            }
        }
    }

    public ElevatorsSceneArgs getArgs() {
        return mArgs;
    }

    public void setArgs(ElevatorsSceneArgs mArgs) {
        this.mArgs = mArgs;
    }

    public List<Elevator> getElevators() {
        return mElevators;
    }

    public void setElevators(List<Elevator> mElevators) {
        this.mElevators = mElevators;
    }

    public Elevator findElevator(int id) {
        for (Elevator elevator : mElevators)
            if (elevator.getID() == id)
                return elevator;

        return null;
    }

    public List<Passenger> getPassengers() {
        return mPassengers;
    }

    public void setPassengers(List<Passenger> mPassengers) {
        this.mPassengers = mPassengers;
    }

    public Passenger findPassenger(int id) {
        for (Passenger passenger : mPassengers)
            if (passenger.getID() == id)
                return passenger;

        return null;
    }

    public IElevatorsScene getElevatorsScene() {
        return mElevatorsScene;
    }

    public void setElevatorsScene(IElevatorsScene mElevatorsScene) {
        this.mElevatorsScene = mElevatorsScene;
    }

    @Override
    public void onElevatorFloorChanged(int elevatorID, int newFloor) {
        Elevator elevator = findElevator(elevatorID);
        if (elevator != null) {
            elevator.setCurrentFloor(newFloor);
        }
    }

    @Override
    public void onElevatorDeparted(int elevatorID) {
        Elevator elevator = findElevator(elevatorID);
        if (elevator != null) {
            elevator.setState(ElevatorState.moving);
        }
    }

    @Override
    public void onElevatorArrived(int elevatorID) {
        Elevator elevator = findElevator(elevatorID);
        if (elevator != null) {
            elevator.setDepartureFloor(elevator.getCurrentFloor());
            elevator.setDestinationFloor(elevator.getCurrentFloor());
            elevator.setState(ElevatorState.waiting);

            force(elevator);
        }
    }

    @Override
    public void onPassengerSpawned(int passengerID) {

    }

    @Override
    public void onPassengerStartedMovingToElevator(int passengerID, int elevatorID) {
        Passenger passenger = findPassenger(passengerID);
        if (passenger != null) {
            passenger.setState(PassengerState.moving);
        }
    }

    @Override
    public void onPassengerEnteredElevator(int passengerID, int elevatorID) {

        Elevator elevator = findElevator(elevatorID);
        Passenger passenger = findPassenger(passengerID);
        if (passenger != null)
            passenger.setState(PassengerState.waiting);

        if (elevator != null) {
            elevator.removeFromWaitingPassengers(passenger);
            elevator.appendToMovingPassengers(passenger);

            force(elevator);
        }

    }

    @Override
    public void onPassengerStartedMovingFromElevator(int passengerID, int elevatorID) {
        Passenger passenger = findPassenger(passengerID);

        if (passenger != null)
            passenger.setState(PassengerState.moving);
    }

    @Override
    public void onPassengerExitedElevator(int passengerID, int elevatorID) {
        Elevator elevator = findElevator(elevatorID);
        Passenger passenger = findPassenger(passengerID);
        if (passenger != null && elevator != null) {
            elevator.removeFromMovingPassengers(passenger);
            passenger.setState(PassengerState.moving);
        }
    }

    @Override
    public void onPassengerDeleted(int passengerID) {
        mPassengers.removeIf(passenger -> passenger.getID() == passengerID);
    }
}

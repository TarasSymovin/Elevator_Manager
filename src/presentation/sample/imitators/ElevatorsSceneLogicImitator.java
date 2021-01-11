package presentation.sample.imitators;

import presentation.sample.enums.ElevatorState;
import presentation.sample.types.*;

import java.util.*;
import java.util.stream.Collectors;

public class ElevatorsSceneLogicImitator {
    private static int ElevatorsIdCounter = 0;
    private static int PassengersIdCounter = 0;

    private static ElevatorsSceneLogicImitator mInstance;

    public static ElevatorsSceneLogicImitator getInstance() {
        if (mInstance == null)
            return newInstance(6, 3);

        return mInstance;
    }

    public static ElevatorsSceneLogicImitator newInstance(int floorsCount, int elevatorsCount) {
        mInstance = new ElevatorsSceneLogicImitator(floorsCount, elevatorsCount);
        mInstance.initialize();

        return mInstance;
    }

    private int mFloorsCount;
    private int mElevatorsCount;

    List<Elevator> mElevators = new ArrayList<>();
    IElevatorsProgressListener mElevatorsProgressListener;

    List<Passenger> mPassengers = new ArrayList<>();
    IPassengerProgressListener mIPassengerProgressListener;

    HashMap<Integer, List<Integer>> mElevatorQueues = new HashMap<>();
    HashMap<Integer, List<Integer>> mPassrngerQueues = new HashMap<>();

    ElevatorsScene mElevatorScene;

    public ElevatorsScene getElevatorScene() {
        return mElevatorScene;
    }

    public void setElevatorScene(ElevatorsScene mElevatorScene) {
        this.mElevatorScene = mElevatorScene;
    }

    public void initialize() {
        for (int i = 0; i < mElevatorsCount; i++) {
            mElevators.add(new Elevator(ElevatorsIdCounter, 1));
            mElevatorQueues.put(ElevatorsIdCounter, new ArrayList<>());

            ElevatorsIdCounter++;
        }
    }

    public void generate() {
        RandomPassengersGenerationThread moveGenerationThread = new RandomPassengersGenerationThread(4000);
        moveGenerationThread.start();
    }

    private ElevatorsSceneLogicImitator(int floorsCount, int elevatorsCount) {
        this.mFloorsCount = floorsCount;
        this.mElevatorsCount = elevatorsCount;

        mElevatorsProgressListener = new IElevatorsProgressListener() {
            @Override
            public void onElevatorFloorChanged(int id, int newFloor) {
                Elevator elevator = findElevator(id);
                if (elevator != null)
                    elevator.setFloor(newFloor);

                System.out.println("Elevator with ID = " + id + " has reached floor #" + newFloor);
            }

            @Override
            public void onElevatorDeparted(int id) {
                Elevator elevator = findElevator(id);
                if (elevator != null) {
                    elevator.setState(ElevatorState.MOVING);
                    System.out.println("\n\nElevator with ID = " + id + " has DEPARTED from floor #" + elevator.getFloor());
                }
            }

            @Override
            public void onElevatorArrived(int id) {
                Elevator elevator = findElevator(id);
                if (elevator != null) {
                    elevator.setState(ElevatorState.WAITING);
                    System.out.println("Elevator with ID = " + id + " has ARRIVED to floor #" + elevator.getFloor());

                    List<Integer> queue = mElevatorQueues.get(elevator.getId());
                    if (elevator.getState() == ElevatorState.WAITING && mElevatorScene != null && queue != null && !queue.isEmpty()) {
                        mElevatorScene.moveElevatorToFloor(elevator.getId(), queue.get(0));
                        queue.remove(0);
                    }
                }
            }
        };

        mIPassengerProgressListener = new IPassengerProgressListener() {
            @Override
            public void onPassengerSpawned(int passengerID) {

            }

            @Override
            public void onPassengerEnteredElevator(int passengerID, int elevatorID) {

            }

            @Override
            public void onPassengerExitedElevator(int passengerID, int elevatorID) {

            }

            @Override
            public void onPassengerDeleted(int passengerID) {

            }
        };
    }

    public IElevatorsProgressListener getElevatorsProgressListener() {
        return mElevatorsProgressListener;
    }

    public IPassengerProgressListener getIPassengerProgressListener() {
        return mIPassengerProgressListener;
    }

    public int getFloorsCount() {
        return mFloorsCount;
    }

    public void setFloorsCount(int mFloorsCount) {
        this.mFloorsCount = mFloorsCount;
    }

    public int getElevatorsCount() {
        return mElevatorsCount;
    }

    public void setElevatorsCount(int mElevatorsCount) {
        this.mElevatorsCount = mElevatorsCount;
    }

    public List<Elevator> getElevators() {
        return mElevators;
    }

    public Elevator findElevator(int id) {
        for (Elevator elevator : mElevators) {
            if (elevator.getId() == id)
                return elevator;
        }

        return null;
    }

    public List<Passenger> getPassengers() {
        return mPassengers;
    }

    public Passenger findPassenger(int id) {
        for (Passenger passenger : mPassengers) {
            if (passenger.getId() == id)
                return passenger;
        }

        return null;
    }

    public class RandomElevatorFloorsQueueGenerationThread extends Thread {
        int mInterval;
        boolean mExit = false;

        public RandomElevatorFloorsQueueGenerationThread(int interval) {
            this.mInterval = interval;
        }

        @Override
        public void run() {
            Random random = new Random();

            while (!mExit) {
                try {
                    sleep(mInterval);

                    int index = random.nextInt(mElevators.size());
                    Elevator elevator = mElevators.get(index);

                    if (elevator != null) {
                        List<Integer> queue = mElevatorQueues.get(elevator.getId());
                        if (queue != null) {
                            int newFloor = random.nextInt(mFloorsCount) + 1;
                            queue.add(newFloor);

                            System.out.println("\tGenerated floor: " + String.valueOf(newFloor));
                            if (elevator.getState() == ElevatorState.WAITING && mElevatorScene != null)
                                mElevatorScene.moveElevatorToFloor(elevator.getId(), queue.get(0));

                            queue.remove(0);
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class RandomPassengersGenerationThread extends Thread {
        int mInterval;
        boolean mExit = false;

        public RandomPassengersGenerationThread(int interval) {
            this.mInterval = interval;
        }

        @Override
        public void run() {
            Random random = new Random();

            while (!mExit) {
                try {
                    sleep(mInterval);

                    int spawnFloor = random.nextInt(mFloorsCount) + 1;
                    int destinationFloor = random.nextInt(mFloorsCount) + 1;
                    if (spawnFloor == destinationFloor)
                        return;

                    System.out.println("\tGenerated floors for passenger: spawn=" + spawnFloor + ", destination=" + destinationFloor);
                    Passenger passenger = new Passenger(PassengersIdCounter++, spawnFloor);
                    mPassengers.add(passenger);
                    mPassrngerQueues.put(passenger.getId(), new ArrayList<>(destinationFloor));

                    mElevatorScene.spawnPassenger(passenger.getId(), spawnFloor);

                    List<Elevator> freeElevators = new ArrayList<>();
                    freeElevators = mElevators.stream().filter(elevator -> elevator.getState() == ElevatorState.WAITING).collect(Collectors.toList());

                    Elevator elevator = null;
                    if (freeElevators.size() > 0) {
                        int index = random.nextInt(freeElevators.size());
                        elevator = freeElevators.get(index);
                    } else {
                        int index = random.nextInt(mElevators.size());
                        elevator = mElevators.get(index);
                    }

                    if (elevator != null) {
                        List<Integer> queue = mElevatorQueues.get(elevator.getId());
                        if (queue != null) {
                            queue.add(destinationFloor);
                            if (elevator.getState() == ElevatorState.WAITING && mElevatorScene != null) {
                                mElevatorScene.moveElevatorToFloor(elevator.getId(), queue.get(0));
                                queue.remove(0);
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

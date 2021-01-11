package presentation.sample.imitators;

import presentation.sample.types.ElevatorsScene;
import presentation.sample.enums.ElevatorState;
import presentation.sample.types.Elevator;
import presentation.sample.types.IElevatorsProgressListener;

import java.util.*;

public class ElevatorsSceneLogicImitator {
    private static int IdCounter = 0;

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

    HashMap<Integer, List<Integer>> mElevatorQueues = new HashMap<>();

    ElevatorsScene mElevatorScene;

    public ElevatorsScene getElevatorScene() {
        return mElevatorScene;
    }

    public void setElevatorScene(ElevatorsScene mElevatorScene) {
        this.mElevatorScene = mElevatorScene;
    }

    public void initialize() {
        for (int i = 0; i < mElevatorsCount; i++) {
            mElevators.add(new Elevator(IdCounter, 1));
            mElevatorQueues.put(IdCounter, new ArrayList<>());

            IdCounter++;
        }
    }

    public void generate() {
        RandomMoveGenerationThread moveGenerationThread = new RandomMoveGenerationThread(4000);
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
    }

    public IElevatorsProgressListener getElevatorsProgressListener() {
        return mElevatorsProgressListener;
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


    public class RandomMoveGenerationThread extends Thread {
        int mInterval;
        boolean mExit = false;

        public RandomMoveGenerationThread(int interval) {
            this.mInterval = interval;
        }

        @Override
        public void run() {
            Random random = new Random();

            while (!mExit) {
                try {
                    sleep(mInterval);

                    int index = random.nextInt(mElevatorsCount);
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
}

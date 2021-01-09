package sample.imitators;

import sample.IElevatorsScene;
import sample.enums.ElevatorState;
import sample.types.Elevator;
import sample.types.IElevatorsProgressListener;
import sample.views.ElevatorView;

import java.util.*;
import java.util.logging.Logger;

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

    private boolean mIsOwnership;
    private int mFloorsCount;
    private int mElevatorsCount;

    List<Elevator> mElevators = new ArrayList<>();
    IElevatorsProgressListener mElevatorsProgressListener;

    HashMap<Integer, List<Integer>> mElevatorQueues = new HashMap<>();

    IElevatorsScene mElevatorScene;

    public IElevatorsScene getElevatorScene() {
        return mElevatorScene;
    }

    public void setElevatorScene(IElevatorsScene mElevatorScene) {
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
        RandomMoveGenerationThread moveGenerationThread = new RandomMoveGenerationThread(2000);
        moveGenerationThread.start();

//        Elevator elevator = findElevator(0);
//        List<Integer> queue = mElevatorQueues.get(elevator.getId());
//
//        if (queue != null) {
//            queue.add(3);
//            queue.add(1);
//            queue.add(5);
//            queue.add(2);
//
//            if (mElevatorScene != null)
//                mElevatorScene.moveToFloor(elevator.getId(), 2, mIsOwnership);
//        }
    }

    private ElevatorsSceneLogicImitator(int floorsCount, int elevatorsCount) {
        this.mIsOwnership = false;
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

                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    List<Integer> queue = mElevatorQueues.get(elevator.getId());
                                    if (elevator.getState() == ElevatorState.WAITING && mElevatorScene != null && queue != null && !queue.isEmpty()) {
                                        mElevatorScene.moveToFloor(elevator.getId(), queue.get(0), mIsOwnership);
                                        queue.remove(0);
                                    }
                                }
                            },
                            20
                    );


                }
            }
        };
    }

    public IElevatorsProgressListener getElevatorsProgressListener() {
        return mElevatorsProgressListener;
    }

    public boolean isOwnership() {
        return mIsOwnership;
    }

    public void setIsOwnership(boolean mIsOwnership) {
        this.mIsOwnership = mIsOwnership;
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
                            queue.add(random.nextInt(mFloorsCount) + 1);

                            if (elevator.getState() == ElevatorState.WAITING && mElevatorScene != null)
                                mElevatorScene.moveToFloor(elevator.getId(), queue.get(0), mIsOwnership);

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

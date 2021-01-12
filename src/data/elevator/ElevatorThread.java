package data.elevator;


import data.elevator.listener.ElevatorMovementListener;
import data.elevator.listener.EmptyElevatorMovementListener;
import data.elevator.strategy.ElevatorStrategy;
import data.logger.Logger;
import data.utils.observable.Observable;

import java.util.List;
import java.util.Set;

public class ElevatorThread extends Thread implements Elevator {

    private static final int LEAVE_DELAY = 2000;

    private final ElevatorControllable elevator;

    private ElevatorMovementListener listener = EmptyElevatorMovementListener.getInstance();

    private final Object accessLock = new Object();
    private final Object movementLock = new Object();

    private boolean isWaitingForTask = true;
    private boolean canMoveFurther = false;

    public ElevatorThread(ElevatorControllable elevator) {
        this.elevator = elevator;
    }

    @Override
    public void run() {
        Logger.getInstance().log("Started elevator thread " + getName() + " - " + elevator);

        synchronized (accessLock) {
            elevator.setOpened(true);
        }

        // TODO end condition
        while (true) {
            Logger.getInstance().log(elevator + " waiting to activate");

            waitToActivate();

            Logger.getInstance().log(elevator + " activated. Resolving floor index to go...");

            executeOrders();
        }
    }

    public void setListener(ElevatorMovementListener listener) {
        if (listener == null) this.listener = EmptyElevatorMovementListener.getInstance();
        else this.listener = listener;
    }

    @Override
    public int getElevatorId() {
        return elevator.getElevatorId();
    }

    @Override
    public int getCurrentFloor() {
        synchronized (accessLock) {
            return elevator.getCurrentFloor();
        }
    }

    @Override
    public Observable<Integer> getFloorObservable() {
        return elevator.getFloorObservable();
    }

    @Override
    public boolean isOpened() {
        synchronized (accessLock) {
            return elevator.isOpened();
        }
    }

    @Override
    public Set<Integer> getCalledFloors() {
        synchronized (accessLock) {
            return elevator.getCalledFloors();
        }
    }

    @Override
    public boolean callAtFloor(int floor) {
        synchronized (accessLock) {
            Logger.getInstance().log(elevator + " called at floor " + floor);
            if (elevator.callAtFloor(floor)) {
                if (isWaitingForTask) {
                    accessLock.notify();
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public List<ElevatorConsumer> getConsumers() {
        synchronized (accessLock) {
            return elevator.getConsumers();
        }
    }

    @Override
    public boolean enter(ElevatorConsumer consumer) {
        synchronized (accessLock) {
            if (elevator.enter(consumer)) {
                resetMovementDelay();
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean leave(ElevatorConsumer consumer) {
        synchronized (accessLock) {
            if (elevator.leave(consumer)) {
                resetMovementDelay();
                return true;
            }
            return false;
        }
    }

    @Override
    public Observable<List<ElevatorConsumer>> getConsumersObservable() {
        return elevator.getConsumersObservable();
    }

    @Override
    public boolean isMoving() {
        synchronized (accessLock) {
            return elevator.isMoving();
        }
    }

    @Override
    public void setIsMoving(boolean isMoving) {
        synchronized (accessLock) {
            if (isMoving) {
                elevator.setIsMoving(false);
                synchronized (movementLock) {
                    movementLock.notify();
                }
            }
        }
    }

    private void waitToActivate() {
        synchronized (accessLock) {
            while (!elevator.getMovementStrategy().hasWhereToGo(elevator)) {
                isWaitingForTask = true;
                try {
                    accessLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isWaitingForTask = false;
        }
    }

    private void executeOrders() {
        ElevatorStrategy strategy = elevator.getMovementStrategy();

        boolean hasWhereToGo;
        int floor = -10;
        synchronized (accessLock) {
            hasWhereToGo = strategy.hasWhereToGo(elevator);
            if (hasWhereToGo) {
                floor = strategy.resolveFloorToGo(elevator);
            }
        }

        while (hasWhereToGo) {
            Logger.getInstance().log(elevator + " goes to floor " + floor);

            synchronized (accessLock) {
                listener.onElevatorMovingToFloor(this, floor);
                goToFloor(floor);
            }

            waitBeforeMoveFurther();

            synchronized (accessLock) {
                hasWhereToGo = strategy.hasWhereToGo(elevator);
                if (hasWhereToGo) {
                    floor = strategy.resolveFloorToGo(elevator);
                }
            }
        }
    }

    private void goToFloor(int floor) {
        elevator.setOpened(false);
        elevator.setIsMoving(true);

        Logger.getInstance().log(elevator + " closed the doors at floor " + getCurrentFloor());

        Logger.getInstance().log(elevator + " performs movement to floor " + floor);
        moveToNextFloor();
        Logger.getInstance().log(elevator + " reached floor " + floor);

        elevator.setOpened(true);
        elevator.removeCalledFloor(floor);

        canMoveFurther = false;

        getConsumers().forEach(consumer -> consumer.onElevatorDockedToFloor(this, floor));

        getFloorObservable().notifyObservers(floor);

        Logger.getInstance().log(elevator + " opened doors at floor " + floor);
    }

//    private void performMovement(int floor) {
//        int distance = floor - elevator.getCurrentFloor();
//        boolean increment = distance < 0;
//        while (distance != 0) {
//            moveToNextFloor();
//            if (increment) distance++;
//            else distance--;
//            elevator.setCurrentFloor(floor - distance);
//        }
//    }

    private void moveToNextFloor() {
        synchronized (movementLock) {
            try {
                movementLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitBeforeMoveFurther() {
        synchronized (movementLock) {
            while (!canMoveFurther) {
                canMoveFurther = true;
                try {
                    movementLock.wait(LEAVE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resetMovementDelay() {
        synchronized (movementLock) {
            canMoveFurther = false;
            movementLock.notify();
        }
    }

}

package data.floor;

import data.building.BuildingFloor;

import java.util.*;

public class FloorImpl implements BuildingFloor {

    private final List<Queue<QueueConsumer>> queues;

    public FloorImpl(int elevatorsCount) {
        List<Queue<QueueConsumer>> queues = new ArrayList<Queue<QueueConsumer>>() {{
            for (int i = 0; i < elevatorsCount; i++) {
                add(i, new ArrayDeque<>());
            }
        }};
        this.queues = Collections.unmodifiableList(queues);
    }

    @Override
    public int getQueueSizeAtElevator(int elevator) {
        Queue<QueueConsumer> queue = queues.get(elevator);
        synchronized (queue) {
            return queue.size();
        }
    }

    @Override
    public synchronized boolean enterQueue(QueueConsumer person) {
        Queue<QueueConsumer> queue = queues.get(person.elevatorIndex());
        synchronized (queue) {
            boolean callElevator = queue.size() == 0;
            queue.add(person);
            return callElevator;
        }
    }

    @Override
    public synchronized void leaveQueue(QueueConsumer person) {
        Queue<QueueConsumer> queue = queues.get(person.elevatorIndex());
        synchronized (queue) {
            if (queue.peek() != person) {
                throw new IllegalStateException("Person " + person + " tried to enter elevator " + person.elevatorIndex() +
                        " without entering it's queue"
                );
            }
            queue.poll();
        }
    }

    @Override
    public QueueConsumer getQueueHead(int elevator) {
        Queue<QueueConsumer> queue = queues.get(elevator);
        synchronized (queue) {
            return queue.peek();
        }
    }
}

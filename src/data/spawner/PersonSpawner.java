package data.spawner;

import data.logger.Logger;
import data.person.Person;
import data.person.PersonImpl;
import data.utils.Action;

import java.util.Random;

public class PersonSpawner {

    private static final int DELAY = 10000;

    private final Action<Person> callback;

    private final Thread spawnerThread = new Thread(this::infiniteSpawning, "SpawnerThread");
    private final Object activeLock = new Object();

    private boolean isActive = false;

    private int counter = 1;

    public PersonSpawner(Action<Person> callback) {
        this.callback = callback;
    }

    public void startSpawn() {
        isActive = true;
        if (spawnerThread.isAlive()) return;
        spawnerThread.start();

        Logger.getInstance().logTitle("Requested person spawner to start");
    }

    public void stopSpawn() {
        isActive = false;
        activeLock.notify();

        Logger.getInstance().logTitle("Requested person spawner to stop");
    }

    private void infiniteSpawning() {
        Logger.getInstance().logTitle("Person spawner started successfully");
        synchronized (activeLock) {
            while (isActive) {
                try {
                    activeLock.wait(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executeSpawn();
            }
        }
        Logger.getInstance().logTitle("Person spawner stopped successfully");
    }

    private void executeSpawn() {
        Logger.getInstance().log("Trying to spawn a new person...");
        Person person = new PersonImpl(
                String.valueOf(counter),
                new Random().nextInt(20) + 60f
        );
        counter++;

        Logger.getInstance().log(person + " spawned");

        callback.call(person);
    }
}

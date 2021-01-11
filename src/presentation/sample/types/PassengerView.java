package presentation.sample.types;

import javafx.animation.SequentialTransition;
import javafx.scene.shape.Rectangle;

public class PassengerView {
    public static final int WIDTH = 32;
    public static final int HEIGHT = 75;

    private int passengerID;
    private Rectangle rectangle;
    private SequentialTransition transition = new SequentialTransition();

    public PassengerView(int passengerID, Rectangle rectangle) {
        this.passengerID = passengerID;
        this.rectangle = rectangle;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public SequentialTransition getTransition() {
        return transition;
    }

    public void setTransition(SequentialTransition transition) {
        this.transition = transition;
    }
}

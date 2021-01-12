package presentation.sample.types;

import javafx.animation.SequentialTransition;
import javafx.scene.shape.Rectangle;

public class PersonView {
    public static final int WIDTH = 32;
    public static final int HEIGHT = 75;

    private int personID;
    private Rectangle rectangle;
    private SequentialTransition transition = new SequentialTransition();

    public PersonView(int personID, Rectangle rectangle) {
        this.personID = personID;
        this.rectangle = rectangle;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
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

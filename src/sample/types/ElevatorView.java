package sample.types;

import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import sample.types.Elevator;
import sample.types.Step;

import java.util.ArrayList;
import java.util.List;

public class ElevatorView {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 75;

    private int elevatorID;
    private Rectangle rectangle;

    private SequentialTransition transition;
    private Timeline timeline;

    private List<Step> steps = new ArrayList<>();

    public ElevatorView() {

    }

    public ElevatorView(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public void setElevatorID(int elevatorID) {
        this.elevatorID = elevatorID;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void addStep(Step step) {
        this.steps.add(step);
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

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }
}

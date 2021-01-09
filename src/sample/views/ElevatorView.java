package sample.views;

import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import sample.types.Elevator;
import sample.types.Step;

import java.util.ArrayList;
import java.util.List;

public class ElevatorView {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 75;

    private Rectangle rectangle;
    private Elevator elevator;
    private Path path;

    private List<Step> steps = new ArrayList<>();

    public ElevatorView() {
        path = new Path();
    }

    public ElevatorView(Rectangle rectangle, Elevator elevator) {
        this.rectangle = rectangle;
        this.elevator = elevator;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
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

    public Elevator getElevator() {
        return elevator;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }


}

package sample.views;

import com.sun.javafx.geom.Rectangle;
import javafx.scene.shape.Path;
import sample.types.Elevator;

public class ElevatorView {
    private Rectangle rectangle;
    private Elevator elevator;
    private Path path;

    public ElevatorView(Rectangle rectangle, Elevator elevator) {
        this.rectangle = rectangle;
        this.elevator = elevator;
    }

    public ElevatorView() {
        path = new Path();
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
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

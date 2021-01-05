package sample;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane pane = new AnchorPane();

    private List<ElevatorView> elevators = new ArrayList<>();
    private int floorCount = 3;
    private int elevatorCount = 3;

    @FXML
    void initialize() {
        initializeElevators();

        goToFloor(3, elevators.get(2));
        goToFloor(1, elevators.get(2));

        goToFloor(3, elevators.get(1));
        goToFloor(1, elevators.get(1));
    }

    public void initializeElevators(){
        Group group = new Group();
        pane.getChildren().add(group);

        for (int i = 0; i < elevatorCount; i++){
            Rectangle rectangle = new Rectangle();

            rectangle.setWidth(100);
            rectangle.setHeight(200);
            rectangle.setArcHeight(10);
            rectangle.setArcWidth(10);
            rectangle.setY((floorCount - 1) * rectangle.getHeight());
            rectangle.setX(i * rectangle.getWidth() + i * 25);

            ElevatorView elevatorView = new ElevatorView();
            elevatorView.setRectangle(rectangle);
            elevators.add(elevatorView);
        }

        elevators.forEach(x -> group.getChildren().add(x.getRectangle()));

        pane.setStyle("-fx-background-color: #94faf0");
        pane.setPrefHeight(floorCount * 200);
        pane.setPrefWidth(elevatorCount * 100 + 25 * elevatorCount);
    }

    public void goToFloor(int floor, ElevatorView elevator){

        elevator.getPath().getElements().add(new MoveTo(elevator.getRectangle().getX() + elevators.size() * 25 - 25, elevator.getRectangle().getY() + elevator.getRectangle().getHeight() / 2));
        elevator.getPath().getElements().add(new LineTo(elevator.getRectangle().getX() + elevators.size() * 25 - 25, ((floorCount - floor) * elevator.getRectangle().getHeight()) + elevator.getRectangle().getHeight() / 2));

        elevator.getRectangle().setX(elevator.getRectangle().getX());
        elevator.getRectangle().setY((floorCount - floor) * elevator.getRectangle().getHeight());

        PathTransition pathTransition = new PathTransition();

        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setNode(elevator.getRectangle());
        pathTransition.setPath(elevator.getPath());
        pathTransition.play();
    }
}

class ElevatorView{
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

class Elevator{

}
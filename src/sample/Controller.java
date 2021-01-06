package sample;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sample.types.Elevator;
import sample.types.Step;
import sample.views.ElevatorView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {
    public static final int ELEVATOR_FIRST_FLOOR_MOVE_DURATION = 3000;
    public static final int ELEVATOR_LAST_FLOOR_MOVE_DURATION = 2250;
    public static final int ELEVATOR_OTHER_FLOOR_MOVE_DURATION = 1500;

    public static final int ELEVATOR_LEFT_MARGIN = 80;


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

//        planElevatorMove(3, elevators.get(0));
//        planElevatorMove(2, elevators.get(1));
        planElevatorMove(3, elevators.get(2));
        planElevatorMove(1, elevators.get(2));
        planElevatorMove(2, elevators.get(2));

        executeElevatorMove(elevators.get(2), true);

//        goToFloor(3, elevators.get(2));
//        goToFloor(1, elevators.get(2));
//
//        goToFloor(3, elevators.get(1));
//        goToFloor(1, elevators.get(1));
    }

    public void initializeElevators() {
        Group group = new Group();
        pane.getChildren().add(group);

        for (int i = 0; i < elevatorCount; i++) {
            Rectangle rectangle = new Rectangle();

            // setting elevator width and height
            rectangle.setWidth(ElevatorView.WIDTH);
            rectangle.setHeight(ElevatorView.HEIGHT);

            // setting corner radius
            rectangle.setArcHeight(10);
            rectangle.setArcWidth(10);

            // calculating elevator coordinates with margins
            double x = i * rectangle.getWidth() + (i + 1) * ELEVATOR_LEFT_MARGIN;
            double y = (floorCount - 1) * rectangle.getHeight();

            rectangle.setX(x);
            rectangle.setY(y);

            ElevatorView elevatorView = new ElevatorView();
            elevatorView.setElevator(new Elevator(1));
            elevatorView.setRectangle(rectangle);
            elevators.add(elevatorView);
        }

        elevators.forEach(x -> group.getChildren().add(x.getRectangle()));

        pane.setStyle("-fx-background-color: #94faf0");
        pane.setPrefHeight(floorCount * ElevatorView.HEIGHT);
        pane.setPrefWidth(elevatorCount * ElevatorView.WIDTH + ELEVATOR_LEFT_MARGIN * elevatorCount);
    }

    public void goToFloor(int floor, ElevatorView elevator) {
        elevator.getPath().getElements().add(new MoveTo(elevator.getRectangle().getX() + elevators.size() * ELEVATOR_LEFT_MARGIN - ELEVATOR_LEFT_MARGIN, elevator.getRectangle().getY() + elevator.getRectangle().getHeight() / 2));
        elevator.getPath().getElements().add(new LineTo(elevator.getRectangle().getX() + elevators.size() * ELEVATOR_LEFT_MARGIN - ELEVATOR_LEFT_MARGIN, ((floorCount - floor) * elevator.getRectangle().getHeight()) + elevator.getRectangle().getHeight() / 2));

        elevator.getRectangle().setX(elevator.getRectangle().getX());
        elevator.getRectangle().setY((floorCount - floor) * elevator.getRectangle().getHeight());

        PathTransition pathTransition = new PathTransition();

        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setNode(elevator.getRectangle());
        pathTransition.setPath(elevator.getPath());
        pathTransition.play();
    }

    public void moveElevator(int newFloor, ElevatorView elevator) {
        elevator.getPath().getElements().add(new MoveTo(elevator.getRectangle().getX() + elevators.size() * ELEVATOR_LEFT_MARGIN - ELEVATOR_LEFT_MARGIN, elevator.getRectangle().getY() + elevator.getRectangle().getHeight() / 2));
        elevator.getPath().getElements().add(new LineTo(elevator.getRectangle().getX() + elevators.size() * ELEVATOR_LEFT_MARGIN - ELEVATOR_LEFT_MARGIN, ((floorCount - newFloor) * elevator.getRectangle().getHeight()) + elevator.getRectangle().getHeight() / 2));

        elevator.getRectangle().setX(elevator.getRectangle().getX());
        elevator.getRectangle().setY((floorCount - newFloor) * elevator.getRectangle().getHeight());

        PathTransition pathTransition = new PathTransition();

        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setNode(elevator.getRectangle());
        pathTransition.setPath(elevator.getPath());
        pathTransition.play();
    }

    public void planElevatorMove(int newFloor, ElevatorView elevatorView) {
        int oldFloor = 1;
        if (elevatorView.getSteps().size() > 0) {
            Step step = elevatorView.getSteps().get(elevatorView.getSteps().size() - 1);
            double x = step.getEnd().getX() - elevatorView.getRectangle().getWidth() / 2;
            double y = step.getEnd().getY() - elevatorView.getRectangle().getHeight() / 2;

            oldFloor = step.getFloor();
        } else {
            double x = elevatorView.getRectangle().getX();
            double y = elevatorView.getRectangle().getY();

            oldFloor = elevatorView.getElevator().getFloor();
        }

        // TODO: split on strategies
        // this is a strategy of lift ownership
        int difference = Math.abs(oldFloor - newFloor);
        if (difference > 0) {

            int increment = 1;
            if (oldFloor > newFloor)
                increment = -1;


            List<Step> steps = new ArrayList<>();
            double x = 0, y = 0;
            for (int i = 0; i < difference; i++) {
                Step step = new Step();
                step.setFloor(oldFloor + (i + 1) * increment);

                if (i == 0)
                    step.setDuration(ELEVATOR_FIRST_FLOOR_MOVE_DURATION);
                else if (i == difference - 1)
                    step.setDuration(ELEVATOR_LAST_FLOOR_MOVE_DURATION);
                else
                    step.setDuration(ELEVATOR_OTHER_FLOOR_MOVE_DURATION);

                // calculating step
                if (i == 0) {
                    if (elevatorView.getSteps().size() > 0) {
                        Step last = elevatorView.getSteps().get(elevatorView.getSteps().size() - 1);
                        x = last.getEnd().getX();
                        y = last.getEnd().getY();
                    } else {
                        x = elevatorView.getRectangle().getX() + elevatorView.getRectangle().getWidth() / 2;
                        y = elevatorView.getRectangle().getY() + elevatorView.getRectangle().getHeight() / 2;
                    }
                }
                step.setBeg(new Point2D(x, y));

                // moving one floor at a time
                y -= increment * elevatorView.getRectangle().getHeight();
                step.setEnd(new Point2D(x, y));

                steps.add(step);
            }
            elevatorView.getSteps().addAll(steps);
        }

    }

    public boolean executeElevatorMove(ElevatorView elevatorView, boolean isFirst) {
        if (elevatorView.getSteps().isEmpty())
            return false;

        Path path = new Path();
        if (isFirst) {
            path.getElements().add(new MoveTo(elevatorView.getSteps().get(0).getBeg().getX(), elevatorView.getSteps().get(0).getBeg().getY()));
            path.getElements().add(new LineTo(elevatorView.getSteps().get(0).getEnd().getX(), elevatorView.getSteps().get(0).getEnd().getY()));
        } else {
            path.getElements().add(new MoveTo(elevatorView.getSteps().get(0).getBeg().getX(), elevatorView.getSteps().get(0).getBeg().getY()));
            path.getElements().add(new LineTo(elevatorView.getSteps().get(0).getEnd().getX(), elevatorView.getSteps().get(0).getEnd().getY()));
        }

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(elevatorView.getSteps().get(0).getDuration()));
        pathTransition.setPath(path);
        pathTransition.setNode(elevatorView.getRectangle());


        pathTransition.setOnFinished(actionEvent -> {
            if (elevatorView.getSteps().size() == 0)
                return;

            int increment = 1;
            if (elevatorView.getSteps().get(0).getEnd().getY() > elevatorView.getSteps().get(0).getEnd().getY())
                increment = -1;
            elevatorView.getElevator().setFloor(elevatorView.getElevator().getFloor() + increment);

            // removing executing step due to be able to make next one
            elevatorView.getSteps().remove(0);

            executeElevatorMove(elevatorView, false);
        });
        pathTransition.play();

        return true;
    }
}


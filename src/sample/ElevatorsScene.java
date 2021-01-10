package sample;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.enums.ElevatorState;
import sample.imitators.ElevatorsSceneLogicImitator;
import sample.types.Elevator;
import sample.types.IElevatorsProgressListener;
import sample.types.Step;
import sample.views.ElevatorView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ElevatorsScene {
    public static final int ELEVATOR_FIRST_FLOOR_MOVE_DURATION = 1200;
    public static final int ELEVATOR_LAST_FLOOR_MOVE_DURATION = 800;
    public static final int ELEVATOR_OTHER_FLOOR_MOVE_DURATION = 800;
//    public static final int ELEVATOR_LAST_FLOOR_MOVE_DURATION = 1800;
//    public static final int ELEVATOR_OTHER_FLOOR_MOVE_DURATION = 1500;

    public static final int ELEVATOR_LEFT_MARGIN = 30;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button start_button;

    @FXML
    private AnchorPane pane = new AnchorPane();
    private Group elements = new Group();

    private final Image elevatorImage = new Image("sample/images/elevator_image.png");
    private final Image backgroundFloorImage = new Image("sample/images/floor_background.jpg");
    private final Image backgroundImage = new Image("sample/images/wall_image.jpg");
    private final BackgroundImage background = new BackgroundImage(backgroundImage, null, null, null, null);

    private List<ElevatorView> elevatorViews = new ArrayList<>();
    private List<Rectangle> floors = new ArrayList<>();
    private int floorCount = 2;
    private int elevatorCount = 2;

    // Data from the main window
    private int numberOfElevators = 3;
    private int numberOfFloors = 3;
    private int numberOfPeople = 10;
    private int strategy = 2;

    IElevatorsScene iElevatorsScene;
    ElevatorsSceneLogicImitator sceneLogicImitator;

    @FXML
    void initialize() {

        start_button.setOnAction(actionEvent -> {
            start();

            start_button.setVisible(false);
        });

    }

    public void start(){
        iElevatorsScene = new IElevatorsScene() {
            @Override
            public void moveToFloor(int elevatorID, int newFloor, boolean isOwnership) {
                ElevatorView elevatorView = null;
                for (ElevatorView ev : elevatorViews) {
                    if (ev.getElevatorID() == elevatorID) {
                        elevatorView = ev;
                        break;
                    }
                }
                if (elevatorView != null) {
                    planElevatorMove(newFloor, elevatorView);
                    moveElevator(elevatorView, isOwnership);
                }
            }
        };

        // setting up logic imitator
        sceneLogicImitator = ElevatorsSceneLogicImitator.newInstance(floorCount, elevatorCount);
        sceneLogicImitator.setElevatorScene(iElevatorsScene);
        sceneLogicImitator.setIsOwnership(true);

        floorCount = sceneLogicImitator.getFloorsCount();
        elevatorCount = sceneLogicImitator.getElevatorsCount();

        initializeElevators();
        initializeFloors();

        pane.getChildren().add(elements);
        pane.setBackground(new Background(background));
        pane.setPrefHeight(floorCount * ElevatorView.HEIGHT);
        pane.setPrefWidth(elevatorCount * ElevatorView.WIDTH + ELEVATOR_LEFT_MARGIN * elevatorCount + ElevatorView.HEIGHT);
        pane.getChildren().addAll(initializeFloorNumber());

//         starting logic generator
        sceneLogicImitator.generate();
    }

    public List<Label> initializeFloorNumber() {
        List<Label> floorNumbers = new ArrayList<>();

        for (int i = 0; i < floorCount; i++) {
            Label label = new Label(String.valueOf(floorCount - i));
            label.setStyle("-fx-font-size: 52");
            label.setMaxWidth(ElevatorView.HEIGHT);
            if ((floorCount - i) < 10)
                label.setLayoutX(20);
            else
                label.setLayoutX(5);

            label.setLayoutY(i * ElevatorView.HEIGHT);

            floorNumbers.add(label);
        }

        return floorNumbers;
    }

    public void initializeFloors() {
        for (int i = 0; i < floorCount; i++) {
            Rectangle rectangle = new Rectangle();

            rectangle.setWidth(ElevatorView.HEIGHT);
            rectangle.setHeight(ElevatorView.HEIGHT);

            double x = 0;
            double y = i * ElevatorView.HEIGHT;

            rectangle.setX(x);
            rectangle.setY(y);

            rectangle.setFill(new ImagePattern(backgroundFloorImage));

            floors.add(rectangle);
        }

        floors.forEach(x -> elements.getChildren().add(x));
    }

    public void initializeElevators() {
        for (int i = 0; sceneLogicImitator.getElevators() != null && i < sceneLogicImitator.getElevators().size(); i++) {
            Rectangle rectangle = new Rectangle();

            // setting elevator width and height
            rectangle.setWidth(ElevatorView.WIDTH);
            rectangle.setHeight(ElevatorView.HEIGHT);

            // setting corner radius
            rectangle.setArcHeight(10);
            rectangle.setArcWidth(10);

            // calculating elevator coordinates with margins
            double x = i * rectangle.getWidth() + (i + 1) * ELEVATOR_LEFT_MARGIN + ElevatorView.HEIGHT;
            double y = (floorCount - 1) * rectangle.getHeight();

            rectangle.setX(x);
            rectangle.setY(y);

            rectangle.setFill(new ImagePattern(elevatorImage));

            ElevatorView elevatorView = new ElevatorView();
            elevatorView.setElevatorID(sceneLogicImitator.getElevators().get(i).getId());
            elevatorView.setRectangle(rectangle);
            elevatorViews.add(elevatorView);
        }

        elevatorViews.forEach(x -> elements.getChildren().add(x.getRectangle()));
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

            Elevator elevator = sceneLogicImitator.findElevator(elevatorView.getElevatorID());
            if (elevator != null)
                oldFloor = elevator.getFloor();
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
                else if (i == difference - 1) {
                    step.setDuration(ELEVATOR_LAST_FLOOR_MOVE_DURATION);
                    step.setIsDestination(true);
                } else
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

    public void moveElevator(ElevatorView elevatorView, boolean isOwnership) {
        if (isOwnership) {
            if (elevatorView.getSteps().isEmpty())
                return;

            Path path = new Path();
            int duration = 0;
            Step first = elevatorView.getSteps().get(0);
            Step last = elevatorView.getSteps().get(0);

            for (int i = 0; i < elevatorView.getSteps().size(); i++) {
                last = elevatorView.getSteps().get(i);
                duration += last.getDuration();

                if (last.isDestination())
                    break;
            }

            path.getElements().add(new MoveTo(first.getBeg().getX(), first.getBeg().getY()));
            path.getElements().add(new LineTo(last.getEnd().getX(), last.getEnd().getY()));

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(duration));
            pathTransition.setPath(path);
            pathTransition.setNode(elevatorView.getRectangle());

            pathTransition.setOnFinished(actionEvent -> {
                Step step = elevatorView.getSteps().get(0);

                int index = elevatorView.getSteps().size();

                while (index > 0) {
                    step = elevatorView.getSteps().get(0);
                    elevatorView.getSteps().remove(0);
                    if (step.isDestination())
                        break;

                    index--;
                }

                IElevatorsProgressListener progressListener = sceneLogicImitator.getElevatorsProgressListener();
                if (progressListener != null) {
                    progressListener.onElevatorFloorChanged(elevatorView.getElevatorID(), step.getFloor());

                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    progressListener.onElevatorArrived(elevatorView.getElevatorID());
                                }
                            },
                            20
                    );
                }
            });
            pathTransition.play();

            // setting rectangle coordinates to match the transition path end coordinates
            elevatorView.getRectangle().setX(last.getEnd().getX() - elevatorView.getRectangle().getWidth() / 2);
            elevatorView.getRectangle().setY(last.getEnd().getY() - elevatorView.getRectangle().getHeight() / 2);

            Elevator elevator = sceneLogicImitator.findElevator(elevatorView.getElevatorID());
            if (elevator != null && elevator.getState() == ElevatorState.WAITING) {
                IElevatorsProgressListener progressListener = sceneLogicImitator.getElevatorsProgressListener();
                if (progressListener != null)
                    progressListener.onElevatorDeparted(elevatorView.getElevatorID());
            }

        } else {
            if (elevatorView.getSteps().isEmpty())
                return;

            Path path = new Path();
            int duration = 0;
            Step first = elevatorView.getSteps().get(0);
            duration += first.getDuration();

            path.getElements().add(new MoveTo(first.getBeg().getX(), first.getBeg().getY()));
            path.getElements().add(new LineTo(first.getEnd().getX(), first.getEnd().getY()));


            int stepsToDestination = 0;
            for (int i = 0; i < elevatorView.getSteps().size(); i++) {
                stepsToDestination++;
                if (elevatorView.getSteps().get(i).isDestination())
                    break;
            }

            if (stepsToDestination > 1) {
            } else {
                //  setting rectangle coordinates to match the transition path end coordinates
                elevatorView.getRectangle().setX(first.getEnd().getX() - elevatorView.getRectangle().getWidth() / 2);
                elevatorView.getRectangle().setY(first.getEnd().getY() - elevatorView.getRectangle().getHeight() / 2);
            }

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(duration));
            pathTransition.setPath(path);
            pathTransition.setNode(elevatorView.getRectangle());

            pathTransition.setOnFinished(actionEvent -> {
                Step step = elevatorView.getSteps().get(0);
                IElevatorsProgressListener progressListener = sceneLogicImitator.getElevatorsProgressListener();
                if (progressListener != null)
                    progressListener.onElevatorFloorChanged(elevatorView.getElevatorID(), step.getFloor());

                elevatorView.getSteps().remove(0);

                if (!step.isDestination() && elevatorView.getSteps().size() > 0) {
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    moveElevator(elevatorView, false);
                                }
                            },
                            20
                    );
                }
                else if (progressListener != null)
                    progressListener.onElevatorArrived(elevatorView.getElevatorID());
            });
            pathTransition.play();

            Elevator elevator = sceneLogicImitator.findElevator(elevatorView.getElevatorID());
            if (elevator != null && elevator.getState() == ElevatorState.WAITING) {
                IElevatorsProgressListener progressListener = sceneLogicImitator.getElevatorsProgressListener();
                if (progressListener != null)
                    progressListener.onElevatorDeparted(elevatorView.getElevatorID());
            }
        }
    }

    public void saveParams(int numberOfElevators, int numberOfFloors, int numberOfPeople, int strategy) {
        this.elevatorCount = numberOfElevators;
        this.floorCount = numberOfFloors;
        this.numberOfPeople = numberOfPeople;
        this.strategy = strategy;
    }
}


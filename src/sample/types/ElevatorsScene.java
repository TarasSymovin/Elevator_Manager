package sample.types;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sample.enums.ElevatorState;
import sample.imitators.ElevatorsSceneLogicImitator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ElevatorsScene implements IElevatorsScene {
    public static final int ELEVATOR_ONE_FLOOR_MOVE_DURATION = 1000;
    public static final int ELEVATOR_LEFT_MARGIN = 30;

    @FXML
    private ResourceBundle mResources;

    @FXML
    private URL mLocation;

    @FXML
    private Button mStartButton;

    @FXML
    private AnchorPane mPane = new AnchorPane();
    private Group mElements = new Group();

    private final Image mElevatorImage = new Image("sample/images/elevator_image.png");
    private final Image mBackgroundFloorImage = new Image("sample/images/floor_background.jpg");
    private final Image mBackgroundImage = new Image("sample/images/wall_image.jpg");
    private final BackgroundImage mBackground = new BackgroundImage(mBackgroundImage, null, null, null, null);

    private List<ElevatorView> mElevatorViews = new ArrayList<>();
    private List<Rectangle> mFloors = new ArrayList<>();
    private int mFloorCount = 10;
    private int mElevatorCount = 1;

    // Data from the main window
    private int mNumberOfElevators = 3;
    private int mNumberOfFloors = 3;
    private int mNumberOfPeople = 10;
    private int mStrategy = 2;

    ElevatorsSceneLogicImitator mSceneLogicImitator;

    @FXML
    void initialize() {

        mStartButton.setOnAction(actionEvent -> {
            start();

            mStartButton.setVisible(false);
        });
    }

    public void start() {
        // setting up logic imitator
        mSceneLogicImitator = ElevatorsSceneLogicImitator.newInstance(mFloorCount, mElevatorCount);
        mSceneLogicImitator.setElevatorScene(this);

        mFloorCount = mSceneLogicImitator.getFloorsCount();
        mElevatorCount = mSceneLogicImitator.getElevatorsCount();

        initializeElevators();
        initializeFloors();

        mPane.getChildren().add(mElements);
        mPane.setBackground(new Background(mBackground));
        mPane.setPrefHeight(mFloorCount * ElevatorView.HEIGHT);
        mPane.setPrefWidth(mElevatorCount * ElevatorView.WIDTH + ELEVATOR_LEFT_MARGIN * mElevatorCount + ElevatorView.HEIGHT);
        mPane.getChildren().addAll(initializeFloorNumber());

        // starting logic generator
        mSceneLogicImitator.generate();
    }

    public List<Label> initializeFloorNumber() {
        List<Label> floorNumbers = new ArrayList<>();

        for (int i = 0; i < mFloorCount; i++) {
            Label label = new Label(String.valueOf(mFloorCount - i));
            label.setStyle("-fx-font-size: 52");
            label.setMaxWidth(ElevatorView.HEIGHT);
            if ((mFloorCount - i) < 10)
                label.setLayoutX(20);
            else
                label.setLayoutX(5);

            label.setLayoutY(i * ElevatorView.HEIGHT);

            floorNumbers.add(label);
        }

        return floorNumbers;
    }

    public void initializeFloors() {
        for (int i = 0; i < mFloorCount; i++) {
            Rectangle rectangle = new Rectangle();

            rectangle.setWidth(ElevatorView.HEIGHT);
            rectangle.setHeight(ElevatorView.HEIGHT);

            double x = 0;
            double y = i * ElevatorView.HEIGHT;

            rectangle.setX(x);
            rectangle.setY(y);

            rectangle.setFill(new ImagePattern(mBackgroundFloorImage));

            mFloors.add(rectangle);
        }

        mFloors.forEach(x -> mElements.getChildren().add(x));
    }

    public void initializeElevators() {
        for (int i = 0; mSceneLogicImitator.getElevators() != null && i < mSceneLogicImitator.getElevators().size(); i++) {
            Rectangle rectangle = new Rectangle();

            // setting elevator width and height
            rectangle.setWidth(ElevatorView.WIDTH);
            rectangle.setHeight(ElevatorView.HEIGHT);

            // setting corner radius
            rectangle.setArcHeight(10);
            rectangle.setArcWidth(10);

            // calculating elevator coordinates with margins
            double x = i * rectangle.getWidth() + (i + 1) * ELEVATOR_LEFT_MARGIN + ElevatorView.HEIGHT;
            double y = (mFloorCount - 1) * rectangle.getHeight();

            rectangle.setX(x);
            rectangle.setY(y);

            rectangle.setFill(new ImagePattern(mElevatorImage));

            ElevatorView elevatorView = new ElevatorView();
            elevatorView.setElevatorID(mSceneLogicImitator.getElevators().get(i).getId());
            elevatorView.setRectangle(rectangle);
            mElevatorViews.add(elevatorView);
        }

        mElevatorViews.forEach(x -> mElements.getChildren().add(x.getRectangle()));
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

            Elevator elevator = mSceneLogicImitator.findElevator(elevatorView.getElevatorID());
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

                step.setDuration(ELEVATOR_ONE_FLOOR_MOVE_DURATION);

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

                if (step.getFloor() == newFloor)
                    step.setIsDestination(true);

                steps.add(step);
            }
            elevatorView.getSteps().addAll(steps);
        }
    }

    public void moveElevatorStepByStep(int newFloor, ElevatorView elevatorView) {
        Elevator elevator = mSceneLogicImitator.findElevator(elevatorView.getElevatorID());
        if (elevator != null && elevator.getState() != ElevatorState.WAITING && elevator.getFloor() != newFloor) {
            return;
        }

        planElevatorMove(newFloor, elevatorView);
        if (elevatorView.getSteps().isEmpty())
            return;

        if (elevatorView.getTransition() != null)
            elevatorView.getTransition().stop();
        else
            elevatorView.setTransition(new SequentialTransition());

        List<PathTransition> pathTransitions = new ArrayList<>();
        for (int i = 0; i < elevatorView.getSteps().size(); i++) {
            Step s = elevatorView.getSteps().get(i);

            Path p = new Path();
            p.getElements().add(new MoveTo(s.getBeg().getX(), s.getBeg().getY()));
            p.getElements().add(new LineTo(s.getEnd().getX(), s.getEnd().getY()));

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(s.getDuration()));
            pathTransition.setPath(p);
            pathTransition.setNode(elevatorView.getRectangle());
            pathTransition.setOnFinished(actionEvent -> {
                double tX = elevatorView.getRectangle().getTranslateX();
                double tY = elevatorView.getRectangle().getTranslateY();

                if (elevatorView.getSteps().size() <= 0)
                    return;

                Step step = elevatorView.getSteps().get(0);

                int toDestination = 0;
                for (int j = 0; j < elevatorView.getSteps().size(); j++) {
                    toDestination++;
                    if (elevatorView.getSteps().get(j).isDestination())
                        break;
                }

                IElevatorsProgressListener progressListener = mSceneLogicImitator.getElevatorsProgressListener();
                if (progressListener != null)
                    progressListener.onElevatorFloorChanged(elevatorView.getElevatorID(), step.getFloor());

                if (elevatorView.getSteps().get(0).isDestination() || elevatorView.getSteps().size() == 1) {
                    double newX = step.getEnd().getX() - elevatorView.getRectangle().getWidth() / 2;
                    double newY = step.getEnd().getY() - elevatorView.getRectangle().getHeight() / 2;
                    elevatorView.getRectangle().setX(newX);
                    elevatorView.getRectangle().setY(newY);
                    elevatorView.getRectangle().setTranslateX(0);
                    elevatorView.getRectangle().setTranslateY(0);
                }

                elevatorView.getSteps().remove(0);

                if (progressListener != null && (step.isDestination() || elevatorView.getSteps().size() == 0)) {
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    progressListener.onElevatorArrived(elevatorView.getElevatorID());
                                }
                            },
                            10
                    );
                }
            });


            pathTransitions.add(pathTransition);
            if (s.isDestination())
                break;
        }

        elevatorView.getTransition().getChildren().clear();
        elevatorView.getTransition().getChildren().addAll(pathTransitions);
        elevatorView.getTransition().play();

        if (elevator != null && elevator.getState() == ElevatorState.WAITING) {
            IElevatorsProgressListener progressListener = mSceneLogicImitator.getElevatorsProgressListener();
            if (progressListener != null)
                progressListener.onElevatorDeparted(elevatorView.getElevatorID());
        }
    }

    public void moveElevatorToDestination(int newFloor, ElevatorView elevatorView) {
        Elevator elevator = mSceneLogicImitator.findElevator(elevatorView.getElevatorID());
        if (elevator != null) {
            if (newFloor == elevator.getFloor())
                return;

            if (elevatorView.getSteps().size() > 0) {
                boolean wasInWay = false;
                for (int i = 0; i < elevatorView.getSteps().size(); i++) {
                    if (i != 0 && elevatorView.getSteps().get(i).getFloor() == newFloor) {
                        elevatorView.getSteps().get(i).setIsDestination(true);
                        wasInWay = true;
                        break;
                    }
                }

                if (wasInWay) {
                    wasInWay = false;
                    for (int i = elevatorView.getSteps().size() - 1; i >= 0; i--) {
                        if (wasInWay && elevatorView.getSteps().get(i).isDestination())
                            break;
                        else
                            wasInWay = true;

                        elevatorView.getSteps().remove(i);
                    }

                    if (elevator.getState() == ElevatorState.MOVING)
                        return;
                }
            } else {
                planElevatorMove(newFloor, elevatorView);
            }
        }

        if (elevatorView.getSteps().isEmpty())
            return;

        if (elevatorView.getTimeline() == null)
            elevatorView.setTimeline(new Timeline());

        List<KeyFrame> keyFrames = new ArrayList<>();
        double tYValue = 0;
        double duration = 0;
        for (int i = 0; i < elevatorView.getSteps().size(); i++) {
            Step s = elevatorView.getSteps().get(i);
            tYValue += s.getEnd().getY() - s.getBeg().getY();
            duration += s.getDuration();

            // TODO: test
            KeyFrame keyFrame = new KeyFrame(Duration.millis(duration),
                    actionEvent -> {
                        if (elevatorView.getSteps().size() <= 0)
                            return;

                        Step step = elevatorView.getSteps().get(0);
                        elevatorView.getSteps().remove(0);

                        IElevatorsProgressListener progressListener = mSceneLogicImitator.getElevatorsProgressListener();
                        if (progressListener != null)
                            progressListener.onElevatorFloorChanged(elevatorView.getElevatorID(), step.getFloor());

                        if (step.isDestination() || elevatorView.getSteps().size() == 0) {
                            double newX = step.getEnd().getX() - elevatorView.getRectangle().getWidth() / 2;
                            double newY = step.getEnd().getY() - elevatorView.getRectangle().getHeight() / 2;
                            elevatorView.getRectangle().setX(newX);
                            elevatorView.getRectangle().setY(newY);
                            elevatorView.getRectangle().setTranslateX(0);
                            elevatorView.getRectangle().setTranslateY(0);

                            elevatorView.getTimeline().stop();
                        }

                        if (progressListener != null && (step.isDestination() || elevatorView.getSteps().size() == 0)) {
                            new java.util.Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            progressListener.onElevatorArrived(elevatorView.getElevatorID());
                                        }
                                    },
                                    10
                            );
                        }
                    },
                    new KeyValue(elevatorView.getRectangle().translateYProperty(), tYValue));

            keyFrames.add(keyFrame);
            if (s.isDestination())
                break;
        }

        elevatorView.getTimeline().getKeyFrames().clear();
        elevatorView.getTimeline().getKeyFrames().addAll(keyFrames);
        elevatorView.getTimeline().play();

        if (elevator != null && elevator.getState() == ElevatorState.WAITING) {
            IElevatorsProgressListener progressListener = mSceneLogicImitator.getElevatorsProgressListener();
            if (progressListener != null)
                progressListener.onElevatorDeparted(elevatorView.getElevatorID());
        }
    }

    public void saveParams(int numberOfElevators, int numberOfFloors, int numberOfPeople, int strategy) {
        this.mElevatorCount = numberOfElevators;
        this.mFloorCount = numberOfFloors;
        this.mNumberOfPeople = numberOfPeople;
        this.mStrategy = strategy;
    }

    @Override
    public void moveElevatorToFloor(int elevatorID, int newFloor) {
        ElevatorView elevatorView = null;
        for (ElevatorView ev : mElevatorViews) {
            if (ev.getElevatorID() == elevatorID) {
                elevatorView = ev;
                break;
            }
        }

        if (elevatorView != null) {
            moveElevatorToDestination(newFloor, elevatorView);
        }
    }

    @Override
    public void spawnPerson(int personID, int floor) {

    }

    @Override
    public void movePersonIntoElevator(int personID, int elevatorID) {

    }

    @Override
    public void movePersonFromElevator(int personID, int elevatorID) {

    }
}


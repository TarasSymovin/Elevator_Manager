package presentation.sample.presenter;

import java.io.IOException;
import java.net.URL;
import java.security.acl.Owner;
import java.util.ResourceBundle;

import andriichello.strategies.OwnershipElevatorStrategy;
import andriichello.types.ElevatorSceneImitator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import presentation.sample.presenter.elevators.ElevatorsPresenter;
import presentation.sample.types.ElevatorsScene;
import presentation.sample.types.ElevatorsSceneArgs;
import presentation.sample.types.IElevatorsScene;

public class SettingsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button save_button;

    @FXML
    private TextField numberOfElevators;

    @FXML
    public TextField numberOfFloors;

    @FXML
    private ComboBox<Integer> stratery;

    @FXML
    private TextField numberOfPeople;

    @FXML
    private ImageView image_view;

    private final Image backgroundImage = new Image("presentation/sample/images/main_background.jfif");
    @FXML
    void initialize() {
        ObservableList<Integer> strategyValue = FXCollections.observableArrayList(1,2);
        stratery.setItems(strategyValue);
        stratery.setValue(1);

        image_view.setImage(backgroundImage);
        image_view.setFitHeight(anchorPane.getHeight());
        anchorPane.setPrefWidth(image_view.getFitWidth());

        save_button.setOnAction(actionEvent -> {
            save_button.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/andriichello/views/sample.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            ElevatorsScene elevatorsScene = loader.getController();
//            ElevatorsPresenter elevatorsPresenter = new ElevatorsPresenter(
//                    new ElevatorsSceneArgs(Integer.parseInt(numberOfFloors.getText()), Integer.parseInt(numberOfElevators.getText()),
//                            stratery.getValue(), Integer.parseInt(numberOfPeople.getText())));
//            elevatorsPresenter.setView(elevatorsScene);
//            elevatorsScene.setElevatorsPresenter(elevatorsPresenter);
//            elevatorsScene.saveParams(Integer.parseInt(numberOfElevators.getText()), Integer.parseInt(numberOfFloors.getText()),
//                    Integer.parseInt(numberOfPeople.getText()), stratery.getValue());


            andriichello.scenes.ElevatorsSceneArgs args = new andriichello.scenes.ElevatorsSceneArgs();
            args.setFloorsCount(Integer.parseInt(numberOfFloors.getText()));
            args.setElevatorsCount(Integer.parseInt(numberOfElevators.getText()));
            args.setMaxPassengersCount(Integer.parseInt(numberOfPeople.getText()));
            args.setPassengersSpawnRate(1000);
            args.setPassengersSpawnAmount(3);

            if (stratery.getValue() == 1) {
                args.setElevatorStrategy(new OwnershipElevatorStrategy());
            } else {
                // TODO: create another strategy
                args.setElevatorStrategy(new OwnershipElevatorStrategy());
            }


            andriichello.scenes.ElevatorsScene scene = loader.getController();
            andriichello.types.ElevatorSceneImitator imitator = new ElevatorSceneImitator(args, scene);
            scene.setElevatorsSceneImitator(imitator);

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 600));
            stage.show();
        });
    }
}

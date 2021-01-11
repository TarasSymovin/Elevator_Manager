package sample.presenter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import sample.types.ElevatorsScene;

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

    private final Image backgroundImage = new Image("sample/images/main_background.jfif");

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
            loader.setLocation(getClass().getResource("/sample/views/sample.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ElevatorsScene chilldren = loader.getController();

            chilldren.saveParams(Integer.parseInt(numberOfElevators.getText()), Integer.parseInt(numberOfFloors.getText()),
                    Integer.parseInt(numberOfPeople.getText()), stratery.getValue());

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 600));
            stage.showAndWait();
        });
    }
}

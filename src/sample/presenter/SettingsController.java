package sample.presenter;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;

public class SettingsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button save_button;

    private final Image backgroundImage = new Image("sample/images/main_background.png");
    private final BackgroundImage background = new BackgroundImage(backgroundImage, null, null, null, null);


    @FXML
    void initialize() {
        assert save_button != null : "fx:id=\"save_button\" was not injected: check your FXML file 'settings.fxml'.";
    }
}

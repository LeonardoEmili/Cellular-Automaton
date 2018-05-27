package inputWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class FXMLIPController implements Initializable{

    @FXML
    private Slider slider1;

    @FXML
    private Slider slider2;

    @FXML
    private Slider slider3;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private TextField profileName;

    // The initialize() method runs once, when the FXML file is loaded.
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        label1.textProperty().bind(slider1.valueProperty().asString("%.0f"));
        label2.textProperty().bind(slider2.valueProperty().asString("%.0f"));
        label3.textProperty().bind(slider3.valueProperty().asString("%.0f"));
    }

    public void save() {
        InputBox.save(slider1, slider2, slider3, profileName.getText());
    }
}

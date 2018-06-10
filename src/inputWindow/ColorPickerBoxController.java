package inputWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class ColorPickerBoxController implements Initializable{

    @FXML
    private Pane pane;

    @FXML
    private ColorPicker colorPicker;

    private final static Paint backgroundColor =  Color.WHITE;


    @Override
    public void initialize(URL url, ResourceBundle resource) {
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        pane.setBackground(background);
    }

    public void changeBG() {
        Paint fill = colorPicker.getValue();
        BackgroundFill backgroundFill = new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        pane.setBackground(background);
    }

    public void sendDATA() {
        ColorPickerBox.setCurrentColor(colorPicker.getValue());
    }

}

package inputWindow;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class ColorPickerBoxController{

    @FXML
    private Pane pane;

    @FXML
    private ColorPicker colorPicker;

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

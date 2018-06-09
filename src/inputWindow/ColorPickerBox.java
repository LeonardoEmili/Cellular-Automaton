package inputWindow;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.*;

import java.io.IOException;

public class ColorPickerBox {

    private static Stage stage;
    private static Color currentColor = new Color(1, 1, 1, 1);      // By default white color is chosen
    private static boolean set = false;


    public ColorPickerBox() {

        try {
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Parent root = FXMLLoader.load(getClass().getResource("ColorPickerBox.fxml"));
            stage.setTitle("Choose a color");
            stage.setScene(new Scene(root, 500, 400));
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Color show() {
        set = false;
        stage.showAndWait();
        if (set)
            return currentColor;
        else
            return null;
    }

    public static void setCurrentColor(Color c) {
        set = true;
        currentColor = c;
        stage.close();
    }
}
package inputWindow;

import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class MessageBox {

    public static void show(String message, String title, int fontSize) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        //stage.setMinWidth(300);
        //stage.setMinHeight(100);

        Label lbl = new Label();
        lbl.setMinHeight(50);
        lbl.setPadding(new Insets(20, 20, 0, 20));
        lbl.setText(message);
        lbl.setFont(new Font("Arial", fontSize));


        Button btnOK = new Button();
        btnOK.setText("OK");
        btnOK.setFont(new Font("Arial", 16));
        btnOK.setOnAction(e -> stage.close());

        VBox pane = new VBox(20);
        pane.getChildren().addAll(lbl, btnOK);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
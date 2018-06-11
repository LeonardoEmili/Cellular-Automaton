package simulatorWindow;

import com.google.gson.Gson;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulatorWindow.programs.CellularAutomataProgram;
import simulatorWindow.programs.Default;
import simulatorWindow.utils.Status;

import java.io.*;

public class SaveWindow {

    private static Default defaultProgram;
    private static Stage stage;
    private static final String extension = ".stb";

    public static void show(CellularAutomataProgram defaultProgram) {
        buildGUIStuff();
        SaveWindow.defaultProgram = (Default) defaultProgram;
        stage.showAndWait();
    }

    private static void saveCurrentProgram(String file) {
        defaultProgram.name = file;
        Gson gson = new Gson();
        Loader wrap = new Loader();
        fillLoader(wrap);
        String templateJson = gson.toJson(wrap);
        try {
            FileWriter f = new FileWriter(file + extension);
            f.write(templateJson);
            f.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void fillLoader(Loader wrap){
        for (Status s:defaultProgram.states) {
            wrap.colNames.add(s.colorName);
            wrap.colors.add(s.hexValue);
            wrap.ids.add(s.id);
            wrap.nextIds.add(s.nextId);
            wrap.rulez.addAll(s.ruleSet);   //  Optimized -> for (Rule r: s.ruleSet) wrap.rulez.add(r);
        }
    }

    //------------------------------------GUI--------------------------------------

    private static void buildGUIStuff() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Save");
        stage.setMinWidth(400);
        Label lbl = new Label();
        lbl.setMinHeight(50);
        lbl.setText("Choose the name for saving the file:");
        lbl.setFont(new Font("Arial", 16));
        TextArea txtArea = new TextArea();
        txtArea.setMaxHeight(5);
        txtArea.setMaxWidth(300);

        Button btnOK = new Button();
        btnOK.setText("Save");
        btnOK.setFont(new Font("Arial", 16));
        btnOK.setOnAction(e -> {
            saveCurrentProgram(txtArea.getText());
            stage.close();
        });

        VBox pane = new VBox(20);
        pane.getChildren().addAll(lbl, txtArea, btnOK);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
    }

}

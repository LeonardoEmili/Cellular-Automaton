package simulatorWindow;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulatorWindow.programs.*;
import simulatorWindow.utils.InitialStateCondition;
import simulatorWindow.utils.iVec2;
import welcomeWindow.FXMLWBController;

import java.awt.*;
import java.io.File;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class SimulatorController implements Initializable{

    @FXML
    private Canvas canvas;

    @FXML
    private Button b1;

    @FXML
    private Button b2;

    @FXML
    private Button b3;

    @FXML
    private Button b4;

    @FXML
    private Button b5;

    @FXML
    private Button b6;

    @FXML
    private Button b7;

    @FXML
    private Button b8;

    @FXML
    private Button b9;

    @FXML
    private Button b10;

    @FXML
    private Button b11;

    @FXML
    private Button b12;

    @FXML
    private Button b13;

    @FXML
    private Button b14;

    @FXML
    private Label genCount;

    @FXML
    private Label cellCount;

    @FXML
    private Button saveBtn;

    private Simulator simulator;
    private Button[] buttonArray;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        this.simulator = FXMLWBController.getSimulator();
        buttonArray = new Button[] {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, saveBtn};
        simulator.setCanvas(canvas);
        setBtnEvents();
        Timeline guiUpdater = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            genCount.setText(Integer.toString(simulator.generationCount));
            int updatedCells = simulator.size.x * simulator.size.y * simulator.generationCount;
            cellCount.setText(Integer.toString(updatedCells));
        }));
        guiUpdater.setCycleCount(Timeline.INDEFINITE);
        guiUpdater.play();
    }

    public void start() {
        simulator.start();
    }

    public void pause() {
        simulator.pause();
    }

    public void stop() {
        simulator.stop();
    }

    public void tick() {
        simulator.tick();
    }

    public void openGitHubPage() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/leoemili/Cellular-automata"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goHome() {
        // todo all threads have to be stopped before doing any switch of scene
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/welcomeWindow/WelcomeBox.fxml"));
            Stage window = (Stage)(b1.getScene().getWindow());
            window.setWidth(920);
            window.setHeight(520);
            b1.getScene().setRoot(anotherRoot);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    public void save() {
        SaveWindow.show(simulator.currentProgram);
    }

    private void setBtnEvents() {
        boostAtomatas();
        b1.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.evenRows));
        b2.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.random));
        b3.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.xySin));
        b4.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.xySin2));
        b5.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.dottedTop));
        b6.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.topRow));
        b7.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.onlyEven));
        b8.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.xyXOR));
        b9.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.mod3));
        b10.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.random2));
        b11.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.random3));
        b12.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.randomGrayScale));
        b13.setOnAction( e -> simulator.setInitialStateCondition(InitialStateCondition.randomColor));
        b14.setOnAction(e -> {
            try {
                selectInitialConditionImage();
            } catch (MalformedURLException ex) { }
        });
    }

    private void boostAtomatas() {
        for (Button btn: buttonArray) { btn.setDisable(false); }
        if (!(simulator.currentProgram instanceof Default))
            saveBtn.setDisable(true);
        if (simulator.currentProgram instanceof CyclicCA) {
            b1.setDisable(true);    b2.setDisable(true);    b3.setDisable(true);
            b4.setDisable(true);    b5.setDisable(true);    b6.setDisable(true);
            b7.setDisable(true);    b8.setDisable(true);    b9.setDisable(true);
            b10.setDisable(true);   b11.setDisable(true);
        } else if (simulator.currentProgram instanceof DiffusionAggregation) {
            b5.setDisable(true);
        } else if (simulator.currentProgram instanceof H3Rule) {
            b1.setDisable(true);    b2.setDisable(true);    b3.setDisable(true);
            b4.setDisable(true);    b7.setDisable(false);   b8.setDisable(true);
            b9.setDisable(true);    b10.setDisable(true);   b11.setDisable(true);
            b12.setDisable(true);   b13.setDisable(true);   b14.setDisable(true);
        }
    }

    private void selectInitialConditionImage() throws MalformedURLException {
        simulator.setInitialStateCondition(InitialStateCondition.fromImage);


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(b1.getScene().getWindow());

        if (selectedFile != null) {
            String imageFile = selectedFile.toURI().toURL().toString();

            Image image = new Image(imageFile);
            // Obtain PixelReader
            CellularAutomataProgram.pixelReader = image.getPixelReader();
            CellularAutomataProgram.imageSize = new iVec2((int)image.getWidth(), (int)image.getHeight());
        }
    }

}
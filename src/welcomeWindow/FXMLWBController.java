package welcomeWindow;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simulatorWindow.Loader;
import simulatorWindow.Simulator;
import simulatorWindow.programs.Default;
import simulatorWindow.utils.Status;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FXMLWBController implements Initializable {

    @FXML
    private Button handleProfiles;

    private static Simulator simulator;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        simulator = new Simulator();
    }

    public void showInputForm() {
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/inputWindow/InputBox.fxml"));
            handleProfiles.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showAutomataCollection() {
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/CAutomatasWindow/CAutomatas.fxml"));
            handleProfiles.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void open() {             // This function gets called when button LOAD is hit
        ArrayList<Status> statesLoaded = openFile();
        goToSimulation(statesLoaded);
    }

    private void goToSimulation(ArrayList<Status> arr) {
        try {
            simulator.setProgram(new Default(arr, "Default"));
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/simulatorWindow/Simulator.fxml"));
            Stage window = (Stage)(handleProfiles.getScene().getWindow());
            window.setWidth(900);
            window.setHeight(700);
            handleProfiles.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Simulator getSimulator() {
        return simulator;
    }

    private ArrayList<Status> openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .stb File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("stb Files", "*.stb"));
        File selectedFile = fileChooser.showOpenDialog(handleProfiles.getScene().getWindow());
        Loader load = new Loader();
        try {
            JsonReader reader = new JsonReader(new FileReader(selectedFile));
            load = new Gson().fromJson(reader, Loader.class);
        } catch (FileNotFoundException ex) { ex.printStackTrace(); }
        return load.parseStates();
    }

}
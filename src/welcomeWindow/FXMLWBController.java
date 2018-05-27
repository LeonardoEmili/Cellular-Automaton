package welcomeWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import simulatorWindow.Simulator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLWBController implements Initializable{

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

    public static Simulator getSimulator() {
        return simulator;
    }

}
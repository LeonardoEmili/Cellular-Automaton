package CAutomatasWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import simulatorWindow.Simulator;
import simulatorWindow.programs.CellularAutomataProgram;
import welcomeWindow.FXMLWBController;

import java.lang.reflect.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CAutomatasController implements Initializable{

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    private Simulator simulator;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        this.simulator = FXMLWBController.getSimulator();
        Button[] buttons = new Button[] {button1, button2, button3, button4, button5};
        for (Button btn: buttons) {
            btn.setStyle("-fx-background-radius: 30");
        }
    }


    public void showHome() {
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/welcomeWindow/WelcomeBox.fxml"));
            button1.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void showSimulation(ActionEvent e) {
        comeToMeCA(e.toString().split("'")[1].replaceAll("\\s",""));
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/simulatorWindow/Simulator.fxml"));
            Stage window = (Stage)(button1.getScene().getWindow());
            window.setWidth(900);
            window.setHeight(700);
            button1.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void comeToMeCA(String className) {
        try {
            Class<?> C = CellularAutomataProgram.class.forName("simulatorWindow.programs." + className);
            CellularAutomataProgram currentProgram = CellularAutomataProgram.class.cast(C.getConstructor().newInstance());
            simulator.setProgram(currentProgram);
        } catch (ClassNotFoundException e) {
            System.out.println("Class " + className + " has not been defined.");
        } catch (NoSuchMethodException e) {
            System.out.println("Constructor not found");
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
        } catch (InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
    }

}

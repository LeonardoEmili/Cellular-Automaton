package welcomeWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import simulatorWindow.Simulator;
import simulatorWindow.programs.Default;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
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

    public void open(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .stb File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("stb Files",
                        "*.stb")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(handleProfiles.getScene().getWindow());
        try{
            if (selectedFile != null) {
                String file = selectedFile.toURI().toURL().toString();
                FileInputStream f = new FileInputStream(new File(file));
                ObjectInputStream o = new ObjectInputStream(f);
                Default obj;
                obj = (Default) o.readObject();
                o.close();
                f.close();
            }
        }catch(IOException | ClassNotFoundException ex){}

    }

}
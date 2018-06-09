package inputWindow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rulesWindow.RuleBoxController;
import simulatorWindow.utils.State;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class FXMLIPController implements Initializable {

    @FXML
    private TableView<State> table;

    @FXML
    private TableColumn<State, String> idCol;

    @FXML
    private TableColumn<State, String> colorCol;

    @FXML
    private TableColumn<State, String> hexCol;


    private ObservableList<State> data;
    private ColorPickerBox mb = new ColorPickerBox();

    // The initialize() method runs once, when the FXML file is loaded.
    @Override
    public void initialize(URL url, ResourceBundle resource) {

        //  label1.textProperty().bind(slider1.valueProperty().asString("%.0f")); may be useful to keep
        idCol.setCellValueFactory(new PropertyValueFactory<>("idCol"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("colorCol"));
        hexCol.setCellValueFactory(new PropertyValueFactory<>("hexCol"));
        data = FXCollections.observableArrayList();
        table.setItems(data);
    }

    public void addRow() {
        String hexVal = "" + mb.show();
        if (hexVal.equals("null"))
            return;
        State s = new State(data.size()+1, "42", hexVal);
        data.add(s);
    }

    public void showResult() {
        if (data.size() == 0) {
            System.out.println("Seleziona almeno uno stato per continuare!");
            return;
        }

        RuleBoxController.setStates(data);
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/rulesWindow/RuleBox.fxml"));
            table.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showHome() {
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/welcomeWindow/WelcomeBox.fxml"));
            table.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

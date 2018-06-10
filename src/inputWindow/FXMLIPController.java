package inputWindow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
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
        idCol.setStyle( "-fx-alignment: CENTER;");
        hexCol.setStyle( "-fx-alignment: CENTER;");
        data = FXCollections.observableArrayList();
        table.setItems(data);
        addCallBack();
    }

    public void addRow() {
        try {
            String hexVal = mb.show().toString();
            hexVal = hexVal.substring(0, hexVal.length() - 2);
            if (hexVal.equals("null"))
                return;
            State s = new State(data.size() + 1, hexVal, hexVal);
            data.add(s);
        } catch (NullPointerException ex) {
            // Nothing to do here .. user has closed ColorPickerBox before choosing a color.
        }
        refreshIndexes();
        table.refresh();
    }

    public void clearTable() {
        data.clear();
    }

    public void removeRow() {
        State selectedItem = table.getSelectionModel().getSelectedItem();
        table.getItems().removeAll(selectedItem);
        System.out.println(data.toString());        // todo just to visualize the current output, to be removed
        refreshIndexes();
        table.refresh();
    }

    public void showResult() {
        if (data.size() == 0) {
            MessageBox.show("Choose at least one State to continue!", "State required", 20);
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

    private void addCallBack() {

        colorCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setStyle("-fx-background-color: #"+ item.substring(2, item.length()) + ";");
                }
            }
        });


    }

    private void refreshIndexes() {
        for (int i = 0; i < data.size(); i++) {
            State currentState = data.get(i);
            currentState.setID(i+1);
        }
    }

}

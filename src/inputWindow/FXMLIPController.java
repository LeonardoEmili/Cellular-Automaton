package inputWindow;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.Pane;
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

    @FXML
    private TableColumn<State, String> nextIdCol;

    @FXML
    private TableColumn<State, String> nextColorCol;


    private ObservableList<State> data;
    private ColorPickerBox mb = new ColorPickerBox();

    // The initialize() method runs once, when the FXML file is loaded.
    @Override
    public void initialize(URL url, ResourceBundle resource) {

        idCol.setCellValueFactory(new PropertyValueFactory<>("idCol"));         // This column value comes from getIdCol in State
        colorCol.setCellValueFactory(new PropertyValueFactory<>("colorCol"));   // This column value comes from getColorCol in State
        hexCol.setCellValueFactory(new PropertyValueFactory<>("hexCol"));       // This column value comes from getHexCol in State
        nextIdCol.setCellValueFactory(new PropertyValueFactory<>("nextIdCol"));       // This column value comes from getNextIdCol in State
        nextColorCol.setCellValueFactory(new PropertyValueFactory<>("nextColorCol"));   // This column value comes from getNextColorCol in State
        idCol.setStyle( "-fx-alignment: CENTER;");      // GUI stuff
        hexCol.setStyle( "-fx-alignment: CENTER;");     // GUI stuff
        nextIdCol.setStyle( "-fx-alignment: CENTER;");  // GUI stuff
        table.setEditable(true);
        nextColorCol.setStyle( "-fx-alignment: CENTER;");  // GUI stuff
        data = FXCollections.observableArrayList();
        table.setItems(data);       // Table has its data, the state list
        addCallBack();      // Useful for keeping the tabled always up-to-date

        //  label1.textProperty().bind(slider1.valueProperty().asString("%.0f")); may be useful to keep for the future
    }

    public void addRow() {
        try {
            String hexVal = mb.show().toString();       // Reads the HEX value of the color chosen by the user
            hexVal = hexVal.substring(0, hexVal.length() - 2);      // Removes the last 2 ff, really don't know why this happens
            if (hexVal.equals("null"))      // If the color is not picked up, probably unnecessary, keep to be sure
                return;
            State s = new State(data.size() + 1, hexVal, hexVal);       // Creates the State
            data.add(s);        // Add to State list
        } catch (NullPointerException ex) {
            // Nothing to do here .. this happens because user has closed ColorPickerBox before choosing a color.
        }
        refreshIndexes();       // Utility-func which keeps the State-indexes always up-to-date
        table.refresh();        // Forces the table to do a refresh, useful for keeping color in order
    }

    public void clearTable() {
        data.clear();           // Delete every state previously chosen
    }

    public void removeRow() {       // Remove the currentSelectedItem from the TableView
        State selectedItem = table.getSelectionModel().getSelectedItem();
        table.getItems().removeAll(selectedItem);       // Probably remove is fine, keep to be sure
        System.out.println(data.toString());        // todo just to visualize the current output, to be removed
        refreshIndexes();       // Utility-func which keeps the State-indexes always up-to-date
        table.refresh();        // Forces the table to do a refresh, useful for keeping color in order
    }

    public void showResult() {      // Goes to the next window if at least a State is in the Data collection
        if (data.size() == 0) {
            MessageBox.show("Choose at least one State to continue!", "State required", 20);
            return;
        }
        RuleBoxController.setStates(data);      // Passes the State's list to next window
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

    private void addCallBack() {        // Keeps table updated

        colorCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setStyle("-fx-background-color: #"+ item.substring(2, item.length()) + ";");
                }
            }
        });


        nextColorCol.setCellFactory(column -> new TableCell<>() {       // Eventually updates the next color row
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    if (item.contains("NOT"))
                        setText(item);
                    else
                        setStyle("-fx-background-color: #"+ item.substring(2, item.length()) + ";");
                }
            }
        });

        nextIdCol.setCellFactory(TextFieldTableCell.<State>forTableColumn());       // Update next color row
        nextIdCol.setOnEditCommit(
                t -> {
                    String newVal = t.getNewValue();
                    if (isValidID(newVal)) {
                        State currentState = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        int index = searchForID(newVal);
                        currentState.setNewID(newVal);
                        State next = data.get(index);
                        currentState.setNextColor(next.getColorCol());
                        currentState.setNextState(next);

                    }
                    table.refresh();
                }
        );
        for (TableColumn<?, ?> t: table.getColumns()) {
            t.setReorderable(false);
        }
    }

    private boolean isValidID(String id) {          // Looks if the user-chosen ID is correct or not
        for (State s: table.getItems()) {
            if (s.getIdCol().equals(id))
                return true;
        }
        return false;
    }

    private int searchForID(String newID) {     // Utility methods that search a State by its ID
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            State s = data.get(i);
            if (s.getIdCol().equals(newID)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void refreshIndexes() {         // Utility function
        for (int i = 0; i < data.size(); i++) {
            State currentState = data.get(i);
            currentState.setID(i+1);
        }

        for (int i = 0; i < data.size(); i++) {
            State currentState = data.get(i);
            int index = data.indexOf(currentState.getNextState());
            if (index == -1) {
                currentState.setNewID(currentState.getIdCol());
                currentState.setNextState(currentState);
                currentState.setNextColor(currentState.getColorCol());
            } else
                currentState.setNewID(String.valueOf(index+1));
        }

    }


}

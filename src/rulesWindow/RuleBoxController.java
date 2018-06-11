package rulesWindow;

import inputWindow.FXMLIPController;
import inputWindow.MessageBox;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import simulatorWindow.Simulator;
import simulatorWindow.programs.Default;
import simulatorWindow.utils.*;
import welcomeWindow.FXMLWBController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class RuleBoxController implements Initializable {

    @FXML
    private HBox statesBar;

    @FXML
    private HBox statesBar2;

    @FXML
    private Label ruleNum;

    @FXML
    private Label stateNum;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField input;

    @FXML
    private RadioButton LTtoggle;

    @FXML
    private RadioButton CTtoggle;

    @FXML
    private RadioButton RTtoggle;

    @FXML
    private RadioButton LCtoggle;

    @FXML
    private RadioButton RCtoggle;

    @FXML
    private RadioButton LBtoggle;

    @FXML
    private RadioButton CBtoggle;

    @FXML
    private RadioButton RBtoggle;

    @FXML
    private ToggleButton NOTRule;

    @FXML
    private ToggleButton ANDRule;

    private static ObservableList<Status> states;        // List of States selected by user
    private static ArrayList<String> choiceItems;       // Items that appears in ChoiceBox
    private final int defaultStateValue = 0;            // Current state in analysis
    private final int defaultRuleValue = 1;             // Current state in analysis
    private final int defaultNotRuleValue = 0;          // If is 0 then NOT is not active, 1 is active
    private final int defaultSelectedStateValue = -1;   // Default value, if is -1 then no state has been selected
    private IntegerProperty currentState;               // GUI stuff
    private IntegerProperty currentRule;                // GUI stuff
    private int selectedState = defaultSelectedStateValue;  // Integer that select a state from the list
    private RadioButton[] boxArray;                         // Just a wrapper for buttons, used to loop over them

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        buildGUIStuff();
        nextState();
    }

    public static void setStates(ObservableList<Status> listStates) {
        states = listStates;
    }       // Used to pass the States from the previous window


    private void setSelected(HBox bar) {
        int index = 0;
        for (int i = 0; i < bar.getChildren().size(); i++) {
            Rectangle rr = (Rectangle)bar.getChildren().get(i);
            if (rr.isHover())
                index = i;
            rr.setStyle("-fx-border-color: none;");     // Probably to fix, should look like as fx-stroke ..
        }
        Rectangle r = (Rectangle)(bar.getChildren().get(index));
        r.setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
        selectedState = Integer.parseInt(states.get(index).getIdCol());
    }

    public void nextState() {
        if (currentState.getValue() == states.size()) {
            ArrayList<Status> newStates=new ArrayList<>();
            for(Status s: this.states){
                s.setOperator();
                newStates.add(s);
            }
            Default program=new Default(newStates,"Default");
            for(Status st:program.states){st.prog=program;}
            Simulator simulator=FXMLWBController.getSimulator();
            simulator.setProgram(program);
            showSimulation();
            return;
        }
        currentRule.setValue(defaultRuleValue);         // Rule counter re-initializes for-each state
        currentState.setValue(currentState.getValue()+1);   // Evaluate next state
        for (Node n: statesBar.getChildren()) {
            Rectangle rr = (Rectangle)n;
            rr.setStyle("-fx-border-color: none;");     // Probably to fix, should look like as fx-stroke ..
        }
        Rectangle r = (Rectangle)(statesBar.getChildren().get(currentState.getValue() -1));  // Select the current state-rect
        r.setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
    }

    public void getAnyNearby() {
        try {
            int value = Integer.parseInt(input.getText());
            input.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color:transparent");
            if (selectedState == -1) {
                MessageBox.show("No Status has been selected.","No selected Status", 16);
                return;
            }
            int[] arr={};
            boolean not=NOTRule.isSelected();
            boolean and=ANDRule.isSelected();
            // Format: [any|spec,  exactly|at least|not more, how much, 8 neighbors, landing Status]
            Rule newRule = new Rule (arr, true, not,and, choiceItems.indexOf(choiceBox.valueProperty().getValue())==0, value, selectedState);
            //System.out.println(Arrays.toString(newRule));       // todo remove
            selectedState = defaultSelectedStateValue;
            cleanSelected();
            states.get(currentState.intValue() - 1).addRule(newRule);       // Adds rule to the relative state
            nextRule();
            input.clear();
            deSelectAll();
        } catch (NumberFormatException ex) {
            input.setStyle("-fx-faint-focus-color: red; -fx-focus-color:rgba(255,0,0,0.2);");
        } catch (NullPointerException ex) {
            input.setStyle("-fx-faint-focus-color: red; -fx-focus-color:rgba(255,0,0,0.2);");
        }
    }

    public void getThoseNearby() {
        int counterSelected = 0;
        for (RadioButton c: boxArray) { if (c.isSelected()) counterSelected++; }
        if (shouldNotPass(counterSelected))
            return;
        Rule newRule = makeRule(counterSelected);
        //System.out.println(Arrays.toString(newRule));       // todo remove
        selectedState = defaultSelectedStateValue;
        cleanSelected();        // Deselects all the rectangles at the bottom
        states.get(currentState.intValue() - 1).addRule(newRule);       // Adds rule to relative Status
        nextRule();
        deSelectAll();
    }

    private Rule makeRule(int counterSelected) {
        // Format: [any|spec,  exactly|at least|not more, how much ,8 neighbors, landing Status]
        int value = Integer.parseInt(input.getText());
        boolean not=NOTRule.isSelected();
        boolean and=ANDRule.isSelected();
        int[] arr={-1,-1,-1,-1,-1,-1,-1,-1};
        for (int i = 0; i < 8; i++) {
            if (boxArray[i].isSelected())
                arr[i] = selectedState;
        }
        return new Rule (arr, false, not,and, choiceItems.indexOf(choiceBox.valueProperty().getValue())==0, value, selectedState);
    }

    private void createUpperHBox() {        // Dynamically creates the upper HBox
        for (Status s : states) {
            Rectangle r = new Rectangle();
            r.setHeight(statesBar.getMaxHeight());
            r.setWidth(statesBar.getMaxWidth()/states.size());
            r.setFill(Color.web(s.getHexCol(),1.0));
            statesBar.getChildren().add(r);
        }
    }

    private void createLowerHBox() {        // Dynamically create the lower HBox, this method adds an eventListener for each Rectangle
        for (Status s : states) {
            Rectangle r = new Rectangle();
            r.setHeight(statesBar.getMaxHeight());
            r.setWidth(statesBar.getMaxWidth()/states.size());
            r.setFill(Color.web(s.getHexCol(),1.0));
            r.setOnMouseClicked(e -> setSelected(statesBar2));
            statesBar2.getChildren().add(r);
        }
    }
    public void showSimulation() {
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/simulatorWindow/Simulator.fxml"));
            Stage window = (Stage)(statesBar.getScene().getWindow());
            window.setWidth(900);
            window.setHeight(700);
            statesBar.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //--------------------------------UTILITY-METHODS--------------------------------------------------------

    private void cleanSelected() {      // GUI method to clear all the Rectangle's border
        for (Node n: statesBar2.getChildren()) {
            Rectangle rr = (Rectangle)n;
            rr.setStyle("-fx-border-color: none;");     // Probably to fix, should look like as fx-stroke ..
        }
    }

    private void buildGUIStuff() {
        boxArray = new RadioButton[] {
                LTtoggle, CTtoggle, RTtoggle,
                LCtoggle          , RCtoggle,
                LBtoggle, CBtoggle, RBtoggle        };
        choiceItems = new ArrayList();
        choiceItems.add("Exactly");
        choiceItems.add("At least");
        currentRule = new SimpleIntegerProperty(defaultRuleValue);
        currentState = new SimpleIntegerProperty(defaultStateValue);
        ruleNum.textProperty().bind(currentRule.asString());
        stateNum.textProperty().bind(currentState.asString());
        choiceBox.getItems().addAll(choiceItems.get(0), choiceItems.get(1));
        choiceBox.setValue("Exactly");
        if (states.size() > 0) {        // Probably unnecessary, just keep it to be sure
            createUpperHBox();
            createLowerHBox();
        }
    }

    private boolean shouldNotPass(int counterSelected) {        // lol
        if (selectedState == -1) {
            MessageBox.show("No Status has been selected.","No selected Status", 16);
            return true;
        }

        if (counterSelected == 0) {
            MessageBox.show("Selected at least a cell to continue.","No selected cell", 16);
            return true;
        }
        return false;
    }

    private void nextRule() {
        currentRule.setValue(currentRule.getValue()+1);     // Just pass over to the next rule
    }

    private void deSelectAll() {            // Method called after a rule is made, re-deselect every neighbor
        for (RadioButton r: boxArray){
            r.setSelected(false);
        }
        NOTRule.setSelected(false);
    }

    public void goRuleWin() {           // Shows the Rule Window
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/inputWindow/InputBox.fxml"));
            statesBar.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //--------------------------END-OF-UTILITY-METHODS--------------------------------------------------------


}

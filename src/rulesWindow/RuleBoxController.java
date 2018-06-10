package rulesWindow;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import simulatorWindow.utils.State;

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

    private static ObservableList<State> states;
    private static ArrayList<String> choiceItems;
    private final int defaultStateValue = 0;
    private final int defaultRuleValue = 1;
    private final int defaultSelectedStateValue = -1;
    private IntegerProperty currentState;
    private IntegerProperty currentRule;
    private int selectedState = defaultSelectedStateValue;
    private RadioButton[] boxArray;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        buildGUIStuff();
        nextState();
    }

    public static void setStates(ObservableList<State> listStates) {
        states = listStates;
    }

    private void createUpperHBox() {
        for (State s : states) {
            Rectangle r = new Rectangle();
            r.setHeight(statesBar.getMaxHeight());
            r.setWidth(statesBar.getMaxWidth()/states.size());
            r.setFill(Color.web(s.getHexCol(),1.0));
            statesBar.getChildren().add(r);
        }
    }

    private void createLowerHBox() {
        for (State s : states) {
            Rectangle r = new Rectangle();
            r.setHeight(statesBar.getMaxHeight());
            r.setWidth(statesBar.getMaxWidth()/states.size());
            r.setFill(Color.web(s.getHexCol(),1.0));
            r.setOnMouseClicked(e -> {
                setSelected(statesBar2, s.getIdCol());
            });
            statesBar2.getChildren().add(r);
        }
    }

    private void setSelected(HBox bar, String id) {
        int index = 0;
        for (int i = 0; i < bar.getChildren().size(); i++) {
            Rectangle rr = (Rectangle)bar.getChildren().get(i);
            if (rr.isHover())
                index = i;
            rr.setStyle("-fx-border-color: none;");     // Probably to fix, should look like as fx-stroke ..
        }
        Rectangle r = (Rectangle)(bar.getChildren().get(index));
        r.setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
        selectedState = index;
    }

    public void nextState() {
        if (currentState.getValue() == states.size()) {
            System.out.println("Non ce ne stanno piú ..");
            for (State s: states) {
                s.printRules();
            }
            return;
        }
        currentRule.setValue(defaultRuleValue);         // Rule counter re-initializes for-each state
        currentState.setValue(currentState.getValue()+1);   // Evaluate next state
        for (Node n: statesBar.getChildren()) {
            Rectangle rr = (Rectangle)n;
            rr.setStyle("-fx-border-color: none;");     // Probably to fix, should look like as fx-stroke ..
        }
        Rectangle r = (Rectangle)(statesBar.getChildren().get(currentState.getValue().intValue() -1));  // Select the current state-rect
        r.setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
    }

    public void getAnyNearby() {
        try {
            int value = Integer.parseInt(input.getText());
            input.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color:transparent");
            if (selectedState == -1) {
                System.out.println("No selected State");
                return;
            }

            // Format: [any|spec,  exactly|at least|not more, how much, 8 neighbors, landing Status]
            int[] newRule = new int[] {0, choiceItems.indexOf(choiceBox.valueProperty().getValue().toString()), value, 0, 0, 0, 0, 0, 0, 0, 0, selectedState};

            System.out.println(Arrays.toString(newRule));       // todo remove
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
        int[] newRule = makeRule(counterSelected);
        System.out.println(Arrays.toString(newRule));       // todo remove
        selectedState = defaultSelectedStateValue;
        cleanSelected();        // Deselects all the rectangles at the bottom
        states.get(currentState.intValue() - 1).addRule(newRule);       // Adds rule to relative State
        nextRule();
        deSelectAll();
    }

    private int[] makeRule(int counterSelected) {
        // Format: [any|spec,  exactly|at least|not more, how much ,8 neighbors, landing Status]
        int[] newRule = new int[] {1, -1, counterSelected, 0, 0, 0, 0, 0, 0, 0, 0, selectedState};
        int offset = 3;
        for (int i = 0; i < 8; i++) {
            int currentIndex = i + offset;
            if (boxArray[i].isSelected())
                newRule[currentIndex] = 1;
        }
        return newRule;
    }

    //--------------------------------UTILITY-METHODS--------------------------------------------------------

    private void cleanSelected() {
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
        choiceItems.add("Esattamente");
        choiceItems.add("Almeno");
        choiceItems.add("Non piú");
        currentRule = new SimpleIntegerProperty(defaultRuleValue);
        currentState = new SimpleIntegerProperty(defaultStateValue);
        ruleNum.textProperty().bind(currentRule.asString());
        stateNum.textProperty().bind(currentState.asString());
        choiceBox.getItems().addAll(choiceItems.get(0), choiceItems.get(1), choiceItems.get(2));
        choiceBox.setValue("Esattamente");
        if (states.size() > 0) {
            createUpperHBox();
            createLowerHBox();
        }
    }

    private boolean shouldNotPass(int counterSelected) {        // lol
        if (selectedState == -1) {
            System.out.println("No selected State");
            return true;
        }

        if (counterSelected == 0) {
            System.out.println("Selezionare almeno un vicino per continuare !");  // todo aggiungere un evento
            return true;
        }
        return false;
    }

    private void nextRule() {
        currentRule.setValue(currentRule.getValue()+1);     // Just pass over to the next rule
    }

    private void deSelectAll() {
        // Deselect the neighbors
        for (RadioButton r: boxArray){
            r.setSelected(false);
        }
    }

    public void goRuleWin() {
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/inputWindow/InputBox.fxml"));
            statesBar.getScene().setRoot(anotherRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //--------------------------END-OF-UTILITY-METHODS--------------------------------------------------------


}

package simulatorWindow.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class State {

    private static String defaultMSG = "NOT SET OR EQUAL TO ID";
    private int id;         // IDs start counting from 1.. pay attention
    private int nextId;
    private String color;
    private String nextColor;
    private String hexValue;
    private State nextState = this;
    private ArrayList<int[]> ruleSet;   // Format: [any|spec,  exactly|at least|not more, how much, 8 neighbors, landing Status]

    public State(int id, String color, String hexValue) {
        this.ruleSet = new ArrayList<>();
        this.id = id;
        this.nextId = id;
        this.color = color;
        this.nextColor = color;
        this.hexValue = hexValue;
    }

    public String getIdCol() {
        return String.valueOf(id);
    }

    public String getColorCol() {
        return color;
    }

    public String getNextColorCol() {
        if (color.equals(nextColor))
            return defaultMSG;
        else
            return nextColor;
    }

    public String getHexCol() {
        return hexValue;
    }

    public void setNextColor(String nextColor) { this.nextColor = nextColor; }

    public String getNextIdCol() { return String.valueOf(nextId); }

    public void addRule(int[] rule) {
        ruleSet.add(rule);
    }

    public void printRules() {
        System.out.println("---------------");
        System.out.println("Stato numero" + id + " :");
        for (int[] rule: ruleSet) {
            System.out.println(Arrays.toString(rule));
        }
    }

    public void setNewID(String newID) {
        nextId = Integer.parseInt(newID);
    }

    public void setID(int id) {
        this.id = id;
    }

    public State getNextState() {
        return nextState;
    }

    public void setNextState(State s) {
        this.nextState = s;
    }


}

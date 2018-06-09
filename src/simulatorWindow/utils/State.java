package simulatorWindow.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class State {

    private int id;         // IDs start counting from 1.. pay attention
    private String color;
    private String hexValue;
    private ArrayList<int[]> ruleSet;   // Format: [any|spec,  exactly|at least|not more, how much, 8 neighbors, landing Status]

    public State(int id, String color, String hexValue) {
        this.ruleSet = new ArrayList<>();
        this.id = id;
        this.color = color;
        this.hexValue = hexValue;
    }

    public String getIdCol() {
        return String.valueOf(id);
    }

    public String getColorCol() {
        return color;
    }

    public String getHexCol() {
        return hexValue;
    }

    public void addRule(int[] rule) {
        ruleSet.add(rule);
    }

    public void printRules() {
        System.out.println("Stato numero" + id + " :");
        for (int[] rule: ruleSet) {
            System.out.println(Arrays.toString(rule));
        }
    }


}

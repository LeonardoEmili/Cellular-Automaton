package simulatorWindow.utils;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Status extends Thread{
    public simulatorWindow.programs.Default prog;
    public int id;         // IDs start counting from 1
    public int nextId;
    public String colorName;
    public String nextColor;
    public String hexValue;
    public Status nextState = this;
    public ArrayList<Cell> cells = new ArrayList<>();
    public ArrayList<Rule> ruleSet;   // Format: [NOTRule, any|spec, Exactly|At least, How Much, 8 neighbors, referred Status]
    public boolean[][] operator;

    public Status(){}

    public Status(int id, String color, String hexValue) {
        this.ruleSet = new ArrayList<>();
        this.id = id;
        this.nextId = id;
        this.colorName = color;
        this.nextColor = color;
        this.hexValue = hexValue;
    }

    @Override
    public synchronized void run() {
        while (!isInterrupted()) {
            this.applyRules();          // We apply rules and than wait other processes to finish
            this.cells.clear();         // Clear all the cells inside the Arralist
            this.prog.ctrl++;           // Increases the FLAG
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e) { }
        }
    }

    //------------------------------------Methods-----------------------------------------

    private void applyRules() {
        for (Cell c : this.cells) {
            for (int i = 0; i < ruleSet.size(); i++) {
                this.operator[0][i] = ruleOnCell(c, ruleSet.get(i));
            }
            if (deMorganator())
                c.futureId = this.nextId;
            else
                c.futureId = this.id;
        }
    }

    private boolean deMorganator() {                                     // :D
        boolean outcome = true && this.operator[0][0];
        for (int i = 1; i < ruleSet.size(); i++) {
            if (this.operator[1][i])
                outcome &= this.operator[0][i];
            else
                outcome |= this.operator[0][i];
        }
        return outcome;
    }

    private boolean ruleOnCell(Cell c, Rule r) {                 // Apply rule upon a single cell
        if (r.any) {
            if(r.exact){return exactRule(r,c);}
            return anyRule(r, c);
        }
        return specificRule(r, c);
    }

    private boolean anyRule(Rule r, Cell c) {                    // Apply "any-neighbor-is-fine-but-minimum-n" rule
        int counter = 0;
        for (Cell near : c.nearby) {
            if (near.idStatus == r.nearStatus) {
                counter += 1;
            }
            if (counter == r.nNeighbors) {
                return true ^ r.not;
            }      // this ^ is xor, he applys "not" rule
        }
        return false ^ r.not;
    }

    private boolean specificRule(Rule r, Cell c) {                // Apply "just-specific-neighbors-are-fine-and-precisely-n" rule
        int counter = 0;
        for (int i = 0; i < 8; i++) {
            if (r.nearby[i] == -1) {
                continue;
            }
            if (r.nearby[i] == c.nearby.get(i).idStatus) {
                counter++;
            }
        }
        return (counter == r.nNeighbors) ^ r.not;
    }
    private boolean exactRule(Rule r, Cell c) {                    //Apply "any-neighbor-is-fine-but-minimum-n" rule
        int counter = 0;
        for (Cell near : c.nearby) {
            if (near.idStatus == r.nearStatus) {
                counter += 1;
            }

        }
        return (counter == r.nNeighbors) ^ r.not;
    }

    public void setOperator(){
        this.operator = new boolean[2][ruleSet.size()];
        for (int i = 0; i < ruleSet.size(); i++) {
            this.operator[1][i] = this.ruleSet.get(i).and;          // Fill operator with rule.and values
        }
    }

    public synchronized void wakeup(){
        notify();
    }

    public String getIdCol() {
        return String.valueOf(id);
    }

    public String getColorCol() {
        return colorName;
    }

    public String getNextColorCol() {
        if (colorName.equals(nextColor))
            return "NOT SET OR EQUAL TO ID";
        else
            return nextColor;
    }

    public String getHexCol() {
        return hexValue;
    }

    public void setNextColor(String nextColor) { this.nextColor = nextColor; }

    public String getNextIdCol() { return String.valueOf(nextId); }

    public void addRule(Rule r) {
        ruleSet.add(r);
    }

    public void setNewID(String newID) {
        nextId = Integer.parseInt(newID);
    }

    public void setID(int id) {
        this.id = id;
    }

    public Status getNextState() {
        return nextState;
    }

    public void setNextState(Status s) {
        this.nextState = s;
    }

    public iVec3 hex2Rgb(String colorStr) {
        Color c=Color.web(colorStr);
        return new iVec3((float)c.getRed(),(float)c.getGreen(),(float)c.getBlue());
    }

}

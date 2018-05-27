package utils;

import programs.CellularAutomataProgram;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Status extends Thread {
    public programs.Default prog;
    public vec3 color;                  //associated color (to print)
    public String name;                 //utils.Status name, if needed...
    public int id;                      //utils.Status "id"
    public int toStatus;                //can change into toStatus
    public ArrayList<Cell> cells;       //cells to manage
    public ArrayList<Rule> ruleSet;     //set of rules to verify for the status-changing
    public boolean[][] operator;        //a 2xruleset.lenght matrix of booleans, see "comments[0]" in the bottom

    //---------------------------------constructors--------------------------------------
    public Status(){}
    public Status(int r, int g, int b, int id, String name, ArrayList<Cell> cells, ArrayList<Rule> set, int toSt) {
        this.color = new vec3(r, g, b);
        this.name = name;
        this.id = id;
        this.cells = cells;
        this.toStatus = toSt;
        this.ruleSet = set;
        setOperator();

    }


    //-----------------------------------------run-----------------------------------------
    @Override
    public synchronized void run() {
        while (!isInterrupted()) {
            this.applyRules();                  //we apply rules and than wait other processes to finish, if needed, i dont really understand yet
            this.cells.clear();
            this.prog.ctrl++;
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
            }

        }
    }

    //------------------------------------methods-----------------------------------------
    private void applyRules() {
        for (Cell c : this.cells) {
            for (int i = 0; i < ruleSet.size(); i++) {
                this.operator[0][i] = ruleOnCell(c, ruleSet.get(i));
            }
            if (deMorganator()) {
                c.futureId = this.toStatus;
            }
            else{c.futureId = this.id;}

        }
    }

    private boolean deMorganator() {                                     // :D
        boolean outcome = true && this.operator[0][0];
        for (int i = 1; i < ruleSet.size(); i++) {
            if (this.operator[1][i]) {
                outcome &= this.operator[0][i];
            } else {
                outcome |= this.operator[0][i];
            }
        }
        return outcome;
    }

    private boolean ruleOnCell(Cell c, Rule r) {                 //apply rule upon a single cell
        if (r.any) {
            if(r.exact){return exactRule(r,c);}
            return anyRule(r, c);
        }
        return specificRule(r, c);
    }

    private boolean anyRule(Rule r, Cell c) {                     //apply "any-neighbor-is-fine-but-minimum-n" rule
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

    private boolean specificRule(Rule r, Cell c) {                //apply "just-specific-neighbors-are-fine-and-precisely-n" rule
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
    private boolean exactRule(Rule r, Cell c) {                     //apply "any-neighbor-is-fine-but-minimum-n" rule
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
            this.operator[1][i] = this.ruleSet.get(i).and;    // fill operator with rule.and values
        }

    }

    public synchronized void wakeup(){
        notify();
    }
}

/*-------------------------------------comments-----------------------------------------
    [0]: the matrix has the following structure:
         {[outcome of applicated rule[0] upon cell c],...,[outcome of applicated rule[i] upon cell c]...}
         {[true(default)],...,[value that say if rule[i] is in and/or with rule[i-1]...}
         its goal is to build many (all possibles?) boolean expressions

    [1]: the neighbors are in a specifc order, in Cell.nearby as in Rule.nearby, the order is the following:
         | 0 | | 1 | | 2 |
         | 3 | | C | | 4 |
         | 5 | | 6 | | 7 |
*/





package simulatorWindow.utils;

public class Rule {
    public int stat;
    public boolean not, any, and,exact;                  //choose if you want to deny a rule, for any cells or specific, and if a rule is in and/or with the previous
    public int nNeighbors;                         //number of neighbors to satisfy the rule
    public int nearStatus;                         //status of neighbors to satisfy the rule
    public int[] nearby=new int[8];                //neighbors status (neighbors are in order,see "comments[1]" in the bottom

    //----------------------------------constructors--------------------------------------
    public Rule(){}
    public Rule(int[] nearby, boolean any, boolean not, boolean and, boolean exact,int nNeighbors,int nearStatus) {
        this.not = not;
        this.any = any;
        this.and = and;
        this.exact=exact;
        this.nearby = nearby;
        this.nNeighbors = nNeighbors;
        this.nearStatus = nearStatus;

    }
}

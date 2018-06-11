package simulatorWindow;

import simulatorWindow.utils.*;

import java.util.ArrayList;

public class Loader {

    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<Integer> nextIds = new ArrayList<>();
    ArrayList<String> colors = new ArrayList<>();
    ArrayList<String> colNames = new ArrayList<>();
    ArrayList<Rule> rulez = new ArrayList<>();

    public ArrayList<Status> parseStates() {
        ArrayList<Status> states = new ArrayList<>();
        Status s;
        for (int i = 0; i < this.ids.size(); i++) {
            s = new Status();
            s.id = ids.get(i);
            s.nextId = nextIds.get(i);
            s.colorName = colNames.get(i);
            s.hexValue = colors.get(i);
            for (int j = 0; j < rulez.size(); j++) {
                if (rulez.get(j).stat==s.id)
                    s.ruleSet.add(rulez.get(j));
            }
            s.setOperator();
            states.add(s);
        }
        return states;
    }
}
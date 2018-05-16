import java.util.ArrayList;

public abstract class Board {

    ArrayList<State> state;

    public abstract void start();   //lancio thread listener che se premi x ammazza tutto

    public abstract void killAll();//ammazza tutti

    public abstract void update();

}

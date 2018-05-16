import java.util.ArrayList;

public abstract class Board {

    int h, w;
    ArrayList<State> state;

    // crea o una o l' altra

    public abstract void start();   //lancio thread listener che se premi x ammazza tutto

    public abstract void killAll();//ammazza tutti

    public abstract void update();


}

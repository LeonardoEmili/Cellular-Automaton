package simulatorWindow.utils;
import java.util.ArrayList;

public class Cell {
    public iVec2 pos;
    public int idStatus;
    public int futureId;
    public iVec3 color;
    public ArrayList<Cell> nearby;
    public Status s;

    public Cell(){}
    public Cell(int idStatus,iVec3 color,iVec2 pos){
        this.idStatus=idStatus;
        this.color=color;
        this.pos=pos;
    }
}

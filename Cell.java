package utils;

import java.util.ArrayList;

public class Cell {
    public ivec2 pos;
    public int idStatus;
    public int futureId;
    public vec3 color;
    public ArrayList<Cell> nearby;

    public Cell(){}
    public Cell(int idStatus,vec3 color,ivec2 pos){
        this.idStatus=idStatus;
        this.color=color;
        this.pos=pos;
    }

}

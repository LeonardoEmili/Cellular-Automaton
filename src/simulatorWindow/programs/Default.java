package simulatorWindow.programs;
import simulatorWindow.utils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.scene.paint.Color;

public class Default extends CellularAutomataProgram {
    public ArrayList<Status> states;
    public Cell[][] grid;
    public volatile int ctrl=0;
    public int h,w;
    public String name;

    public Default(ArrayList<Status> arr,String name){
        this.states=arr;
        this.name=name;
    }
    @Override
    public void setGridDimension(int w,int h){
        this.h=h;
        this.w=w;
        this.grid=new Cell[h][w];
        gridSize = new iVec2(w, h);
        for (int i = 0; i < gridSize.y; i++) {
            for (int j = 0; j < gridSize.x; j++) {
                this.grid[i][j]=new Cell();
                this.grid[i][j].pos=new iVec2(j,i);
            }
        }
        for (int i = 0; i <h; i++) {
            for (int j = 0; j < w; j++) {
                grid[i][j].nearby=new ArrayList<>(Arrays.asList(
                        grid[(i-1+h)%h][(j-1+w)%w],  grid[(i-1+h)%h][(j)],   grid[(i-1+h)%h][(j+1)%w],

                        grid[(i)][(j-1+w)%w],        /*central cell*/        grid[(i)][(j+1)%w],

                        grid[(i+1)%h][(j-1+w)%w],    grid[(i+1)%h][(j)],     grid[(i+1)%h][(j+1)%w]));
            }
        }
    }
    @Override
    public void reset(){
        for (int j = 0; j < gridSize.y; j++) {
            for (int l = 0; l < gridSize.x; l++) {
                this.grid[j][l].s=this.states.get(new Random().nextInt(this.states.size()));
                this.grid[j][l].color= this.grid[j][l].s.hex2Rgb(this.grid[j][l].s.hexValue);
                this.grid[j][l].idStatus=this.grid[j][l].s.id;
                this.grid[j][l].futureId=this.grid[j][l].s.id;
                this.grid[j][l].s.cells.add(this.grid[j][l]);
            }
        }
        for (Status s: this.states){
            s.start();
        }
    }

    @Override
    public void tick(){
        if (this.ctrl>=2){
            this.ctrl=0;
            for (int i = 0; i < gridSize.y; i++) {
                for (int j = 0; j < gridSize.x; j++) {
                    for (Status s : this.states) {
                        if (s.id == this.grid[i][j].futureId) {
                            this.grid[i][j].idStatus = this.grid[i][j].futureId;
                            s.cells.add(this.grid[i][j]);
                            this.grid[i][j].color = s.hex2Rgb(s.colorName);
                        }
                    }
                }
            }
            for (Status s:this.states){s.wakeup();}
        }
    }

    @Override
    public iVec3 getColor(int x, int y) {
        return this.grid[y][x].color;
    }
    @Override
    public String name() {
        return this.name;
    }

}

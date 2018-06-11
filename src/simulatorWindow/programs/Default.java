package simulatorWindow.programs;
import simulatorWindow.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Default extends CellularAutomataProgram {
    final public ArrayList<Status> states;
    public Cell[][] grid;
    //public volatile int ctrl = 0;
    public int h,w;
    public String name;
    public Wrapper myWrapper;

    public Default(ArrayList<Status> arr,String name){
        this.states=arr;
        this.name=name;
        this.myWrapper = new Wrapper();
        for (Status s: states) { s.setWrapper(this.myWrapper); }
        killAll();
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
        Status s;
        for (int j = 0; j < gridSize.y; j++) {
            for (int l = 0; l < gridSize.x; l++) {
                s = this.states.get(new Random().nextInt(this.states.size()));
                this.grid[j][l].color = s.hex2Rgb(s.hexValue);
                this.grid[j][l].idStatus =s.id;
                this.grid[j][l].futureId =s.id;
                s.cells.add(this.grid[j][l]);
            }
        }
        unleashHell();
    }

    private void unleashHell() {
        Thread.State st= states.get(0).getState();
        if (st == Thread.State.RUNNABLE) {
            try {
                for (Status s : this.states) { s.wait(); }
            } catch (InterruptedException ex) {}
        } else if ( st == Thread.State.WAITING) {
            ;
        } else if (st == Thread.State.TERMINATED) {
            ;
        } else {        // Base State
            for (Status s : this.states) {
                s.start();
            }
        }
    }

    @Override
    public void tick() {

        if (myWrapper.getCtrl()>=states.size()){
            myWrapper.setCtrl();

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

            wakeUpAll();
        }
    }
    private void wakeUpAll() {
        for (Status s:this.states){s.wakeup();}
    }

    public void killAll() {
        for (Status s:this.states) {
            s.setDaemon(true);
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
